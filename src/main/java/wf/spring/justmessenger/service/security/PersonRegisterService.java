package wf.spring.justmessenger.service.security;

import org.springframework.transaction.annotation.Transactional;
import wf.spring.justmessenger.dto.auth.RegisterRqDTO;

public interface PersonRegisterService {
    @Transactional
    void register(RegisterRqDTO registerRqDTO);
}
