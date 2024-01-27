package wf.spring.justmessenger.repository.chat;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.GroupChat;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatRepository extends MongoRepository<GroupChat, ObjectId> {

    public List<GroupChat> findAllByChatIdIsIn(Collection<ObjectId> chatIds);

}
