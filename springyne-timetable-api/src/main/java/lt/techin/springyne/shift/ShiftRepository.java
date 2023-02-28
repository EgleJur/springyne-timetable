package lt.techin.springyne.shift;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long> {
    boolean existsByNameIgnoreCase(String name);
    Shift findOneByNameIgnoreCase(String name);
}
