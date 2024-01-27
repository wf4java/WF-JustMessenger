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
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoDeleteRqDTO;
import wf.spring.justmessenger.dto.chat.group_chat.profile_photo.GroupChatProfilePhotoGetRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.ContentType;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatProfilePhotoService;

@RestController
@RequestMapping("/api/group_chat/profile_photo")
@RequiredArgsConstructor
public class GroupProfilePhotoController {

    private final GroupChatProfilePhotoService groupChatProfilePhotoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void setProfilePhoto(@RequestPart("file") MultipartFile file, @RequestPart("chatId") String chatId, @AuthenticationPrincipal Person principal) {
        if(!ObjectId.isValid(chatId))
            throw new BadRequestException("Invalid chat ID");

        if(ContentType.getByName(file.getContentType()) != ContentType.IMAGE)
            throw new BadRequestException("This is not image");

        groupChatProfilePhotoService.setProfilePhoto(new ObjectId(chatId), file, principal);
    }

    @GetMapping
    public Resource getProfilePhoto(@Valid GroupChatProfilePhotoGetRqDTO groupChatProfilePhotoGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatProfilePhotoService.getProfilePhoto(groupChatProfilePhotoGetRqDTO, principal);
    }

    @DeleteMapping
    public void deleteProfilePhoto(@RequestBody @Valid GroupChatProfilePhotoDeleteRqDTO groupChatProfilePhotoDeleteRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatProfilePhotoService.deleteProfilePhoto(groupChatProfilePhotoDeleteRqDTO, principal);
    }

}
