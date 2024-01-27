package wf.spring.justmessenger.controller.person;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.person.status.PersonStatusGetRqDTO;
import wf.spring.justmessenger.dto.person.status.PersonStatusRqDTO;
import wf.spring.justmessenger.dto.person.status.PersonStatusRsDTO;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.person.PersonStatusService;

@RestController
@RequestMapping("/api/person/status")
@RequiredArgsConstructor
public class PersonStatusController {

    private final PersonStatusService personStatusService;


    @PostMapping
    public void setSelectedStatus(@RequestBody @Valid PersonStatusRqDTO personStatusRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        personStatusService.setStatus(principal, personStatusRqDTO.getStatus());
    }

    @GetMapping
    public PersonStatusRsDTO get(@Valid PersonStatusGetRqDTO personStatusGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return personStatusService.get(personStatusGetRqDTO, principal);
    }

    @GetMapping("/selected")
    private PersonStatusRsDTO getMySelectedStatus(@AuthenticationPrincipal Person principal) {
        return new PersonStatusRsDTO(principal.getId(), principal.getSelectedStatus());
    }

    @GetMapping("/current")
    private PersonStatusRsDTO getMyCurrenStatus(@AuthenticationPrincipal Person principal) {
        return new PersonStatusRsDTO(principal.getId(), personStatusService.getStatus(principal));
    }



}
