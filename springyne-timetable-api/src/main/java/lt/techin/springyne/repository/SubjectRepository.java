package lt.techin.springyne.repository;

import lt.techin.springyne.model.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {

    @Query(value = "SELECT * FROM SUBJECT_TABLE ORDER BY Deleted ASC",
            nativeQuery = true)
    List<Subject> findAllSubjects();
}
