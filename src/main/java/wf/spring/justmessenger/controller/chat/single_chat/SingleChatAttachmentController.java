package wf.spring.justmessenger.controller.chat.single_chat;


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
import wf.spring.justmessenger.service.chat.single_chat.SingleChatAttachmentService;

import java.util.List;

@RestController
@RequestMapping("/api/single_chat/attachment")
@RequiredArgsConstructor
public class SingleChatAttachmentController {

    private final SingleChatAttachmentService singleChatAttachmentService;



    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<AttachmentRsDTO> upload(@RequestPart("files") MultipartFile[] files, @RequestPart("receiverId") String receiverId, @AuthenticationPrincipal Person principal) {
        if(!ObjectId.isValid(receiverId))
            throw new BadRequestException("Invalid receiverId ID");

        if(files.length > 5)
            throw new BadRequestException("Max files count is 5");

        return singleChatAttachmentService.upload(List.of(files), new ObjectId(receiverId), principal);
    }

    @GetMapping
    public Resource get(@Valid AttachmentGetRqDTO attachmentGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return singleChatAttachmentService.getResource(attachmentGetRqDTO, principal);
    }



}
