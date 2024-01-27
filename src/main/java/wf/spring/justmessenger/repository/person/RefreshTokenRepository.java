package wf.spring.justmessenger.repository.person;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import wf.spring.justmessenger.entity.person.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends MongoRepository<RefreshToken, ObjectId> {

    public Optional<RefreshToken> findByToken(String token);

    public boolean existsByToken(String token);

}
