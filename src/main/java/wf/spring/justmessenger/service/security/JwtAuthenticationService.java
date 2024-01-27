package wf.spring.justmessenger.service.security;

import org.bson.types.ObjectId;
import org.springframework.security.core.AuthenticationException;
import wf.spring.justmessenger.security.auth.PersonAuthenticationToken;

public interface JwtAuthenticationService {
    String validateAndGenerateToken(String username, String password) throws AuthenticationException;

    PersonAuthenticationToken getAuthenticatedOrFail(String jwtToken) throws AuthenticationException;

    String generateTokenFromId(ObjectId id);
}
