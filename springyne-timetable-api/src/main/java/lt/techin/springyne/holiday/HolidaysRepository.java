package lt.techin.springyne.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidaysRepository extends JpaRepository<Holiday, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<Holiday> findAllByNameIgnoreCaseContainingOrderByStartsAsc(String name);

    List<Holiday> findAllByStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(LocalDate ends, LocalDate starts);

    List<Holiday> findAllByNameIgnoreCaseContainingOrStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(String name, LocalDate ends, LocalDate starts);
    @Query(value = "SELECT * FROM HOLIDAY ORDER BY STARTS ASC",
            nativeQuery = true)
    List<Holiday> findAllOrderByStartsAsc();
    @Query(value = "SELECT * FROM HOLIDAY WHERE YEAR(STARTS)=:YEAR_NOW ORDER BY STARTS ASC",
            nativeQuery = true)
    List<Holiday> findAllHolidays(@Param("YEAR_NOW") int y);
//    List<Holidays> findAllOrderByDeletedAscStartsAsc(); SELECT * FROM HOLIDAYS WHERE year(starts)='2023'
}