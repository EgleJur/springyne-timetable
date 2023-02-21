package lt.techin.springyne.repository;

import lt.techin.springyne.model.Group;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

Page<Group> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
    Page<Group> findAllByProgramNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingOrProgramNameIgnoreCaseContaining(String name,String programName, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingOrProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(String name,String programName,String groupYear, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(String name, String groupYear, Pageable pageable);

    Page<Group> findAllByProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(String programName, String groupYear, Pageable pageable);

    Page<Group> findAllByGroupYearIgnoreCaseContaining (String groupYear, Pageable pageable);


    boolean existsByNameIgnoreCase(String name);


//    List<Group> findByModuleIdOrderByDeletedAscIdAsc(Long moduleId);
//    List<Group> findByDeletedFalseAndModuleIdNotOrModuleIdIsNull(Long moduleId);
}