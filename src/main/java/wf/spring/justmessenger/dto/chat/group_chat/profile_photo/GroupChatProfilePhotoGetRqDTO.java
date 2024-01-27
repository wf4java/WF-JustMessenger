package wf.spring.justmessenger.dto.chat.group_chat.profile_photo;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatProfilePhotoGetRqDTO {

    @NotNull
    private ObjectId chatId;

}
