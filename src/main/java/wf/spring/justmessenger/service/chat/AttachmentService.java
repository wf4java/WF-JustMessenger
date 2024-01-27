package wf.spring.justmessenger.service.chat;

import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.entity.chat.Attachment;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface AttachmentService {
    Resource getResourceById(ObjectId id);

    List<Attachment> findAllByIdInAndChatId(Collection<ObjectId> ids, ObjectId chatId);

    List<Attachment> upload(List<MultipartFile> files, ObjectId chatId);

    Attachment upload(MultipartFile file, ObjectId chatId);

    Optional<Attachment> findById(ObjectId id);

    Attachment getById(ObjectId id);

    boolean existsByIdAndChatId(ObjectId id, ObjectId chatId);
}
