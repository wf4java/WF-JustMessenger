package wf.spring.justmessenger.repository.chat;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.Attachment;

import java.util.*;

@Repository
public interface AttachmentRepository extends MongoRepository<Attachment, ObjectId> {

    public boolean existsByIdAndChatId(ObjectId id, ObjectId chatId);


    public List<Attachment> findAllByIdInAndChatId(Collection<ObjectId> ids, ObjectId chatId);

}
