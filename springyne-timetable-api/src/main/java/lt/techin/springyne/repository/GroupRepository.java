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

    Page<Group> findAllByNameIgnoreCaseContainingOrProgramNameIgnoreCaseContainingOrYearIgnoreCaseContaining(String name,String programName,String year, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingOrYearIgnoreCaseContaining(String name, String year, Pageable pageable);

    Page<Group> findAllByProgramNameIgnoreCaseContainingOrYearIgnoreCaseContaining(String programName, String year, Pageable pageable);


    boolean existsByNameIgnoreCase(String name);


//    List<Group> findByModuleIdOrderByDeletedAscIdAsc(Long moduleId);
//    List<Group> findByDeletedFalseAndModuleIdNotOrModuleIdIsNull(Long moduleId);
}