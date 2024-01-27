package wf.spring.justmessenger.repository.chat;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.Chat;

import java.io.Serializable;

@Repository
public interface ChatRepository extends MongoRepository<Chat, ObjectId> {



}
