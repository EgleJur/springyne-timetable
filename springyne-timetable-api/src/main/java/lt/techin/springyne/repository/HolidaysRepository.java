package lt.techin.springyne.repository;

import lt.techin.springyne.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface HolidaysRepository extends JpaRepository<Holiday, Long> {
    boolean existsByNameIgnoreCase(String name);
    List<Holiday> findAllByNameIgnoreCaseContaining(String name);

    List<Holiday> findAllByStartsLessThanEqualAndEndsGreaterThanEqual(Date ends, Date starts);

    List<Holiday> findAllByNameIgnoreCaseContainingOrStartsLessThanEqualAndEndsGreaterThanEqual(String name, Date ends, Date starts);

    @Query(value = "SELECT * FROM HOLIDAY WHERE YEAR(STARTS)=:YEAR_NOW",
            nativeQuery = true)
    List<Holiday> findAllHolidays(@Param("YEAR_NOW") int y);
//    List<Holidays> findAllOrderByDeletedAscStartsAsc(); SELECT * FROM HOLIDAYS WHERE year(starts)='2023'
}
