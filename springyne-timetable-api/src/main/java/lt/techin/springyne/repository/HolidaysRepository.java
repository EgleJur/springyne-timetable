package lt.techin.springyne.repository;

import lt.techin.springyne.model.Holidays;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HolidaysRepository extends JpaRepository<Holidays, Long> {
    boolean existsByNameIgnoreCase(String name);
    Holidays findOneByNameIgnoreCase(String name);
}
