package lt.techin.springyne.repository;

import lt.techin.springyne.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


//    List<Subject> findAllByOrderByDeletedAcsNameAsc();

//    @Query("select s from Subject s join Module m where m.name = :moduleName")
//    List<Subject> findByModule_ModuleName(@Param("moduleName") String moduleName);

    Page<Subject> findByModuleName(String name, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);
//
//    @Query(value = "INSERT INTO SUBJECT_AND_MODULES (SUBJECT_ID, MODULE_ID) VALUES (subId, modId)",
//            nativeQuery = true)
//    List<Subject> insertSubjects(@Param ("SUBJECT_ID") Long subID, @Param("MODULE_ID") Long modId);
//@EntityGraph(attributePaths="module")
//Optional<Subject> findModuleWithSubjectById(Long id);
}
