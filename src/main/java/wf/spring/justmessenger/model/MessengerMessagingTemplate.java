package wf.spring.justmessenger.model;

import lombok.*;
import org.springframework.lang.Nullable;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.core.MessagePostProcessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.support.MessageHeaderInitializer;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
public class MessengerMessagingTemplate {

    private final SimpMessagingTemplate simpMessagingTemplate;




    //-----------------------------------------------------------------------------------------------------------------------------------------


    public void convertAndSendToGroupChatMessage(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.MESSAGE, object));
    }

    public void convertAndSendToGroupChatParticipantLeave(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.PARTICIPANT_LEAVE, object));
    }

    public void convertAndSendToGroupChatParticipantAdd(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.PARTICIPANT_ADD, object));
    }

    public void convertAndSendToGroupChatParticipantRemove(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.PARTICIPANT_REMOVE, object));
    }

    public void convertAndSendToGroupChatNameUpdate(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.NAME_UPDATE, object));
    }

    public void convertAndSendToGroupChatProfilePhotoUpdate(String id) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.PROFILE_PHOTO_UPDATE));
    }

    public void convertAndSendToGroupChatProfilePhotoDelete(String id) {
        simpMessagingTemplate.convertAndSend("/topic/group_chat/" + id, new SocketRsModel(GroupChatRsType.PROFILE_PHOTO_DELETE));
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------

    public void convertAndSendToUserError(String id, Object object) {
        simpMessagingTemplate.convertAndSendToUser(id,"/queue/error", object);
    }

    public void convertAndSendToUserReply(String id, Object object) {
        simpMessagingTemplate.convertAndSendToUser(id,"/queue/reply", object);
    }


    public void convertAndSendToUserMessage(String id, Object object) {
        simpMessagingTemplate.convertAndSendToUser(id,"/queue/message", object);
    }

    public void convertAndSendToUserNewGroupChat(String id, Object object) {
        simpMessagingTemplate.convertAndSendToUser(id,"/queue", new SocketRsModel(PersonRsType.NEW_GROUP_CHAT, object));
    }


    public void convertAndSendToUserRemoveGroupChat(String id, Object object) {
        simpMessagingTemplate.convertAndSendToUser(id,"/queue", new SocketRsModel(PersonRsType.REMOVE_GROUP_CHAT, object));
    }

    public void convertAndSendToTopicOfUserStatus(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/user/" + id + "/update", new SocketRsModel(PersonUpdateType.STATUS, object));
    }

    public void convertAndSendToTopicOfUserUsername(String id, Object object) {
        simpMessagingTemplate.convertAndSend("/topic/user/" + id + "/update", new SocketRsModel(PersonUpdateType.USERNAME, object));
    }

    public void convertAndSendToTopicOfUserProfilePhotoUpdate(String id) {
        simpMessagingTemplate.convertAndSend("/topic/user/" + id + "/update", new SocketRsModel(PersonUpdateType.PROFILE_PHOTO_UPDATE));
    }

    public void convertAndSendToTopicOfUserProfilePhotoDelete(String id) {
        simpMessagingTemplate.convertAndSend("/topic/user/" + id + "/update", new SocketRsModel(PersonUpdateType.PROFILE_PHOTO_DELETE));
    }

    //-----------------------------------------------------------------------------------------------------------------------------------------


    @Getter
    @AllArgsConstructor
    @ToString
    public static class SocketRsModel {

        private final Object t;

        private final Object o;

        public SocketRsModel(Object t) {
            this.t = t;
            this.o = null;
        }
    }



    public static enum PersonRsType {
        NEW_GROUP_CHAT,
        REMOVE_GROUP_CHAT,
    }

    public static enum GroupChatRsType {
        MESSAGE,
        PARTICIPANT_ADD,
        PARTICIPANT_REMOVE,
        PARTICIPANT_LEAVE,
        NAME_UPDATE,
        PROFILE_PHOTO_UPDATE,
        PROFILE_PHOTO_DELETE;
    }

    public static enum PersonUpdateType {
        STATUS,
        USERNAME,
        PROFILE_PHOTO_UPDATE,
        PROFILE_PHOTO_DELETE
    }




    public MessageChannel getMessageChannel() {
        return simpMessagingTemplate.getMessageChannel();
    }

    public void setUserDestinationPrefix(String prefix) {
        simpMessagingTemplate.setUserDestinationPrefix(prefix);
    }

    public String getUserDestinationPrefix() {
        return simpMessagingTemplate.getUserDestinationPrefix();
    }

    public void setSendTimeout(long sendTimeout) {
        simpMessagingTemplate.setSendTimeout(sendTimeout);
    }

    public long getSendTimeout() {
        return simpMessagingTemplate.getSendTimeout();
    }

    public void setHeaderInitializer(MessageHeaderInitializer headerInitializer) {
        simpMessagingTemplate.setHeaderInitializer(headerInitializer);
    }

    @Nullable
    public MessageHeaderInitializer getHeaderInitializer() {
        return simpMessagingTemplate.getHeaderInitializer();
    }

    public void send(Message<?> message) {
        simpMessagingTemplate.send(message);
    }

    public void convertAndSendToUser(String user, String destination, Object payload) throws MessagingException {
        simpMessagingTemplate.convertAndSendToUser(user, destination, payload);
    }

    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers) throws MessagingException {
        simpMessagingTemplate.convertAndSendToUser(user, destination, payload, headers);
    }

    public void convertAndSendToUser(String user, String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {
        simpMessagingTemplate.convertAndSendToUser(user, destination, payload, postProcessor);
    }

    public void convertAndSendToUser(String user, String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException {
        simpMessagingTemplate.convertAndSendToUser(user, destination, payload, headers, postProcessor);
    }

    public void setDefaultDestination(String defaultDestination) {
        simpMessagingTemplate.setDefaultDestination(defaultDestination);
    }

    @Nullable
    public String getDefaultDestination() {
        return simpMessagingTemplate.getDefaultDestination();
    }

    public void setMessageConverter(MessageConverter messageConverter) {
        simpMessagingTemplate.setMessageConverter(messageConverter);
    }

    public MessageConverter getMessageConverter() {
        return simpMessagingTemplate.getMessageConverter();
    }

    public void send(String destination, Message<?> message) {
        simpMessagingTemplate.send(destination, message);
    }

    public void convertAndSend(Object payload) throws MessagingException {
        simpMessagingTemplate.convertAndSend(payload);
    }

    public void convertAndSend(String destination, Object payload) throws MessagingException {
        simpMessagingTemplate.convertAndSend(destination, payload);
    }

    public void convertAndSend(String destination, Object payload, Map<String, Object> headers) throws MessagingException {
        simpMessagingTemplate.convertAndSend(destination, payload, headers);
    }

    public void convertAndSend(Object payload, MessagePostProcessor postProcessor) throws MessagingException {
        simpMessagingTemplate.convertAndSend(payload, postProcessor);
    }

    public void convertAndSend(String destination, Object payload, MessagePostProcessor postProcessor) throws MessagingException {
        simpMessagingTemplate.convertAndSend(destination, payload, postProcessor);
    }

    public void convertAndSend(String destination, Object payload, Map<String, Object> headers, MessagePostProcessor postProcessor) throws MessagingException {
        simpMessagingTemplate.convertAndSend(destination, payload, headers, postProcessor);
    }
}
