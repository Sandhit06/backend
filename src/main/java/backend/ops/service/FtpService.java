package backend.ops.service;

import backend.ops.model.StoredFile;                      // <-- FIXED
import backend.ops.repository.StoredFileRepository;       // <-- FIXED
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.time.LocalDateTime;

@Service
public class FtpService {
    private final FTPClient ftpClient;
    private final StoredFileRepository repo;

    @Value("${ftp.remote-dir}") private String remoteDir;
    @Value("${ftp.local-dir}")  private String localDir;

    public FtpService(FTPClient ftpClient, StoredFileRepository repo) {
        this.ftpClient = ftpClient;
        this.repo = repo;
    }

    public String fetchAllFiles() throws Exception {
        ftpClient.changeWorkingDirectory(remoteDir);
        var files = ftpClient.listFiles();

        for (var file : files) {
            if (file.isFile()) {
                String localPath = localDir + "/" + file.getName();
                try (OutputStream out = new FileOutputStream(localPath)) {
                    if (ftpClient.retrieveFile(file.getName(), out)) {
                        StoredFile f = new StoredFile();
                        f.setFileName(file.getName());
                        f.setCategory("Uncategorized");
                        f.setSource(remoteDir);
                        f.setUploadedAt(LocalDateTime.now());
                        f.setSize(file.getSize());
                        f.setStatus("pending");
                        f.setLocalPath(localPath);
                        repo.save(f);
                    }
                }
            }
        }
        return "Files fetched successfully";
    }
}
