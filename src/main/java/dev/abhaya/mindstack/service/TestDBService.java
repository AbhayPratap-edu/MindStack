package dev.abhaya.mindstack.service;

import dev.abhaya.mindstack.model.TestModel;
import dev.abhaya.mindstack.repository.TestDBRepository;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class TestDBService {

    private final TestDBRepository testDBRepository;

    public TestModel getTestUser(String name){
        return testDBRepository.findByName(name);
    }

    public List<TestModel> findAll(){
        return testDBRepository.findAll();
    }

    public TestModel saveTestUser(TestModel testModel){
        return testDBRepository.save(testModel);
    }


}
