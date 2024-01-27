package wf.spring.justmessenger.dto.chat.message;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.bson.types.ObjectId;

import java.util.Date;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class MessageGetOldRqDTO {

    @NotNull
    private ObjectId chatId;


    private ObjectId offsetMessageId;

    @Min(1)
    @Max(100)
    private int limit = 25;


    public ObjectId getOffsetMessageId() {
        if(this.offsetMessageId == null)
            return new ObjectId(Integer.MAX_VALUE, Integer.MAX_VALUE);

        return offsetMessageId;
    }
}
