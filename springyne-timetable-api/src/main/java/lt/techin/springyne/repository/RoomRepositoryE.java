package lt.techin.springyne.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoomRepositoryE extends JpaRepository<Room, Long> {


}
