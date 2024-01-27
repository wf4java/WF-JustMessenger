package wf.spring.justmessenger.dto.chat.group_chat;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.bson.types.ObjectId;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GroupChatCreateRqDTO {

    @NotNull
    @Length(max = 64)
    private String name;

    @Size(max = 20)
    private List<ObjectId> members;

}
