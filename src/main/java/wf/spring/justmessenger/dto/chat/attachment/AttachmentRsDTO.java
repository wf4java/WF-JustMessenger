package wf.spring.justmessenger.dto.chat.attachment;


import lombok.*;
import org.bson.types.ObjectId;
import wf.spring.justmessenger.model.ContentType;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttachmentRsDTO {

    private ObjectId id;

    private ContentType contentType;

    private String name;

}
