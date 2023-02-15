package lt.techin.springyne.repository;

import lt.techin.springyne.model.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


//    List<Subject> findAllByOrderByDeletedAcsNameAsc();

//    @Query("select s from Subject s join Module m where m.name = :moduleName")
//    List<Subject> findByModule_ModuleName(@Param("moduleName") String moduleName);

    Page<Subject> findByModuleName(String name, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO SUBJECTS_IN_ROOMS (SUBJECT_ID, ROOM_ID) VALUES (:SUB_ID, :R_ID)",
            nativeQuery = true)
    void insertSubjectAndRoom(@Param("SUB_ID") Long subID, @Param("R_ID") Long modId);
//@EntityGraph(attributePaths="module")
//Optional<Subject> findModuleWithSubjectById(Long id);
}
