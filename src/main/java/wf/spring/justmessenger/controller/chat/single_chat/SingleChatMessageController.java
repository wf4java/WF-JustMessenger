package wf.spring.justmessenger.controller.chat.single_chat;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.chat.message.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.mapper.MessageMapper;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.chat.single_chat.SingleChatMessageService;
import wf.spring.justmessenger.utils.validators.ValidationUtils;

import java.util.*;

@RestController
@RequiredArgsConstructor
@MessageMapping("/single_chat/message")
@RequestMapping("/api/single_chat/message")
public class SingleChatMessageController {

    private final SingleChatMessageService singleChatMessageService;
    private final ValidationUtils validationUtils;
    private final MessageMapper messageMapper;



    @MessageMapping
    public void send(@RequestBody MessageSendRqDTO messageSendRqDTO, @AuthenticationPrincipal Person principal) {
        validationUtils.validateAndThrow(messageSendRqDTO);

        singleChatMessageService.sendMessage(messageSendRqDTO, principal);
    }







    @GetMapping
    public MessageRsDTO get(@Valid MessageGetRqDTO messageGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTO(singleChatMessageService.getMessage(messageGetRqDTO, principal));
    }


    @GetMapping("/new")
    public List<MessageRsDTO> getNew(@Valid MessageGetNewRqDTO messageGetNewRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTOList(singleChatMessageService.getNewMessages(messageGetNewRqDTO, principal));
    }

    @GetMapping("/old")
    public List<MessageRsDTO> getOld(@Valid MessageGetOldRqDTO messageGetOldRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return messageMapper.toMessageRsDTOList(singleChatMessageService.getOldMessages(messageGetOldRqDTO, principal));
    }


}
