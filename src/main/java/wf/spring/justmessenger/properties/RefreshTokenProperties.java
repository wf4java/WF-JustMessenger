package wf.spring.justmessenger.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties("spring.security.refresh-token")
@Getter
@Setter
public class RefreshTokenProperties {

    private int length = 64;

    private Duration expireTime = Duration.ofDays(14);

}
