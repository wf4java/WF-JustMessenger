package wf.spring.justmessenger.service.chat.single_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
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
public class SingleChatAccessServiceImpl implements SingleChatAccessService {


    private final SingleChatService singleChatService;
    private final MessageService messageService;
    private final AttachmentService attachmentService;
    private final PersonService personService;


    @Override
    public void sendMessageAccess(Person principal, ObjectId receiverId) {
        if(!personService.existsById(receiverId))
            throw new NotFoundException("Person with this id was not found");
        // Check for blocking
    }

    @Override
    public void sendAttachmentAccess(Person principal, ObjectId receiverId) {
        sendMessageAccess(principal, receiverId);
    }

    @Override
    public void getAttachmentAccess(Person principal, ObjectId attachmentId, ObjectId chatId) {
        if(!attachmentService.existsByIdAndChatId(attachmentId, chatId))
            throw new AccessException("You do not have access to this attachment");

        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }

    @Override
    public void getMessagesAccess(Person principal, ObjectId chatId) {
        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }

    @Override
    public void getMessageAccess(Person principal, ObjectId chatId, ObjectId messageId) {
        if(!messageService.existsByIdAndChatId(messageId, chatId))
            throw new AccessException("You do not have access to this message");

        SingleChat singleChat = singleChatService.getById(chatId);

        if(!singleChat.isThisUserChat(principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }

    @Override
    public void getSingleChatAccess(Person principal, ObjectId chatId) {
        if(!singleChatService.existsByChatIdAndFirstPersonIdOrSecondPersonId(chatId, principal.getId()))
            throw new AccessException("You do not have access to this chat");
    }


}
