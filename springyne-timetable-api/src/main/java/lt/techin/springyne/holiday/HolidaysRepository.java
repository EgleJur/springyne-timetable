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

    @Query(value = "select case when count(name)> 0 then true else false end from HOLIDAY " +
            "where lower(NAME) like lower(:NAME) " +
            "AND STARTS = :STARTS AND ENDS = :ENDS", nativeQuery = true)
    boolean findAllByNameIgnoreCase(@Param("NAME") String name, @Param("STARTS") LocalDate starts, @Param("ENDS") LocalDate ends);

    List<Holiday> findAllByStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(LocalDate ends, LocalDate starts);

    List<Holiday> findAllByNameIgnoreCaseContainingOrStartsLessThanEqualAndEndsGreaterThanEqualOrderByStartsAsc(String name, LocalDate ends, LocalDate starts);
    @Query(value = "SELECT * FROM HOLIDAY ORDER BY STARTS ASC",
            nativeQuery = true)
    List<Holiday> findAllOrderByStartsAsc();
    @Query(value = "SELECT * FROM HOLIDAY WHERE YEAR(STARTS)=:YEAR_NOW ORDER BY STARTS ASC",
            nativeQuery = true)
    List<Holiday> findAllHolidays(@Param("YEAR_NOW") int y);
//    List<Holidays> findAllOrderByDeletedAscStartsAsc(); SELECT * FROM HOLIDAYS WHERE year(starts)='2023
    @Query(value = "SELECT * FROM HOLIDAY where TO_CHAR(starts, 'MM-DD')>=TO_CHAR(:YEAR_START, 'MM-DD') " +
            "or TO_CHAR(starts, 'MM-DD')<=TO_CHAR(:YEAR_END, 'MM-DD') " +
            "and ends<=:YEAR_END " +
            "order by starts asc", nativeQuery = true)
    List<Holiday> findAllHolidaysByDate(@Param("YEAR_START") LocalDate starts, @Param("YEAR_END") LocalDate ends );

    List<Holiday> findByStartsBetweenAndEndsBetweenOrderByStartsAsc(LocalDate sStarts, LocalDate sEnds, LocalDate eStarts, LocalDate eEnds);

    @Query(value = "SELECT * FROM HOLIDAY where lower(name) like '%' || LOWER(:NAME) || '%' " +
            "and (TO_CHAR(starts, 'MM-DD')>=TO_CHAR(:YEAR_START, 'MM-DD') " +
            "or TO_CHAR(starts, 'MM-DD')<=TO_CHAR(:YEAR_END, 'MM-DD') " +
            "and ends<=:YEAR_END) " +
            "order by starts asc", nativeQuery = true)
    List<Holiday> findAllHolidaysByDateAndName(@Param("NAME") String name, @Param("YEAR_START") LocalDate starts, @Param("YEAR_END") LocalDate ends );

}
