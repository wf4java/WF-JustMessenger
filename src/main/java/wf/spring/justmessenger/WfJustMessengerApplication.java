package wf.spring.justmessenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.mongo.MongoDataAutoConfiguration;
import org.springframework.boot.autoconfigure.data.mongo.MongoRepositoriesAutoConfiguration;

import java.util.Arrays;

@SpringBootApplication
public class WfJustMessengerApplication {

    public static void main(String[] args) {
        SpringApplication.run(WfJustMessengerApplication.class, args);
    }

}
