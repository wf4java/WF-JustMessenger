package wf.spring.justmessenger.entity.person;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.messaging.simp.user.DestinationUserNameProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import wf.spring.justmessenger.model.Gender;

import java.security.Principal;
import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "person")
public class Person implements Principal, DestinationUserNameProvider, UserDetails {


    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private String username;

    @Indexed(unique = true)
    private String email;

    private String password;

    private Boolean active;

    private Gender gender;

    private UUID profilePhoto;

    @NotNull
    private Status selectedStatus;



    public enum Status {

        ONLINE,
        NOT_DISTURB,
        NOT_ACTIVE,
        INVISIBLE;

    }




    @Override
    public Person clone() {
        try {return (Person) super.clone();} catch (CloneNotSupportedException e) {throw new RuntimeException(e);}
    }

    @Override
    public String getName() {
        return username;
    }

    @Override
    @NonNull
    public String getDestinationUserName() {
        return id.toHexString();
    }



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return getActive();
    }

}
