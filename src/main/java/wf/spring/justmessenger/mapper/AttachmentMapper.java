package wf.spring.justmessenger.mapper;


import org.springframework.stereotype.Component;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentRsDTO;
import wf.spring.justmessenger.entity.chat.Attachment;

import java.util.List;

@Component
public class AttachmentMapper {


    public AttachmentRsDTO toAttachmentRsDTO(Attachment attachment) {
        AttachmentRsDTO attachmentRsDTO = new AttachmentRsDTO();

        attachmentRsDTO.setId(attachment.getId());
        attachmentRsDTO.setContentType(attachment.getContentType());
        attachmentRsDTO.setName(attachment.getName());

        return attachmentRsDTO;
    }

    public List<AttachmentRsDTO> toAttachmentRsDTOList(List<Attachment> attachments) {
        return attachments.stream().map(this::toAttachmentRsDTO).toList();
    }


}
