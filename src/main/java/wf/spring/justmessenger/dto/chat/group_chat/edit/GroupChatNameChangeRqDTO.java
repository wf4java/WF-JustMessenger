package wf.spring.justmessenger.dto.chat.group_chat.edit;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatNameChangeRqDTO {

    @NotNull
    private ObjectId chatId;

    @NotNull
    @Length(max = 64)
    private String name;


}
