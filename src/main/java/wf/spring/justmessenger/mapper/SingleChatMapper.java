package wf.spring.justmessenger.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatRsDTO;
import wf.spring.justmessenger.entity.chat.SingleChat;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SingleChatMapper {

    private final MessageMapper messageMapper;

    public SingleChatRsDTO toSingleChatRsDTO(SingleChat singleChat) {
        SingleChatRsDTO singleChatRsDTO = new SingleChatRsDTO();

        singleChatRsDTO.setChatId(singleChat.getChatId());
        singleChatRsDTO.setFirstPersonId(singleChat.getFirstPersonId());
        singleChatRsDTO.setSecondPersonId(singleChat.getSecondPersonId());

        if(singleChat.getLastMessage() != null)
            singleChatRsDTO.setLastMessage(messageMapper.toMessageRsDTO(singleChat.getLastMessage()));

        return singleChatRsDTO;
    }

    public List<SingleChatRsDTO> toSingleChatRsDTOList(List<SingleChat> singleChats) {
        return singleChats.stream().map(this::toSingleChatRsDTO).toList();
    }

}
