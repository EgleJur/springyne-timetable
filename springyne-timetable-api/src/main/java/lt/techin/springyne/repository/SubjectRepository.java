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
    List<Subject> findAllSubjectsAcsDeletedOrder();
//
//    @Query(value = "INSERT INTO SUBJECT_AND_MODULES (SUBJECT_ID, MODULE_ID) VALUES (subId, modId)",
//            nativeQuery = true)
//    List<Subject> insertSubjects(@Param ("SUBJECT_ID") Long subID, @Param("MODULE_ID") Long modId);
//@EntityGraph(attributePaths="module")
//Optional<Subject> findModuleWithSubjectById(Long id);
}
