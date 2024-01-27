package wf.spring.justmessenger.entity.chat;

import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "single_chat")
@CompoundIndexes({
        @CompoundIndex(name = "message_persons_index", def = "{'lastMessage': 1, 'firstPersonId': 1, 'secondPersonId': 1}"),
        @CompoundIndex(name = "persons_index", def = "{'firstPersonId': 1, 'secondPersonId': 1}")
})
public class SingleChat {

    @Id
    private ObjectId chatId;

    @NotNull
    private ObjectId firstPersonId;

    @NotNull
    private ObjectId secondPersonId;


    @DBRef
    @Indexed(unique = true, sparse = true)
    private Message lastMessage;



    public boolean isFavoriteChat() {
        return firstPersonId.equals(secondPersonId);
    }

    public boolean isThisUserChat(ObjectId personId) {
        return (firstPersonId.equals(personId) || secondPersonId.equals(personId));
    }

}
