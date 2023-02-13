package lt.techin.springyne.controller;

import lt.techin.springyne.model.Teacher;
import lt.techin.springyne.repository.TeacherRepository;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("http://localhost:3000")
public class TeacherController {
    private final TeacherRepository repository;

    public TeacherController(TeacherRepository repository) {
        this.repository = repository;
    }

  /*  @GetMapping("/teachers")
    public Page<Teacher> getAllTeachers(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        if (name != null) {
            return (Page<Teacher>) repository.findByNameContaining(name, (Pageable) pageable);
        }
        return repository.findAll(pageable);
    }*/
    @GetMapping("/teachers")
    public List<Teacher> getAllTeachers() {
      return repository.findAll();
  }

    @PostMapping("/teachers")
    public Teacher addTeacher(@RequestBody Teacher teacher) {
        if (teacher.getName() == null || teacher.getName().trim().isEmpty() ||
                teacher.getLastname() == null || teacher.getLastname().trim().isEmpty() ||
                teacher.getEmail() == null || teacher.getEmail().trim().isEmpty() ||
                teacher.getSubject() == null || teacher.getSubject().trim().isEmpty() ||
                teacher.getShift() == null || teacher.getShift().trim().isEmpty()) {
            throw new IllegalArgumentException("Name, Lastname, Email, Subject, and Shift are required fields");
        }
        return repository.save(teacher);
    }

    @PutMapping("/teachers/{id}")
    public Teacher updateTeacher(@PathVariable Long id, @RequestBody Teacher teacher) {
        Teacher existingTeacher = repository.findById(id).orElse(null);
        if (existingTeacher == null) {
            throw new IllegalArgumentException("Teacher with id " + id + " not found");
        }

        if (teacher.getName() == null || teacher.getName().trim().isEmpty() ||
                teacher.getLastname() == null || teacher.getLastname().trim().isEmpty() ||
                teacher.getEmail() == null || teacher.getEmail().trim().isEmpty() ||
                teacher.getSubject() == null || teacher.getSubject().trim().isEmpty() ||
                teacher.getShift() == null || teacher.getShift().trim().isEmpty()) {
            throw new IllegalArgumentException("Name, Lastname, Email, Subject, and Shift are required fields");
        }

        existingTeacher.setName(teacher.getName());
        existingTeacher.setLastname(teacher.getLastname());
        existingTeacher.setTeams_email(teacher.getTeams_email());
        existingTeacher.setEmail(teacher.getEmail());
        existingTeacher.setPhone_number(teacher.getPhone_number());
        existingTeacher.setHours(teacher.getHours());
        existingTeacher.setSubject(teacher.getSubject());
        existingTeacher.setShift(teacher.getShift());
        return repository.save(existingTeacher);
    }
    @DeleteMapping("/teachers/{id}")
    public void deleteTeacher(@PathVariable Long id) {
        repository.deleteById(id);
    }
}

