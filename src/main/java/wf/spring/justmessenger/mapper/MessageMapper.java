package wf.spring.justmessenger.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import wf.spring.justmessenger.dto.chat.message.MessageRsDTO;
import wf.spring.justmessenger.dto.chat.message.MessageSendRqDTO;
import wf.spring.justmessenger.entity.chat.Message;
import wf.utils.java.data.list.ListUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class MessageMapper {

    private final AttachmentMapper attachmentMapper;


    public Message toMessage(MessageSendRqDTO sendMessageRqDTO) {
        Message message = new Message();

        message.setText(sendMessageRqDTO.getText());
        message.setReplyMessageId(sendMessageRqDTO.getReplyMessageId());


        return message;
    }

    public MessageRsDTO toMessageRsDTO(Message message) {
        MessageRsDTO messageRsDTO = new MessageRsDTO();

        messageRsDTO.setId(message.getId());
        messageRsDTO.setText(message.getText());
        messageRsDTO.setSenderId(message.getSenderId());
        messageRsDTO.setChatId(message.getChatId());
        messageRsDTO.setReplyMessageId(message.getReplyMessageId());

        if(!CollectionUtils.isEmpty(message.getAttachments()))
            messageRsDTO.setAttachments(attachmentMapper.toAttachmentRsDTOList(message.getAttachments()));

        return messageRsDTO;
    }

    public List<MessageRsDTO> toMessageRsDTOList(List<Message> messages) {
        return messages.stream().map(this::toMessageRsDTO).toList();
    }



}
