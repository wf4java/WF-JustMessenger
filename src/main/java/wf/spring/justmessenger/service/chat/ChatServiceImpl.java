package wf.spring.justmessenger.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.chat.Chat;
import wf.spring.justmessenger.repository.chat.ChatRepository;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final ChatRepository chatRepository;

    @Override
    public Chat createChat() {
        return chatRepository.save(new Chat());
    }

}
