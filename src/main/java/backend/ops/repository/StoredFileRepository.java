package backend.ops.repository;

import backend.ops.model.StoredFile;
import backend.ops.model.FileStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {
    List<StoredFile> findByStatusIn(List<FileStatus> statuses);
}
