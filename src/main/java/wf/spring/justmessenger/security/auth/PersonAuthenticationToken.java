package wf.spring.justmessenger.security.auth;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;
import wf.spring.justmessenger.entity.person.Person;

import java.util.Collection;

public class PersonAuthenticationToken extends AbstractAuthenticationToken {


    private final Person principal;
    @Getter
    private Object credentials;

    public PersonAuthenticationToken(Person principal, Object credentials) {
        super(null);
        this.principal = principal;
        this.credentials = credentials;
        this.setAuthenticated(false);
    }

    public PersonAuthenticationToken(Person principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        this.credentials = credentials;
        super.setAuthenticated(true);
    }

    public static PersonAuthenticationToken unauthenticated(Person principal, Object credentials) {
        return new PersonAuthenticationToken(principal, credentials);
    }

    public static PersonAuthenticationToken authenticated(Person principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        return new PersonAuthenticationToken(principal, credentials, authorities);
    }

    public Person getPrincipal() {
        return this.principal;
    }

    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        Assert.isTrue(!isAuthenticated, "Cannot set this token to trusted - use constructor which takes a GrantedAuthority list instead");
        super.setAuthenticated(false);
    }

    public void eraseCredentials() {
        super.eraseCredentials();
        this.credentials = null;
    }
}
