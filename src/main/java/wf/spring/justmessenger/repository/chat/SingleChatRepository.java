package wf.spring.justmessenger.repository.chat;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.SingleChat;

import java.util.List;
import java.util.Optional;

@Repository
public interface SingleChatRepository extends MongoRepository<SingleChat, ObjectId> {

    public Optional<SingleChat> findByFirstPersonIdAndSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId);

    public List<SingleChat> findByFirstPersonIdOrSecondPersonId(ObjectId firstPersonId, ObjectId secondPersonId);

}
