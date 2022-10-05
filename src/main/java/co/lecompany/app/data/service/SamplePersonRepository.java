package co.lecompany.app.data.service;

import co.lecompany.app.data.entity.SamplePerson;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SamplePersonRepository extends JpaRepository<SamplePerson, UUID> {

    @Query(nativeQuery = true, value = "SELECT * FROM sample_person WHERE email = :email")
    Optional<SamplePerson> findByEmail(@Param("email") String email);
}