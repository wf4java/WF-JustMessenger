package wf.spring.justmessenger.entity.person;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "refresh_token")
public class RefreshToken {

    @Id
    @NotNull
    private ObjectId personId;

    @NotNull
    @NotEmpty
    @Indexed(unique = true)
    private String token;

    @NotNull
    private Instant expiryDate;


}
