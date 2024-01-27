package wf.spring.justmessenger.controller.chat.group_chat;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.group_chat.participant.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatParticipantService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group_chat/participant")
public class GroupChatParticipantController {

    private final GroupChatParticipantService groupChatParticipantService;

    @PostMapping("/add")
    public void add(@RequestBody @Valid GroupChatParticipantAddRqDTO groupChatParticipantAddRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatParticipantService.add(groupChatParticipantAddRqDTO, principal);
    }

    @PostMapping("/remove")
    public void remove(@RequestBody @Valid GroupChatParticipantRemoveRqDTO groupChatParticipantRemoveRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatParticipantService.remove(groupChatParticipantRemoveRqDTO, principal);
    }

    @GetMapping
    public GroupChatParticipantRsDTO get(GroupChatParticipantGetRqDTO groupChatParticipantGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatParticipantService.get(groupChatParticipantGetRqDTO, principal);
    }


    @GetMapping("/all")
    public List<GroupChatParticipantRsDTO> getAll(GroupChatParticipantGetAllRqDTO groupChatParticipantGetAllRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatParticipantService.getAll(groupChatParticipantGetAllRqDTO, principal);
    }



}
