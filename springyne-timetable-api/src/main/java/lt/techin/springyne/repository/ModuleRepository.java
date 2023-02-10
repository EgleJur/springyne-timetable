package lt.techin.springyne.repository;

import lt.techin.springyne.model.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ModuleRepository extends JpaRepository<Module,Long> {

    boolean existsByNumberIgnoreCase(String name);

    List<Module> findAllByOrderByDeletedAscIdAsc();
}
