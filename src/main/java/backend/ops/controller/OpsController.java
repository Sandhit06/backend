//ops controller
package backend.ops.controller;


import backend.ops.model.StoredFile;
import backend.ops.service.FileService;
import backend.ops.service.FtpService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ops")
@CrossOrigin(origins = "http://localhost:3000")
public class OpsController {
    private final FtpService ftpService;
    private final FileService fileService;

    public OpsController(FtpService ftpService, FileService fileService) {
        this.ftpService = ftpService;
        this.fileService = fileService;
    }

    @GetMapping("/sync")
    public ResponseEntity<String> fetchFiles() throws Exception {
        return ResponseEntity.ok(ftpService.fetchAllFiles().toString());
    }

    @GetMapping("/files")
    public ResponseEntity<List<StoredFile>> listFiles() {
        return ResponseEntity.ok(fileService.getAllFiles());
    }

    @PutMapping("/files/{id}/category")
    public ResponseEntity<StoredFile> updateCategory(@PathVariable Long id, @RequestParam String category) {
        return ResponseEntity.ok(fileService.updateCategory(id, category));
    }
}

