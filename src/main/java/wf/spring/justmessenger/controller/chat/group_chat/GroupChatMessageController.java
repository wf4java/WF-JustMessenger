package wf.spring.justmessenger.controller.chat.group_chat;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.message.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.MessageMapper;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.group_chat.GroupChatMessageService;
import wf.spring.justmessenger.utils.validators.ValidationUtils;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/group_chat/message")
@MessageMapping("/group_chat/message")
public class GroupChatMessageController {

    private final MessageMapper messageMapper;
    private final ValidationUtils validationUtils;
    private final GroupChatMessageService groupChatMessageService;

    @MessageMapping
    public void send(@RequestBody MessageSendRqDTO messageSendRqDTO, @AuthenticationPrincipal Person principal) {
        validationUtils.validateAndThrow(messageSendRqDTO);

        groupChatMessageService.sendMessage(messageSendRqDTO, principal);
    }


    @GetMapping
    public MessageRsDTO get(@Valid MessageGetRqDTO messageGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTO(groupChatMessageService.getMessage(messageGetRqDTO, principal));
    }


    @GetMapping("/new")
    public List<MessageRsDTO> getNew(@Valid MessageGetNewRqDTO messageGetNewRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTOList(groupChatMessageService.getNewMessages(messageGetNewRqDTO, principal));
    }

    @GetMapping("/old")
    public List<MessageRsDTO> getOld(@Valid MessageGetOldRqDTO messageGetOldRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTOList(groupChatMessageService.getOldMessages(messageGetOldRqDTO, principal));
    }


}
