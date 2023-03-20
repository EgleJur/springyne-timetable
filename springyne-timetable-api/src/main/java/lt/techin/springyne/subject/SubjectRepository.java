package lt.techin.springyne.subject;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {


    Page<Subject> findAllByModuleNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Subject> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
    Page<Subject> findAllByNameIgnoreCaseContainingOrModuleNameIgnoreCaseContaining(String name,String moduleName, Pageable pageable);
    boolean existsByNameIgnoreCase(String name);

    List<Subject> findByModuleIdOrderByDeletedAscIdAsc(Long moduleId);
    List<Subject> findByDeletedFalseAndModuleIdNotOrModuleIdIsNull(Long moduleId);
}