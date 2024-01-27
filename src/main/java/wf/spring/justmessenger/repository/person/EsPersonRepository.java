package wf.spring.justmessenger.repository.person;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import wf.spring.justmessenger.entity.person.EsPerson;

import java.util.List;

public interface EsPersonRepository extends ElasticsearchRepository<EsPerson, ObjectId> {


    public List<EsPerson> findAllByUsername(String username, Pageable pageable);



}
