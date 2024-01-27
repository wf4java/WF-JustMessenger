package wf.spring.justmessenger.service.security;

import wf.spring.justmessenger.dto.auth.AuthenticationRqDTO;
import wf.spring.justmessenger.dto.auth.JwtAndRtTokenRsDTO;
import wf.spring.justmessenger.dto.auth.RefreshTokenRqDTO;

public interface AuthenticationService {
    JwtAndRtTokenRsDTO authenticate(AuthenticationRqDTO authenticationRqDTO);

    JwtAndRtTokenRsDTO refreshToken(RefreshTokenRqDTO refreshTokenRqDTO);
}
