//ftp service
package backend.ops.service;

import backend.ops.config.FtpConfig;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

/**
 * Connect on-demand only. Nothing runs at app startup.
 */
@Service
public class FtpService {

    private final FtpConfig props;

    public FtpService(FtpConfig props) {
        this.props = props;
    }

    /** Create and login a fresh client only when needed. */
    private FTPClient connect() throws Exception {
        if (!props.isEnabled()) {
            throw new IllegalStateException("FTP is disabled (set ftp.enabled=true to use).");
        }
        FTPClient ftp = new FTPClient();
        ftp.setConnectTimeout(5000);
        ftp.connect(props.getHost(), props.getPort());

        if (!ftp.login(props.getUser(), props.getPassword())) {
            try { ftp.disconnect(); } catch (Exception ignore) {}
            throw new IllegalStateException("FTP login failed for user " + props.getUser());
        }
        ftp.enterLocalPassiveMode();
        ftp.setFileType(FTP.BINARY_FILE_TYPE);
        return ftp;
    }

    private void safeClose(FTPClient ftp) {
        if (ftp != null && ftp.isConnected()) {
            try { ftp.logout(); } catch (Exception ignore) {}
            try { ftp.disconnect(); } catch (Exception ignore) {}
        }
    }

    /** Existing method: returns file names from remoteDir. */
    public String[] listRemoteFiles() throws Exception {
        FTPClient ftp = null;
        try {
            ftp = connect();
            String[] names = ftp.listNames(props.getRemoteDir());
            return names != null ? names : new String[0];
        } finally {
            safeClose(ftp);
        }
    }

    /**
     * NEW: Provide the method that OpsController expects.
     * Delegates to listRemoteFiles() and returns a List<String>.
     */
    public List<String> fetchAllFiles() throws Exception {
        return Arrays.asList(listRemoteFiles());
    }

    /**
     * Download a single file from remoteDir to localDir.
     * Returns the absolute local path.
     */
    public String download(String remoteFileName) throws Exception {
        FTPClient ftp = null;
        try {
            ftp = connect();

            Path localDir = Path.of(props.getLocalDir());
            Files.createDirectories(localDir);

            Path localFile = localDir.resolve(remoteFileName);
            String remotePath = (props.getRemoteDir() + "/" + remoteFileName).replace("\\", "/");

            try (OutputStream os = new FileOutputStream(localFile.toFile())) {
                boolean ok = ftp.retrieveFile(remotePath, os);
                if (!ok) {
                    throw new IllegalStateException("FTP retrieve failed for " + remotePath);
                }
            }
            return localFile.toAbsolutePath().toString();
        } finally {
            safeClose(ftp);
        }
    }
}
