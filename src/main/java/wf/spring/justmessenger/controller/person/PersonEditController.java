package wf.spring.justmessenger.controller.person;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.person.edit.PersonPasswordChangeRqDTO;
import wf.spring.justmessenger.dto.person.edit.PersonUsernameChangeRqDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.person.PersonEditService;

@RestController
@RequestMapping("/api/person/edit")
@RequiredArgsConstructor
public class PersonEditController {

    private final PersonEditService personEditService;



    @PostMapping("/username")
    public void changeUsername(@RequestBody @Valid PersonUsernameChangeRqDTO personUsernameChangeRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal){
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        personEditService.changeUsername(personUsernameChangeRqDTO, principal);
    }

    @PostMapping("/password")
    public void changePassword(@RequestBody @Valid PersonPasswordChangeRqDTO personPasswordChangeRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        personEditService.changePassword(personPasswordChangeRqDTO, principal);
    }


}
