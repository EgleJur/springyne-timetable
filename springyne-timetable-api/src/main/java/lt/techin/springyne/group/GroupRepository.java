package lt.techin.springyne.group;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

Page<Group> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
    Page<Group> findAllByProgramNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingAndProgramNameIgnoreCaseContaining(String name,String programName, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingAndProgramNameIgnoreCaseContainingAndGroupYearIgnoreCaseContaining(String name,String programName,String groupYear, Pageable pageable);

    Page<Group> findAllByNameIgnoreCaseContainingAndGroupYearIgnoreCaseContaining(String name, String groupYear, Pageable pageable);

    Page<Group> findAllByProgramNameIgnoreCaseContainingOrGroupYearIgnoreCaseContaining(String programName, String groupYear, Pageable pageable);

    Page<Group> findAllByGroupYearIgnoreCaseContaining (String groupYear, Pageable pageable);


    boolean existsByNameIgnoreCase(String name);

    Optional<Group> findById(Long id);


}