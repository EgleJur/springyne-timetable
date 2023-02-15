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


    Page<Subject> findByModuleName(String name, Pageable pageable);

    boolean existsByNameIgnoreCase(String name);

    @Transactional
    @Modifying
    @Query(value = "INSERT INTO SUBJECTS_IN_ROOMS (SUBJECT_ID, ROOM_ID) VALUES (:SUBJECT_ID, :ROOM_ID)",
            nativeQuery = true)
    void insertSubjectAndRoom(@Param("SUBJECT_ID") Long subjectID, @Param("ROOM_ID") Long roomId);

    @Transactional
    @Modifying
    @Query(value = "DELETE FROM SUBJECTS_IN_ROOMS WHERE SUBJECT_ID= :SUBJECT_ID AND ROOM_ID = :ROOM_ID",
            nativeQuery = true)
    void deleteRoomFromSubject(@Param("SUBJECT_ID") Long subjectID, @Param("ROOM_ID") Long roomId);
}
