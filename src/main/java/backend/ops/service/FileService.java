package backend.ops.service;

import backend.ops.model.StoredFile;                      // <-- FIXED
import backend.ops.repository.StoredFileRepository;       // <-- FIXED
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {
    private final StoredFileRepository repo;
    public FileService(StoredFileRepository repo) { this.repo = repo; }

    public List<StoredFile> getAllFiles() { return repo.findAll(); }

    public StoredFile updateCategory(Long id, String category) {
        var file = repo.findById(id).orElseThrow();
        file.setCategory(category);
        return repo.save(file);
    }
}

