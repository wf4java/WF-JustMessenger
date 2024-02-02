package wf.spring.justmessenger.dto.chat.group_chat;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.web.bind.annotation.PathVariable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatDeleteRqDTO {


    @NotNull
    private ObjectId chatId;

}
