package wf.spring.justmessenger.mapper;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import wf.spring.justmessenger.dto.chat.group_chat.participant.GroupChatParticipantRsDTO;
import wf.spring.justmessenger.entity.chat.GroupChatParticipant;

import java.util.List;


@Component
@RequiredArgsConstructor
public class GroupChatParticipantMapper {

    public GroupChatParticipantRsDTO toGroupChatParticipantRsDTO(GroupChatParticipant groupChatParticipant) {
        GroupChatParticipantRsDTO groupChatParticipantRsDTO = new GroupChatParticipantRsDTO();

        groupChatParticipantRsDTO.setChatId(groupChatParticipant.getGroupId());
        groupChatParticipantRsDTO.setPersonId(groupChatParticipant.getPersonId());

        return groupChatParticipantRsDTO;
    }

    public List<GroupChatParticipantRsDTO> toGroupChatParticipantRsDTOList(List<GroupChatParticipant> groupChatParticipants) {
        return groupChatParticipants.stream().map(this::toGroupChatParticipantRsDTO).toList();
    }

}
