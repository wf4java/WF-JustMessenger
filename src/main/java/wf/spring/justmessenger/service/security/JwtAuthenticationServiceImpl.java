package wf.spring.justmessenger.service.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.properties.JwtProperties;
import wf.spring.justmessenger.security.auth.PersonAuthenticationToken;


import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class JwtAuthenticationServiceImpl implements JwtAuthenticationService {

    private final PersonDetailsService personDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;


    @Override
    public String validateAndGenerateToken(String username, String password) throws AuthenticationException {
        Person personDetails = personDetailsService.loadUserByUsername(username);

        if (!passwordEncoder.matches(password, personDetails.getPassword()))
            throw new BadCredentialsException("Authenticate failed!");

        if (!personDetails.isEnabled()) throw new DisabledException("This account not activated");
        return generateTokenFromId(personDetails.getId());
    }


    @Override
    public PersonAuthenticationToken getAuthenticatedOrFail(String jwtToken) throws AuthenticationException {
        if (jwtToken == null)
            throw new AuthenticationCredentialsNotFoundException("Token was empty");

        String id;

        try {id = validateTokenAndRetrieveSubjectToId(jwtToken);}
        catch (JWTVerificationException exception) {throw new BadCredentialsException("Token not valid");}

        Person personDetails = personDetailsService.loadUserById(id);

        if (!personDetails.isEnabled()) throw new DisabledException("This account not activated");

        PersonAuthenticationToken authenticationToken = new PersonAuthenticationToken(
                personDetails,
                personDetails.getPassword(),
                personDetails.getAuthorities()
        );
        authenticationToken.setDetails(personDetails);

        return authenticationToken;
    }




    @Override
    public String generateTokenFromId(ObjectId id) {
        Date expirationDate = Date.from(ZonedDateTime.now().plus(jwtProperties.getExpireTime()).toInstant());

        return JWT.create()
                .withSubject("user_details")
                .withClaim("id", id.toHexString())
                .withIssuer(jwtProperties.getIssuer())
                .withIssuedAt(new Date())
                .withExpiresAt(expirationDate)
                .sign(Algorithm.HMAC256(jwtProperties.getSecretKey()));
    }



    private String validateTokenAndRetrieveSubjectToId(String token) throws JWTVerificationException {
        JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(jwtProperties.getSecretKey()))
                .withSubject("user_details")
                .withIssuer(jwtProperties.getIssuer())
                .build();

        return Optional.ofNullable(jwtVerifier.verify(token).getClaim("id").asString())
                .orElseThrow(() -> new AuthenticationServiceException("JWT token invalid"));
    }

}
