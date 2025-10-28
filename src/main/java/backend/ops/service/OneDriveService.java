package backend.ops.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OneDriveService {

    public static class DriveItemLite {
        public String id;
        public String name;
        public long size;
        public DriveItemLite(String id, String name, long size) {
            this.id = id; this.name = name; this.size = size;
        }
    }

    private final org.springframework.web.client.RestTemplate http = new org.springframework.web.client.RestTemplate();

    @org.springframework.beans.factory.annotation.Value("${app.msgraph.tenantId:}")
    private String tenantId;
    @org.springframework.beans.factory.annotation.Value("${app.msgraph.clientId:}")
    private String clientId;
    @org.springframework.beans.factory.annotation.Value("${app.msgraph.clientSecret:}")
    private String clientSecret;

    @org.springframework.beans.factory.annotation.Value("${app.msgraph.publishedShareLink:}")
    private String publishedShareLink;
    @org.springframework.beans.factory.annotation.Value("${app.msgraph.incomingShareLink:}")
    private String incomingShareLink;

    private volatile String cachedToken;
    private volatile long tokenExpiryEpochSec;

    private String bearer() {
        long now = System.currentTimeMillis() / 1000;
        if (cachedToken != null && now < tokenExpiryEpochSec - 60) {
            return cachedToken;
        }
        var form = new org.springframework.util.LinkedMultiValueMap<String, String>();
        form.add("client_id", clientId);
        form.add("scope", "https://graph.microsoft.com/.default");
        form.add("client_secret", clientSecret);
        form.add("grant_type", "client_credentials");
        var headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_FORM_URLENCODED);
        var req = new org.springframework.http.HttpEntity<>(form, headers);
        @SuppressWarnings("unchecked")
        Map<String, Object> token = http.postForObject(
                "https://login.microsoftonline.com/" + tenantId + "/oauth2/v2.0/token",
                req, Map.class);
        if (token == null || token.get("access_token") == null) {
            throw new IllegalStateException("Failed to acquire Graph token");
        }
        cachedToken = "Bearer " + token.get("access_token");
        Object exp = token.get("expires_in");
        long ttl = exp instanceof Number ? ((Number) exp).longValue() : 3600L;
        tokenExpiryEpochSec = now + ttl;
        return cachedToken;
    }

    private static String encodeShareUrl(String raw) {
        String b64 = java.util.Base64.getUrlEncoder().withoutPadding().encodeToString(raw.getBytes(java.nio.charset.StandardCharsets.UTF_8));
        return "u!" + b64;
    }

    private Map<String, Object> resolveShare(String shareLink) {
        String enc = encodeShareUrl(shareLink);
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", bearer());
        var req = new org.springframework.http.HttpEntity<Void>(headers);
        var resp = http.exchange(
                "https://graph.microsoft.com/v1.0/shares/" + enc + "/driveItem",
                org.springframework.http.HttpMethod.GET, req, Map.class);
        return resp.getBody();
    }

    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> listChildren(String driveId, String itemId) {
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", bearer());
        var req = new org.springframework.http.HttpEntity<Void>(headers);
        var url = "https://graph.microsoft.com/v1.0/drives/" + driveId + "/items/" + itemId + "/children?$top=200";
        var resp = http.exchange(url, org.springframework.http.HttpMethod.GET, req, Map.class);
        Map<String, Object> body = resp.getBody();
        if (body == null) return List.of();
        Object value = body.get("value");
        if (value instanceof List<?>) {
            return (List<Map<String, Object>>) value;
        }
        return List.of();
    }

    public record PublishedItem(String driveId, String id, String name, long size, String webUrl, String lastModified) {}

    public List<PublishedItem> listPublishedItems() {
        if (publishedShareLink == null || publishedShareLink.isBlank() || tenantId == null || tenantId.isBlank() || clientId == null || clientId.isBlank() || clientSecret == null || clientSecret.isBlank()) {
            return List.of();
        }
        Map<String, Object> root = resolveShare(publishedShareLink);
        if (root == null) return List.of();
        Map<String, Object> parentRef = (Map<String, Object>) root.get("parentReference");
        String driveId = parentRef != null ? (String) parentRef.get("driveId") : null;
        String itemId = (String) root.get("id");
        if (driveId == null || itemId == null) return List.of();
        return listChildren(driveId, itemId).stream().filter(m -> m.get("file") != null)
                .map(m -> {
                    Map<String, Object> pref = (Map<String, Object>) m.get("parentReference");
                    String childDriveId = pref != null ? (String) pref.get("driveId") : driveId;
                    return new PublishedItem(
                            childDriveId,
                            (String) m.get("id"),
                            (String) m.get("name"),
                            ((Number) m.getOrDefault("size", 0)).longValue(),
                            (String) m.get("webUrl"),
                            (String) m.get("lastModifiedDateTime")
                    );
                })
                .toList();
    }

    private record DriveRef(String driveId, String itemId) {}

    private DriveRef rootFromShare(String shareLink) {
        Map<String, Object> root = resolveShare(shareLink);
        if (root == null) return null;
        @SuppressWarnings("unchecked")
        Map<String, Object> parentRef = (Map<String, Object>) root.get("parentReference");
        String driveId = parentRef != null ? (String) parentRef.get("driveId") : null;
        String itemId = (String) root.get("id");
        if (driveId == null || itemId == null) return null;
        return new DriveRef(driveId, itemId);
    }

    public void moveItemToIncoming(String fromDriveId, String itemId, String newName) {
        DriveRef incoming = rootFromShare(incomingShareLink);
        if (incoming == null) throw new IllegalStateException("Incoming share not resolvable");
        var headers = new org.springframework.http.HttpHeaders();
        headers.set("Authorization", bearer());
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        var parentRef = new java.util.HashMap<String, Object>();
        parentRef.put("id", incoming.itemId());
        if (!incoming.driveId().equals(fromDriveId)) {
            parentRef.put("driveId", incoming.driveId());
        }
        var body = new java.util.HashMap<String, Object>();
        body.put("parentReference", parentRef);
        if (newName != null && !newName.isBlank()) body.put("name", newName);
        var req = new org.springframework.http.HttpEntity<>(body, headers);
        String url = "https://graph.microsoft.com/v1.0/drives/" + fromDriveId + "/items/" + itemId;
        http.exchange(url, org.springframework.http.HttpMethod.PATCH, req, Map.class);
    }

    public List<DriveItemLite> listIncomingPdfs(String prefix, int size) {
        return List.of();
    }

    public void noop() {}
}
