package wf.spring.justmessenger.dto.person;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.entity.person.Person;
import wf.spring.justmessenger.model.Gender;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PersonRsDTO {

    private ObjectId id;

    private String username;

    private Gender gender;

    private Person.Status status;

    private boolean hasProfilePhoto;

}
