package lt.techin.springyne.repository;

import lt.techin.springyne.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {


   // Page<Group> findAllByModuleNameIgnoreCaseContaining(String name, Pageable pageable);

    //Page<Group> findAllByNameIgnoreCaseContainingOrModuleNameIgnoreCaseContaining(String name,String moduleName, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);


//    List<Group> findByModuleIdOrderByDeletedAscIdAsc(Long moduleId);
//    List<Group> findByDeletedFalseAndModuleIdNotOrModuleIdIsNull(Long moduleId);
}