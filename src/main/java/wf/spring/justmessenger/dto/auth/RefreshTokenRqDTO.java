package wf.spring.justmessenger.dto.auth;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshTokenRqDTO {

    @NotNull
    @NotEmpty
    private String refreshToken;

}
