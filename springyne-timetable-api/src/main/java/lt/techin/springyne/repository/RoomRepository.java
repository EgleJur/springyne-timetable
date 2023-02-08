package lt.techin.springyne.repository;

import lt.techin.springyne.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
