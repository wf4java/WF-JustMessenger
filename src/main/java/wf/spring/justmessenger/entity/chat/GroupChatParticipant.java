package wf.spring.justmessenger.entity.chat;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "group_participant")
public class GroupChatParticipant {

    @Id
    private ObjectId id;

    @NotNull
    private ObjectId groupId;

    @NotNull
    @Indexed
    private ObjectId personId;

    //Other parameters

}
