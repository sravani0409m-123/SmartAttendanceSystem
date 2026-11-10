import java.io.Serializable;

public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    int studentId;
    String studentName;
    String department;
    String section;

    Student(int id, String name, String department, String section) {

        this.studentId = id;
        this.studentName = name;
        this.department = department;
        this.section = section;
    }
}
