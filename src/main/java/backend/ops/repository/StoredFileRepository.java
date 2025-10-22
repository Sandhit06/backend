//stored file repo
package backend.ops.repository;

import backend.ops.model.StoredFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StoredFileRepository extends JpaRepository<StoredFile, Long> {

//    Optional<StoredFile> findByFileName(String fileName);
}
