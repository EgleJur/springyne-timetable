package lt.techin.springyne.repository;

import lt.techin.springyne.model.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeacherRepository extends JpaRepository<Teacher,Long> {

    boolean existsByNumberIgnoreCase(String name);

    List<Teacher> findAllByOrderByDeletedAscIdAsc();
}
