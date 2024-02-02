package wf.spring.justmessenger.support.json;




import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.bson.types.ObjectId;

import java.io.IOException;



public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {

    @Override
    public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        try { return new ObjectId(jsonParser.getValueAsString()); }
        catch (IllegalArgumentException e) {
            throw InvalidFormatException.from(deserializationContext, "Cannot parse to ObjectId`, problem:" + e.getMessage());
        }
    }

}
