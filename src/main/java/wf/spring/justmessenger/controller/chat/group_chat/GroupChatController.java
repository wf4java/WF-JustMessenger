package wf.spring.justmessenger.controller.chat.group_chat;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.group_chat.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group_chat")
public class GroupChatController {

    private final GroupChatService groupChatService;

    @PostMapping("/create")
    public void create(@RequestBody @Valid GroupChatCreateRqDTO groupChatCreateRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatService.create(groupChatCreateRqDTO, principal);
    }

    @PostMapping("/leave")
    public void leave(@RequestBody @Valid GroupChatLeaveRqDTO groupChatLeaveRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatService.leave(groupChatLeaveRqDTO, principal);
    }

    @DeleteMapping
    public void delete(@RequestBody @Valid GroupChatDeleteRqDTO groupChatDeleteRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        groupChatService.delete(groupChatDeleteRqDTO, principal);
    }




    @GetMapping("/new")
    public List<GroupChatRsDTO> getNew(@Valid GroupChatGetNewRqDTO groupChatGetNewRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatService.getNewGroupChats(groupChatGetNewRqDTO, principal);
    }

    @GetMapping("/all")
    public List<GroupChatRsDTO> getAll(@AuthenticationPrincipal Person principal) {
        return groupChatService.getAllGroupChats(principal);
    }

    @GetMapping
    public GroupChatRsDTO get(@Valid GroupChatGetRqDTO groupChatGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return groupChatService.getById(groupChatGetRqDTO, principal);
    }


}
