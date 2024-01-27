package wf.spring.justmessenger.entity.person;


import lombok.*;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.*;
import wf.spring.justmessenger.model.converter.ObjectIdPropertyValueConverter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(indexName = "person")
@Setting(settingPath = "data/es-config/es-person-setting.json")
public class EsPerson {


    @Id
    @ValueConverter(ObjectIdPropertyValueConverter.class)
    @Field(type = FieldType.Keyword)
    private ObjectId id;


    @Field(type = FieldType.Text, analyzer = "ngram_analyzer")
    private String username;



}


