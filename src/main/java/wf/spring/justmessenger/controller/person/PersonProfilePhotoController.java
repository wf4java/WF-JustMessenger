package wf.spring.justmessenger.controller.person;


import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.ContentType;
import wf.spring.justmessenger.model.exception.basic.BadRequestException;
import wf.spring.justmessenger.service.person.PersonProfilePhotoService;

@RestController
@RequestMapping("/api/person/profile_photo")
@RequiredArgsConstructor
public class PersonProfilePhotoController {


    private final PersonProfilePhotoService personProfilePhotoService;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void setProfilePhoto(@RequestPart("file") MultipartFile file, @AuthenticationPrincipal Person principal) {
        if(ContentType.getByName(file.getContentType()) != ContentType.IMAGE)
            throw new BadRequestException("This is not image");

        personProfilePhotoService.setProfilePhoto(file, principal);
    }

    @GetMapping
    public Resource getProfilePhoto(@RequestParam("personId") String personId, @AuthenticationPrincipal Person principal) {
        if(!ObjectId.isValid(personId))
            throw new BadRequestException("Invalid person ID");

        return personProfilePhotoService.getProfilePhoto(new ObjectId(personId), principal);
    }

    @DeleteMapping
    public void deleteProfilePhoto(@AuthenticationPrincipal Person principal) {
        personProfilePhotoService.deleteProfilePhoto(principal);
    }


}
