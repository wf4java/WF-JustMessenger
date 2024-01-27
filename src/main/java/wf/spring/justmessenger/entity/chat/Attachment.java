package wf.spring.justmessenger.entity.chat;


import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import wf.spring.justmessenger.model.ContentType;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "attachment")
@CompoundIndexes(
        @CompoundIndex(name= "chatId_contentType", def = "{'chatId': 1, 'contentType': 1, 'id': 1}")
)
public class Attachment {

    @Id
    private ObjectId id;

    @NotNull
    @Indexed
    private ObjectId chatId;

    @NotNull
    @Indexed
    private ContentType contentType;

    @NotNull
    private String name;

    @NotNull
    private UUID uuid;


}
