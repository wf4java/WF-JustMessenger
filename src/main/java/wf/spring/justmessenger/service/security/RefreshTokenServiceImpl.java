package wf.spring.justmessenger.service.security;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.person.RefreshToken;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.model.exception.RefreshTokenException;
import wf.spring.justmessenger.model.exception.basic.ServerException;
import wf.spring.justmessenger.properties.RefreshTokenProperties;
import wf.spring.justmessenger.repository.person.RefreshTokenRepository;
import wf.spring.justmessenger.utils.RefreshTokenGenerator;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {


    private final RefreshTokenProperties refreshTokenProperties;
    private final RefreshTokenRepository refreshTokenRepository;


    @Override
    public RefreshToken generateNewToken(ObjectId personId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setPersonId(personId);
        refreshToken.setToken(getUniqueToken());
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenProperties.getExpireTime()));

        return refreshTokenRepository.save(refreshToken);
    }

    @Override
    public RefreshToken validateAndGet(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new RefreshTokenException("Refresh token invalid!"));

        if(refreshToken.getExpiryDate().isBefore(Instant.now()))
            throw new RefreshTokenException("Refresh token has expired!");

        if(!refreshToken.getToken().equals(token))
            throw new RefreshTokenException("Refresh token invalid!");

        return refreshToken;
    }

    @Override
    public String getUniqueToken() {
        for(int i = 0; i < 10; i++) {
            String token = RefreshTokenGenerator.generateToken(refreshTokenProperties.getLength());
            if(!refreshTokenRepository.existsByToken(token))
                return token;
        }
        throw new ServerException("Refresh token generate error!");
    }

}
