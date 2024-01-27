package wf.spring.justmessenger.dto.person.profile_photo;

import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PersonProfilePhotoDeleteRsDTO {

    private ObjectId personId;

}
