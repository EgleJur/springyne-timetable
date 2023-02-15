package lt.techin.springyne.repository;

import lt.techin.springyne.model.Shift;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    boolean existsByNameIgnoreCase(String name);
}
