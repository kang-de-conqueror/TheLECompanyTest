package co.lecompany.app.data.service;

import co.lecompany.app.data.entity.SamplePerson;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

@Service
public class SamplePersonService {

    private final SamplePersonRepository repository;

    @Autowired
    public SamplePersonService(SamplePersonRepository repository) {
        this.repository = repository;
    }

    public Optional<SamplePerson> get(UUID id) {
        return repository.findById(id);
    }

    public SamplePerson update(SamplePerson entity) {
        return repository.save(entity);
    }

    public void delete(UUID id) {
        repository.deleteById(id);
    }

    public boolean deleteByEmail(String email) {
        Optional<SamplePerson> optionalSamplePerson = repository.findByEmail(email);
        if (optionalSamplePerson.isEmpty()) {
            return false;
        }
        repository.delete(optionalSamplePerson.get());

        return true;
    }

    public Page<SamplePerson> list(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public int count() {
        return (int) repository.count();
    }

    public List<SamplePerson> getWithMultipleConditions(Map<String, String> conditions) {
        int page = Integer.parseInt(conditions.getOrDefault("page", "0"));
        int size = Integer.parseInt(conditions.getOrDefault("size", "10"));
        SamplePerson samplePerson = new SamplePerson();
        samplePerson.setFirstName(conditions.get("firstName"));
        samplePerson.setLastName(conditions.get("lastName"));
        samplePerson.setEmail(conditions.get("email"));

        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                ;

        Example<SamplePerson> example = Example.of(samplePerson, exampleMatcher);



        return repository.findAll(example, PageRequest.of(page, size)).toList();
    }

}
