package wf.spring.justmessenger.controller.chat.group_chat;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.group_chat.edit.GroupChatNameChangeRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatEditService;

@RestController
@RequestMapping("/api/group_chat/edit")
@RequiredArgsConstructor
public class GroupChatEditController {

    private final GroupChatEditService groupChatEditService;


    @PostMapping("/name")
    public void changeName(@RequestBody @Valid GroupChatNameChangeRqDTO groupChatNameChangeRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatEditService.changeName(groupChatNameChangeRqDTO, principal);
    }





}
