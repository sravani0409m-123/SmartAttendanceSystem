import java.io.Serializable;

public class AttendanceRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    Student student;
    Subject subject;
    boolean present;
    String date;

    AttendanceRecord(Student student, Subject subject,
                     boolean present, String date) {

        this.student = student;
        this.subject = subject;
        this.present = present;
        this.date = date;
    }
}
