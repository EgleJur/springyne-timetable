package lt.techin.springyne.stubs;

import lt.techin.springyne.model.Teacher;

public class TeacherCreator {
    public static Teacher createTeacher(Long id) {
        Teacher teacher = new Teacher(id,"T1", "Test name1", "Test lastname1", false, null);

        return teacher;
    }
}
