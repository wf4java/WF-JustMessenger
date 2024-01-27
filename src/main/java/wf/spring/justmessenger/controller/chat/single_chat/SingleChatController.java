package wf.spring.justmessenger.controller.chat.single_chat;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetNewRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatGetRqDTO;
import wf.spring.justmessenger.dto.chat.single_chat.SingleChatRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.single_chat.SingleChatService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/single_chat")
public class SingleChatController {

    private final SingleChatService singleChatService;


    @GetMapping("/new")
    public List<SingleChatRsDTO> getNew(@Valid SingleChatGetNewRqDTO singleChatGetNewRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return singleChatService.getNewSingleChats(singleChatGetNewRqDTO, principal);
    }

    @GetMapping("/all")
    public List<SingleChatRsDTO> getAll(@AuthenticationPrincipal Person principal) {
        return singleChatService.getAllSingleChats(principal);
    }

    @GetMapping
    public SingleChatRsDTO get(@Valid SingleChatGetRqDTO singleChatGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return singleChatService.getById(singleChatGetRqDTO, principal);
    }


}
