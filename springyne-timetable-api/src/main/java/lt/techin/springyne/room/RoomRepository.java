package lt.techin.springyne.room;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByOrderByDeletedAscIdAsc();

    boolean existsByNameIgnoreCase(String name);

    Page<Room> findAllByNameIgnoreCaseContaining(String name, Pageable pageable);

    Page<Room> findAllByBuildingIgnoreCaseContaining(String building, Pageable pageable);

    Page<Room> findAllByNameIgnoreCaseContainingAndBuildingIgnoreCaseContaining(String name, String building, Pageable pageable);
}
