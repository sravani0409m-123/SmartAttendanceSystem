import java.io.Serializable;

public class Subject implements Serializable {

    private static final long serialVersionUID = 1L;

    int subjectId;
    String subjectName;

    Subject(int id, String name) {

        this.subjectId = id;
        this.subjectName = name;
    }
}
