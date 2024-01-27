package wf.spring.justmessenger.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.server.resource.authentication.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;
import wf.spring.justmessenger.service.security.JwtAuthenticationService;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationManager implements AuthenticationManager {

    private final JwtAuthenticationService jwtAuthenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if(!(authentication instanceof BearerTokenAuthenticationToken)) throw new AuthenticationServiceException("Authentication is not BearerTokenAuthenticationToken");

        return jwtAuthenticationService.getAuthenticatedOrFail(((BearerTokenAuthenticationToken) authentication).getToken());
    }


}
