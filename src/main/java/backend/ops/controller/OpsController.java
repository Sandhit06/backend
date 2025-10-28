package backend.ops.controller;

import backend.ops.dto.FileItemResponse;
import backend.ops.dto.ImportRequest;
import backend.ops.model.StoredFile;
import backend.ops.service.OpsService;
import backend.ops.service.OneDriveService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/ops")
@CrossOrigin(origins = "http://localhost:3000")
public class OpsController {

    private final OneDriveService oneDriveService;
    private final OpsService opsService;

    public OpsController(OneDriveService oneDriveService, OpsService opsService) {
        this.oneDriveService = oneDriveService;
        this.opsService = opsService;
    }

    /** List PDFs from OneDrive incoming/ (stubbed for now) */
    @GetMapping("/incoming")
    public List<Map<String, Object>> listIncoming(@RequestParam(required = false) String prefix,
                                                  @RequestParam(defaultValue = "50") int size) {
        return oneDriveService.listIncomingPdfs(prefix, size).stream()
                .map(i -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("id", i.id);
                    m.put("name", i.name);
                    m.put("size", Long.valueOf(i.size));
                    return m;
                })
                .toList();
    }

    /** Import selected files (create DB rows) */
    @PostMapping("/import")
    public List<FileItemResponse> importSelection(@RequestBody ImportRequest req) {
        List<StoredFile> saved = opsService.importItems(req);
        return saved.stream().map(f -> new FileItemResponse(
                f.getId(), f.getFileName(), f.getDomainPrefix(), f.getSourcePath(), f.getOutputPath(),
                f.getStatus().name()
        )).toList();
    }

    /** List files from OneDrive Published Reports folder */
    @GetMapping("/published")
    public List<Map<String, Object>> listPublished() {
        return oneDriveService.listPublishedItems().stream().map(pi -> {
            Map<String, Object> m = new HashMap<>();
            m.put("driveId", pi.driveId());
            m.put("id", pi.id());
            m.put("name", pi.name());
            m.put("size", pi.size());
            m.put("webUrl", pi.webUrl());
            m.put("lastModified", pi.lastModified());
            return m;
        }).toList();
    }

    /** Move a selected published file to the OneDrive incoming folder */
    @PostMapping("/published/import")
    public Map<String, Object> movePublishedToIncoming(@RequestBody Map<String, String> body) {
        String driveId = body.get("driveId");
        String itemId = body.get("itemId");
        String newName = body.getOrDefault("newName", null);
        oneDriveService.moveItemToIncoming(driveId, itemId, newName);
        Map<String, Object> resp = new HashMap<>();
        resp.put("moved", true);
        resp.put("to", "incoming");
        return resp;
    }
}
