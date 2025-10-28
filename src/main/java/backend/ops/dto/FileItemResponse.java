package backend.ops.dto;

public record FileItemResponse(
        Long id,
        String fileName,
        String domainPrefix,
        String sourcePath,
        String outputPath,
        String status
) {}
