package wf.spring.justmessenger.service.chat;

import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.entity.chat.Attachment;
import wf.spring.justmessenger.model.ContentType;
import wf.spring.justmessenger.model.exception.NotFoundException;
import wf.spring.justmessenger.model.exception.StorageException;
import wf.spring.justmessenger.repository.chat.AttachmentRepository;
import wf.spring.justmessenger.service.StorageService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AttachmentServiceImpl implements AttachmentService {


    private final AttachmentRepository attachmentRepository;
    private final StorageService storageService;



    @Override
    public Resource getResourceById(ObjectId id) {
        Attachment attachment = getById(id);

        return new InputStreamResource(storageService.get(attachment.getUuid()));
    }

    @Override
    public List<Attachment> findAllByIdInAndChatId(Collection<ObjectId> ids, ObjectId chatId) {
        return attachmentRepository.findAllByIdInAndChatId(ids, chatId);
    }

    @Override
    public List<Attachment> upload(List<MultipartFile> files, ObjectId chatId) {
        List<Attachment> attachments = new ArrayList<>(files.size());
        for (int i = 0; i < files.size(); i++) {
            try {
                attachments.add(upload(files.get(i), chatId));
            } catch (StorageException e) {
                if (i == files.size() - 1 && attachments.isEmpty())
                    throw e;
            }
        }
        return attachments;
    }

    @Override
    public Attachment upload(MultipartFile file, ObjectId chatId) {
        Attachment attachment = new Attachment();

        attachment.setChatId(chatId);
        attachment.setName(file.getOriginalFilename());
        attachment.setContentType(ContentType.getByName(file.getContentType()));

        attachment.setUuid(storageService.save(file));

        return attachmentRepository.save(attachment);
    }



    @Override
    public Optional<Attachment> findById(ObjectId id) {
        return attachmentRepository.findById(id);
    }

    @Override
    public Attachment getById(ObjectId id) {
        return findById(id)
                .orElseThrow(() -> new NotFoundException("Attachment with this id was not found"));
    }


    @Override
    public boolean existsByIdAndChatId(ObjectId id, ObjectId chatId) {
        return attachmentRepository.existsByIdAndChatId(id, chatId);
    }

}
