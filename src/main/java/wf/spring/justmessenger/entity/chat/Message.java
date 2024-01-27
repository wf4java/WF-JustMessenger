package wf.spring.justmessenger.entity.chat;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.*;
import wf.spring.justmessenger.model.ContentType;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Document(collection = "message")
//@CompoundIndexes(
        //@CompoundIndex(name = "chatId_id_index", def = "{'chatId': 1, 'id': 1}", unique = true)
//)
public class Message {

    @Id
    private ObjectId id;

    private String text;

    @NotNull
    private ObjectId senderId;

    @NotNull
    @Indexed
    private ObjectId chatId;

    private ObjectId replyMessageId;


    private List<Attachment> attachments;






}
