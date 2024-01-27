package wf.spring.justmessenger.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wf.spring.justmessenger.dto.chat.group_chat.GroupChatCreateRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.GroupChatRsDTO;
import wf.spring.justmessenger.entity.chat.GroupChat;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class GroupChatMapper {

    private final MessageMapper messageMapper;

    public GroupChat toGroupChat(GroupChatCreateRqDTO createGroupChatRqDTO) {
        GroupChat groupChat = new GroupChat();

        groupChat.setName(createGroupChatRqDTO.getName());

        return groupChat;
    }

    public GroupChatRsDTO toGroupChatRsDTO(GroupChat groupChat) {
        GroupChatRsDTO groupChatRsDTO = new GroupChatRsDTO();

        groupChatRsDTO.setChatId(groupChat.getChatId());
        groupChatRsDTO.setName(groupChat.getName());
        groupChatRsDTO.setOwnerId(groupChat.getOwnerId());
        groupChatRsDTO.setParticipantCount(groupChat.getParticipantCount());
        groupChatRsDTO.setHasProfilePhoto(groupChat.getProfilePhoto() != null);

        if(groupChat.getLastMessage() != null)
            groupChatRsDTO.setLastMessage(messageMapper.toMessageRsDTO(groupChat.getLastMessage()));


        return groupChatRsDTO;
    }

    public List<GroupChatRsDTO> toGroupChatRsDTOList(List<GroupChat> groupChats) {
        return groupChats.stream().map(this::toGroupChatRsDTO).toList();
    }

}
