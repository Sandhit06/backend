package backend.ops.service;

import backend.ops.dto.ImportRequest;
import backend.ops.model.FileStatus;
import backend.ops.model.StoredFile;
import backend.ops.repository.StoredFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class OpsService {

    private final StoredFileRepository repo;

    public OpsService(StoredFileRepository repo) {
        this.repo = repo;
    }

    private String extractPrefix(String fileName) {
        String base = fileName;
        int dot = base.lastIndexOf('.');
        if (dot > 0) base = base.substring(0, dot);
        int idx = base.indexOf("__");
        if (idx < 0) idx = base.indexOf('_');
        if (idx < 0) return base;
        return base.substring(0, idx);
    }

    @Transactional
    public List<StoredFile> importItems(ImportRequest req) {
        return req.driveItemIds().stream().map(id -> {
            String name = req.idToName() != null ? req.idToName().getOrDefault(id, id) : id;
            Long size = req.idToSize() != null ? req.idToSize().getOrDefault(id, 0L) : 0L;
            String prefix = extractPrefix(name);
            StoredFile sf = new StoredFile();
            sf.setDriveItemId(id);
            sf.setFileName(name);
            sf.setDomainPrefix(prefix);
            sf.setSourcePath("incoming/" + name);
            sf.setSizeBytes(size);
            sf.setStatus(FileStatus.IMPORTED);
            return repo.save(sf);
        }).toList();
    }
}
