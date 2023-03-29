package lt.techin.springyne.holiday;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidaysRepository extends JpaRepository<Holiday, Long> {

    List<Holiday> findAllByNameIgnoreCaseContaining(String name);

    @Query(value = "select case when count(name)> 0 then true else false end from HOLIDAY " +
            "where lower(NAME) like lower(:NAME) " +
            "AND STARTS = :STARTS AND ENDS = :ENDS", nativeQuery = true)
    boolean findAllByNameIgnoreCase(@Param("NAME") String name, @Param("STARTS") LocalDate starts, @Param("ENDS") LocalDate ends);

    @Query(value = "SELECT * FROM HOLIDAY where (TO_CHAR(starts, 'MM-DD')>=TO_CHAR(:YEAR_START, 'MM-DD') " +
            "or TO_CHAR(starts, 'MM-DD')<=TO_CHAR(:YEAR_END, 'MM-DD')) " +
            "and ends<=:YEAR_END ", nativeQuery = true)
    List<Holiday> findAllHolidaysByDate(@Param("YEAR_START") LocalDate starts, @Param("YEAR_END") LocalDate ends );

}
