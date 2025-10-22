// ftp config
package backend.ops.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Pure configuration holder for FTP settings.
 * No connection is attempted at application startup.
 */
@Configuration
@ConfigurationProperties(prefix = "ftp")
public class FtpConfig {

    private boolean enabled = false;
    private String host = "localhost";
    private int port = 21;
    private String user = "anonymous";
    private String password = "";
    private String remoteDir = "/incoming";
    private String localDir = "./rw-storage";

    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }

    public String getHost() { return host; }
    public void setHost(String host) { this.host = host; }

    public int getPort() { return port; }
    public void setPort(int port) { this.port = port; }

    public String getUser() { return user; }
    public void setUser(String user) { this.user = user; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRemoteDir() { return remoteDir; }
    public void setRemoteDir(String remoteDir) { this.remoteDir = remoteDir; }

    public String getLocalDir() { return localDir; }
    public void setLocalDir(String localDir) { this.localDir = localDir; }
}
