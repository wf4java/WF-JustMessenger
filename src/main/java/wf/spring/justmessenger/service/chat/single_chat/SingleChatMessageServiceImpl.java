package wf.spring.justmessenger.service.chat.single_chat;



import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.dto.chat.message.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.entity.chat.Message;
import wf.spring.justmessenger.entity.chat.SingleChat;
import wf.spring.justmessenger.mapper.MessageMapper;
import wf.spring.justmessenger.model.MessengerMessagingTemplate;
import wf.spring.justmessenger.service.chat.AttachmentService;
import wf.spring.justmessenger.service.chat.MessageService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SingleChatMessageServiceImpl implements SingleChatMessageService {

    private final MessageMapper messageMapper;
    private final MongoTemplate mongoTemplate;
    private final MessageService messageService;
    private final AttachmentService attachmentService;
    private final SingleChatService singleChatService;
    private final SingleChatAccessService singleChatAccessService;
    private final MessengerMessagingTemplate messengerMessagingTemplate;


    @Override
    public void sendMessage(MessageSendRqDTO messageSendRqDTO, Person principal) {
        singleChatAccessService.sendMessageAccess(principal, messageSendRqDTO.getReceiverId());

        SingleChat singleChat = singleChatService.getSingleChatOrCreateIfNotExists(principal.getId(), messageSendRqDTO.getReceiverId());

        Message message = messageMapper.toMessage(messageSendRqDTO);
        message.setSenderId(principal.getId());
        message.setChatId(singleChat.getChatId());
        message.setAttachments(attachmentService.findAllByIdInAndChatId(messageSendRqDTO.getAttachments(), singleChat.getChatId()));
        message = messageService.save(message);
        updateLastMessage(singleChat.getChatId(), message);

        MessageRsDTO messageRsDTO = messageMapper.toMessageRsDTO(message);

        messengerMessagingTemplate.convertAndSendToUserMessage(principal.getDestinationUserName(), messageRsDTO);
        if(!singleChat.isFavoriteChat())
            messengerMessagingTemplate.convertAndSendToUserMessage(messageSendRqDTO.getReceiverId().toHexString(), messageRsDTO);
    }


    @Override
    public void updateLastMessage(ObjectId id, Message message) {
        Query query = new Query(Criteria.where("_id").is(id));
        Update update = new Update().set("lastMessage", message);
        mongoTemplate.updateFirst(query, update, SingleChat.class);
    }


    @Override
    public Message getMessage(MessageGetRqDTO messageGetRqDTO, Person principal) {
        singleChatAccessService.getMessageAccess(principal, messageGetRqDTO.getChatId(), messageGetRqDTO.getMessageId());

        return messageService.getById(messageGetRqDTO.getMessageId());
    }

    @Override
    public List<Message> getNewMessages(MessageGetNewRqDTO messageGetNewRqDTO, Person principal) {
        singleChatAccessService.getMessagesAccess(principal, messageGetNewRqDTO.getChatId());

        return messageService.getNewMessagesByChatIdAndOffsetMessageId(messageGetNewRqDTO.getChatId(),
                messageGetNewRqDTO.getOffsetMessageId(), messageGetNewRqDTO.getLimit());
    }

    @Override
    public List<Message> getOldMessages(MessageGetOldRqDTO messageGetOldRqDTO, Person principal) {
        singleChatAccessService.getMessagesAccess(principal, messageGetOldRqDTO.getChatId());

        return messageService.getOldMessagesByChatIdAndOffsetMessageId(messageGetOldRqDTO.getChatId(),
                messageGetOldRqDTO.getOffsetMessageId(), messageGetOldRqDTO.getLimit());
    }

}
