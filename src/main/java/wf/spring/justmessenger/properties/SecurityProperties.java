package wf.spring.justmessenger.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.net.InetAddress;
import java.util.List;

@ConfigurationProperties("spring.security")
@Getter
@Setter
public class SecurityProperties {

    private List<String> corsAllowedOrigins;

}
