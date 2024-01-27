package wf.spring.justmessenger.dto.chat.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import wf.spring.justmessenger.dto.chat.attachment.AttachmentRsDTO;
import wf.spring.justmessenger.entity.chat.Attachment;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageRsDTO {


    private ObjectId id;

    private String text;

    @NotNull
    private ObjectId senderId;

    @NotNull
    private ObjectId chatId;

    private ObjectId replyMessageId;

    private List<AttachmentRsDTO> attachments;

}
