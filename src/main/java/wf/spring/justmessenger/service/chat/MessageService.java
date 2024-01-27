package wf.spring.justmessenger.service.chat;

import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.chat.Message;

import java.util.List;
import java.util.Optional;

public interface MessageService {
    Optional<Message> findById(ObjectId id);

    Message getById(ObjectId id);

    List<Message> getNewMessagesByChatIdAndOffsetMessageId(ObjectId chatId, ObjectId offsetMessageId, int limit);

    List<Message> getOldMessagesByChatIdAndOffsetMessageId(ObjectId chatId, ObjectId offsetMessageId, int limit);

    boolean existsByIdAndChatId(ObjectId id, ObjectId chatId);

    Message save(Message message);
}
