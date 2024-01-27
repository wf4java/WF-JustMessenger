package wf.spring.justmessenger.model.converter;


import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.elasticsearch.core.convert.ConversionException;
import org.springframework.data.elasticsearch.core.mapping.PropertyValueConverter;


public class ObjectIdPropertyValueConverter implements PropertyValueConverter {
    @NotNull
    @Override
    public Object write(@NotNull Object value) {
        if(value instanceof ObjectId objectId)
            return objectId.toHexString();

        throw new ConversionException("Value must be an instance of ObjectId");
    }

    @NotNull
    @Override
    public Object read(@NotNull Object value) {
        if(value instanceof ObjectId) return value;

        String s = value.toString();
        if (ObjectId.isValid(s))
            return new ObjectId(s);
        else
            throw new ConversionException("Invalid ObjectId value");
    }
}
