package wf.spring.justmessenger.service.chat.group_chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentGetRqDTO;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.AttachmentMapper;
import wf.spring.justmessenger.service.chat.AttachmentService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GroupChatAttachmentServiceImpl implements GroupChatAttachmentService {


    private final AttachmentMapper attachmentMapper;
    private final AttachmentService attachmentService;
    private final GroupChatAccessService groupChatAccessService;


    @Override
    public Resource getResource(AttachmentGetRqDTO attachmentGetRqDTO, Person principal) {
        groupChatAccessService.getAttachmentAccess(principal, attachmentGetRqDTO.getAttachmentId(), attachmentGetRqDTO.getChatId());

        return attachmentService.getResourceById(attachmentGetRqDTO.getAttachmentId());
    }

    @Override
    public List<AttachmentRsDTO> upload(List<MultipartFile> files, ObjectId chatId, Person principal) {
        groupChatAccessService.sendAttachmentAccess(principal, chatId);

        return attachmentMapper.toAttachmentRsDTOList(attachmentService.upload(files, chatId));
    }


}
