package lt.techin.springyne.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByOrderByDeletedAscIdAsc();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByBuildingIgnoreCase(String building);

    Page<Room> findAllByBuildingIgnoreCaseContaining(String building, Pageable pageable);

    Page<Room> findAllByNameIgnoreCaseContainingOrBuildingIgnoreCaseContaining(String name, String building, Pageable pageable);
}
