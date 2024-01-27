package wf.spring.justmessenger.controller.chat.group_chat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentGetRqDTO;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatAttachmentService;

import java.util.List;

@RestController
@RequestMapping("/api/group_chat/attachment")
@RequiredArgsConstructor
public class GroupChatAttachmentController {

    private final GroupChatAttachmentService groupChatAttachmentService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AttachmentRsDTO> upload(@RequestPart("files") MultipartFile[] files, @RequestPart("chatId") String chatId, @AuthenticationPrincipal Person principal) {
        if(!ObjectId.isValid(chatId))
            throw new BadRequestException("Invalid chat ID");

        if(files.length > 5)
            throw new BadRequestException("Max files count is 5");

        return groupChatAttachmentService.upload(List.of(files), new ObjectId(chatId), principal);
    }

    @GetMapping
    public Resource get(@Valid AttachmentGetRqDTO attachmentGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatAttachmentService.getResource(attachmentGetRqDTO, principal);
    }



}
