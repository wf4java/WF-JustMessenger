package wf.spring.justmessenger.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import jakarta.validation.Validator;
import org.bson.types.ObjectId;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.task.TaskExecutor;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import wf.spring.justmessenger.properties.JwtProperties;
import wf.spring.justmessenger.model.json.ObjectIdDeserializer;
import wf.spring.justmessenger.model.json.ObjectIdSerializer;
import wf.spring.justmessenger.properties.RefreshTokenProperties;
import wf.spring.justmessenger.properties.SecurityProperties;
import wf.spring_boot.minio.MinioConfiguration;

@Configuration
@EnableAsync
@EnableConfigurationProperties({SecurityProperties.class ,JwtProperties.class, RefreshTokenProperties.class})
@EnableElasticsearchRepositories
@Import(MinioConfiguration.class)
public class ApplicationConfig {

    @Bean
    public ValidatingMongoEventListener validatingMongoEventListener() {
        return new ValidatingMongoEventListener(validator());
    }

    @Bean
    public Validator validator() {
        return new LocalValidatorFactoryBean();
    }

    @Bean
    public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
        ObjectMapper objectMapper = builder.build();

        SimpleModule objectIdModule = new SimpleModule();
        objectIdModule.addSerializer(ObjectId.class, new ObjectIdSerializer());
        objectIdModule.addDeserializer(ObjectId.class, new ObjectIdDeserializer());
        objectMapper.registerModule(objectIdModule);

        return objectMapper;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);

        return executor;
    }

}
