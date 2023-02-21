package lt.techin.springyne.repository;

import lt.techin.springyne.model.Room;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    List<Room> findAllByOrderByDeletedAscIdAsc();

    boolean existsByNameIgnoreCase(String name);

    boolean existsByBuildingIgnoreCase(String building);

    Page<Room> findByNameContainingIgnoreCaseAndBuildingContainingIgnoreCaseAndDeletedIsFalse(String name, String building, PageRequest of);
}
