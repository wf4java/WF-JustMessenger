package wf.spring.justmessenger.service.security;

import org.bson.types.ObjectId;

public interface SubscribeService {
    void unsubscribePerson(ObjectId personId, String destination);

    void unsubscribeFromGroup(ObjectId personId, ObjectId chatId);
}
