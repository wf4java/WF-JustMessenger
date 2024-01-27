package wf.spring.justmessenger.controller.person;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import wf.spring.justmessenger.dto.person.*;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.person.PersonService;

import java.util.List;


@RestController
@RequestMapping("/api/person")
@RequiredArgsConstructor
public class PersonController {

    private final PersonService personService;




    @GetMapping
    public PersonRsDTO get(@Valid PersonGetRqDTO personGetRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);
        return personService.getPersonById(personGetRqDTO.getPersonId(), principal);
    }

    @GetMapping("/all")
    public List<PersonRsDTO> getAll(@Valid PersonGetAllRqDTO personGetAllRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return personService.getPersonByIds(personGetAllRqDTO.getPersonIds(), principal);
    }

    @GetMapping("/me")
    public PersonRsDTO getMe(@AuthenticationPrincipal Person principal) {
        return personService.getPersonById(principal.getId(), principal);
    }

    @GetMapping("/search")
    private List<PersonRsDTO> search(@Valid PersonSearchRqDTO personSearchRqDTO, BindingResult bindingResult, @AuthenticationPrincipal Person principal) {
        if(bindingResult.hasErrors())
            throw new BadRequestException(bindingResult);

        return personService.search(personSearchRqDTO, principal);
    }

}
