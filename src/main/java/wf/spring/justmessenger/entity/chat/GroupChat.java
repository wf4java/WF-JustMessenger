package wf.spring.justmessenger.entity.chat;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "group_chat")
public class GroupChat {

    @Id
    private ObjectId chatId;

    @NotNull
    private String name;

    @NotNull
    private ObjectId ownerId;

    @NotNull
    private Integer participantCount;

    @DBRef
    @Indexed(unique = true, sparse = true)
    private Message lastMessage;

    private UUID profilePhoto;

}
