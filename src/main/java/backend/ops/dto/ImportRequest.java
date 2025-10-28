package backend.ops.dto;

import java.util.List;
import java.util.Map;

public record ImportRequest(
        List<String> driveItemIds,
        Map<String, String> idToName,
        Map<String, Long> idToSize
) {}
