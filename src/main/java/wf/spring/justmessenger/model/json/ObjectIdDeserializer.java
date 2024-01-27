package wf.spring.justmessenger.model.json;




import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.bson.types.ObjectId;

import java.io.IOException;



public class ObjectIdDeserializer extends JsonDeserializer<ObjectId> {


    @Override
    public ObjectId deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String objectIdString = jsonParser.getValueAsString();

        if(ObjectId.isValid(objectIdString))
            return new ObjectId(objectIdString);

        return null;
    }
}
