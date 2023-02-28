package lt.techin.springyne.program;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramRepository extends JpaRepository<Program, Long> {

    Page<Program> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
}
