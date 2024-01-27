package wf.spring.justmessenger.controller.chat.group_chat;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatDeleteRqDTO {

    @NotNull
    private ObjectId chatId;

}
