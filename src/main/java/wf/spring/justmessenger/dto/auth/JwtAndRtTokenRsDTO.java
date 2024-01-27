package wf.spring.justmessenger.dto.auth;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class JwtAndRtTokenRsDTO {

    private String jwtToken;

    private String refreshToken;

}