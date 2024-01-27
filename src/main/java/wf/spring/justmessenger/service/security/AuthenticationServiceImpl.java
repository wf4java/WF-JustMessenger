package wf.spring.justmessenger.service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.auth.AuthenticationRqDTO;
import wf.spring.justmessenger.dto.auth.JwtAndRtTokenRsDTO;
import wf.spring.justmessenger.dto.auth.RefreshTokenRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.entity.person.RefreshToken;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final RefreshTokenService refreshTokenService;

    @Override
    public JwtAndRtTokenRsDTO authenticate(AuthenticationRqDTO authenticationRqDTO) {
        String jwtToken = jwtAuthenticationService.validateAndGenerateToken(authenticationRqDTO.getUsername(), authenticationRqDTO.getPassword());
        Person principal = jwtAuthenticationService.getAuthenticatedOrFail(jwtToken).getPrincipal();
        RefreshToken refreshToken = refreshTokenService.generateNewToken(principal.getId());

        return new JwtAndRtTokenRsDTO(jwtToken, refreshToken.getToken());
    }

    @Override
    public JwtAndRtTokenRsDTO refreshToken(RefreshTokenRqDTO refreshTokenRqDTO) {
        RefreshToken refreshToken = refreshTokenService.validateAndGet(refreshTokenRqDTO.getRefreshToken());

        String jwtToken = jwtAuthenticationService.generateTokenFromId(refreshToken.getPersonId());
        RefreshToken newRefreshToken = refreshTokenService.generateNewToken(refreshToken.getPersonId());

        return new JwtAndRtTokenRsDTO(jwtToken, newRefreshToken.getToken());
    }

}
