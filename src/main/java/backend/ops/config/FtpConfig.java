package backend.ops.config;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FtpConfig {
    @Value("${ftp.host}") private String host;
    @Value("${ftp.port}") private int port;
    @Value("${ftp.user}") private String user;
    @Value("${ftp.password}") private String password;

    @Bean
    public FTPClient ftpClient() throws Exception {
        FTPClient c = new FTPClient();
        c.connect(host, port);
        c.login(user, password);
        c.enterLocalPassiveMode();
        return c;
    }
}
