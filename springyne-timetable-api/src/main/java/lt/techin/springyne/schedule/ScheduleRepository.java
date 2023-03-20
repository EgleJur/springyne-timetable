package lt.techin.springyne.schedule;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ScheduleRepository extends JpaRepository<Schedule,Long> {

    List<Schedule> findByGroupId(Long groupId);

    Page<Schedule> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);
    Page<Schedule> findAllByNameIgnoreCaseContainingAndEndDateGreaterThanEqual(String name, LocalDate date, Pageable pageable);
}
