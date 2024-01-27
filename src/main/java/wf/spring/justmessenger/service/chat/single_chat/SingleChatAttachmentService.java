package wf.spring.justmessenger.service.chat.single_chat;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentGetRqDTO;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentRsDTO;
import wf.spring.justmessenger.entity.person.Person;

import java.util.List;

public interface SingleChatAttachmentService {
    Resource getResource(AttachmentGetRqDTO attachmentGetRqDTO, Person principal);

    List<AttachmentRsDTO> upload(List<MultipartFile> files, ObjectId receiverId, Person principal);
}
