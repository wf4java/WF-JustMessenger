package wf.spring.justmessenger.service.security;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.RefreshToken;

public interface RefreshTokenService {
    RefreshToken generateNewToken(ObjectId personId);

    RefreshToken validateAndGet(String token);

    String getUniqueToken();
}
