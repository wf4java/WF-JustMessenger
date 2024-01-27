package wf.spring.justmessenger.dto.chat.attachment;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class AttachmentGetRqDTO {

    @NotNull
    private ObjectId attachmentId;

    @NotNull
    private ObjectId chatId;


}
