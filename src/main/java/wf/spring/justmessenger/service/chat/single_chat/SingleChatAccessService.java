package wf.spring.justmessenger.service.chat.single_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import wf.spring.justmessenger.entity.chat.SingleChat;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.AccessException;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.service.chat.AttachmentService;
import wf.spring.justmessenger.service.chat.MessageService;
import wf.spring.justmessenger.service.person.PersonService;

@Service
@RequiredArgsConstructor
public class SingleChatAccessService {

    @Lazy
    @Autowired
    private SingleChatService singleChatService;
    private final PersonService personService;
    private final MessageService messageService;
    private final AttachmentService attachmentService;



    public void sendMessageAccess(Person principal, ObjectId receiverId) {
        if(!personService.existsById(receiverId))
            throw new NotFoundException("Person with this id was not found");
        // Check for blocking
    }


    public void sendAttachmentAccess(Person principal, ObjectId receiverId) {
        sendMessageAccess(principal, receiverId);
    }


    public void getAttachmentAccess(Person principal, ObjectId attachmentId, ObjectId chatId) {
        if(!attachmentService.existsByIdAndChatId(attachmentId, chatId))
            throw new AccessException("You do not have access to this attachment");

        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }


    public void getMessagesAccess(Person principal, ObjectId chatId) {
        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }


    public void getMessageAccess(Person principal, ObjectId chatId, ObjectId messageId) {
        if(!messageService.existsByIdAndChatId(messageId, chatId))
            throw new AccessException("You do not have access to this message");

        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }


    public void getSingleChatAccess(Person principal, ObjectId chatId) {
        if(!singleChatService.existsByChatIdAndFirstPersonIdOrSecondPersonId(chatId, principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }


}
