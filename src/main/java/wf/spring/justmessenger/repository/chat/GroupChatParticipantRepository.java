package wf.spring.justmessenger.repository.chat;

import jakarta.validation.constraints.NotNull;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import wf.spring.justmessenger.entity.chat.GroupChatParticipant;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupChatParticipantRepository extends MongoRepository<GroupChatParticipant, ObjectId> {

    public boolean existsByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    public long removeByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    public List<GroupChatParticipant> findAllByPersonId(ObjectId personId);

    public List<GroupChatParticipant> findAllByGroupId(ObjectId groupId);

    public Optional<GroupChatParticipant> findByGroupIdAndPersonId(ObjectId groupId, ObjectId personId);

    public void deleteAllByGroupId(ObjectId groupId);

}
