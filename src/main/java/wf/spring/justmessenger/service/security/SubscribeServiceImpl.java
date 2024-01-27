package wf.spring.justmessenger.service.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.bson.types.ObjectId;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.messaging.*;
import wf.spring.justmessenger.entity.person.Person;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SubscribeServiceImpl implements SubscribeService {



    private final PersonSubscribeMap personSubscribeMap = new PersonSubscribeMap();
    private final SimpMessagingTemplate simpMessagingTemplate;


    @Override
    public void unsubscribePerson(ObjectId personId, String destination){
        SubscribeMap subscribeMap = personSubscribeMap.getSubscribeMap(personId);
        if(subscribeMap == null) return;
        
        for(SubscribeKey subscribeKey : subscribeMap.getSubscribeKeys(destination)) {
            personSubscribeMap.removeByDestination(personId, subscribeKey.getSessionId(), destination);

            StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.UNSUBSCRIBE);

            accessor.setSessionId(subscribeKey.getSessionId());
            accessor.setSubscriptionId(subscribeKey.getSubscribeId());
            accessor.setUser(subscribeMap.getPerson());

            simpMessagingTemplate.getMessageChannel().send(new GenericMessage<>(new byte[0], accessor.getMessageHeaders()));
        }
    }


    @EventListener
    public void handleWebSocketConnectListener(SessionConnectEvent event) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        if(accessor == null) return;

        Person principal = (Person) Objects.requireNonNull(event.getUser());

        personSubscribeMap.connect(principal, accessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        if(accessor == null) return;

        Person principal = (Person) Objects.requireNonNull(event.getUser());

        personSubscribeMap.disconnect(principal.getId(), accessor.getSessionId());
    }

    @EventListener
    public void handleWebSocketSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        if(accessor == null) return;

        Person principal = (Person) Objects.requireNonNull(event.getUser());

        personSubscribeMap.add(principal.getId(), accessor.getSessionId(), accessor.getDestination(), accessor.getSubscriptionId());
    }

    @EventListener
    public void handleWebSocketUnsubscribeListener(SessionUnsubscribeEvent event) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);
        if(accessor == null) return;

        Person principal = (Person) Objects.requireNonNull(event.getUser());

        personSubscribeMap.removeBySubscribeId(principal.getId(), accessor.getSessionId(), accessor.getSubscriptionId());
    }

    @Override
    public void unsubscribeFromGroup(ObjectId personId, ObjectId chatId){
        unsubscribePerson(personId, "/topic/group_chat/" + chatId.toHexString());
    }


    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class PersonSubscribeMap {

        private final HashMap<ObjectId, SubscribeMap> map = new HashMap<>();

        public void connect(Person principal, String sessionId) {
            map.computeIfAbsent(principal.getId(), value -> new SubscribeMap(principal)).connect(sessionId);
        }

        public void disconnect(ObjectId objectId, String sessionId) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return;

            subscribeMap.disconnect(sessionId);
            if(subscribeMap.isEmpty()) map.remove(objectId);
        }


        public void add(ObjectId objectId, String sessionId, String destination, String subscribeId) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return;

            subscribeMap.add(sessionId, destination, subscribeId);
        }

        public void removeByDestination(ObjectId objectId, String sessionId, String destination) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return;

            subscribeMap.removeByDestination(sessionId, destination);
        }

        public void removeBySubscribeId(ObjectId objectId, String sessionId, String subscribeId) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return;

            subscribeMap.removeBySubscribeId(sessionId, subscribeId);
        }

        public boolean contains(ObjectId objectId) {
            return map.containsKey(objectId);
        }

        public boolean contains(ObjectId objectId, String sessionId) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return false;

            return subscribeMap.contains(sessionId);
        }


        public String getSubscribeId(ObjectId objectId, String sessionId, String destination) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return null;

            return subscribeMap.getSubscribeId(sessionId, destination);
        }

        public SubscribeMap getSubscribeMap(ObjectId objectId) {
            return map.get(objectId);
        }

        public List<SubscribeKey> getSubscribeKeys(ObjectId objectId, String destination) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return Collections.emptyList();

            return subscribeMap.getSubscribeKeys(destination);
        }

        public boolean isEmpty(ObjectId objectId, String sessionId) {
            SubscribeMap subscribeMap = map.get(objectId);
            if(subscribeMap == null) return true;

            return subscribeMap.isEmpty(sessionId);
        }


    }


    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class SubscribeMap {
        private final Person person;
        private final HashMap<String, Subscribe> map = new HashMap<>(1);

        public void connect(String sessionId) {
            map.put(sessionId, new Subscribe());
        }

        public void disconnect(String sessionId) {
            map.remove(sessionId);
        }

        public void add(String sessionId, String destination, String subscribeId) {
            map.computeIfAbsent(sessionId, key -> new Subscribe()).put(destination, subscribeId);
        }

        public void removeByDestination(String sessionId, String destination) {
            Subscribe subscribe = map.get(sessionId);
            if(subscribe == null) return;

            subscribe.removeByDestination(destination);
        }

        public void removeBySubscribeId(String sessionId, String subscribeId) {
            Subscribe subscribe = map.get(sessionId);
            if(subscribe == null) return;

            subscribe.removeBySubscribeId(subscribeId);
        }

        public boolean contains(String sessionId) {
            return map.containsKey(sessionId);
        }

        public String getSubscribeId(String sessionId, String destination) {
            Subscribe subscribe = map.get(sessionId);
            if(subscribe == null) return null;
            return subscribe.getSubscribeId(destination);
        }

        public List<SubscribeKey> getSubscribeKeys(String destination) {
            List<SubscribeKey> subscribeKeys = new ArrayList<>(map.size());

            for(Map.Entry<String, Subscribe> entry : map.entrySet()) {
                String subscribeId = entry.getValue().getSubscribeId(destination);
                if(subscribeId == null) continue;
                subscribeKeys.add(new SubscribeKey(entry.getKey(), subscribeId));
            }

            return subscribeKeys;
        }


        public boolean isEmpty(String sessionId) {
            Subscribe subscribe = map.get(sessionId);
            if(subscribe == null) return true;
            return subscribe.getDestinationsMap().isEmpty();
        }

        public boolean isEmpty() {
            return map.isEmpty();
        }

    }


    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class Subscribe {
        private final Map<String, String> subscribeIdMap = new HashMap<>();
        private final Map<String, String> destinationsMap = new HashMap<>();

        public void put(String destination, String subscribeId) {
            subscribeIdMap.put(destination, subscribeId);
            destinationsMap.put(subscribeId, destination);
        }

        public void removeBySubscribeId(String subscribeId) {
            String destination = destinationsMap.get(subscribeId);
            if(destination == null) return;
            destinationsMap.remove(subscribeId);
            subscribeIdMap.remove(destination);
        }

        public void removeByDestination(String destination) {
            String subscribeId = subscribeIdMap.get(destination);
            if(subscribeId == null) return;
            destinationsMap.remove(subscribeId);
            subscribeIdMap.remove(destination);
        }

        public String getSubscribeId(String destination){
            return subscribeIdMap.get(destination);
        }

    }


    @Getter
    @ToString
    @RequiredArgsConstructor
    private static class SubscribeKey {
        private final String sessionId;
        private final String subscribeId;
    }




}