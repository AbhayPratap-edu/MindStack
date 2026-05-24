package dev.abhaya.mindstack.repository;

import dev.abhaya.mindstack.model.TestModel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TestDBRepository extends JpaRepository<TestModel, Long> {
    TestModel findByName(String name);
}
