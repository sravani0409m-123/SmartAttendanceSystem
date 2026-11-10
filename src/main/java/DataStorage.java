import java.io.*;
import java.util.ArrayList;

public class DataStorage {

    private static final String FILE_NAME = "attendance_data.dat";

    // Save all data
    public static void saveData(
            ArrayList<Student> students,
            ArrayList<Subject> subjects,
            ArrayList<AttendanceRecord> records) {

        try (ObjectOutputStream out =
                     new ObjectOutputStream(
                             new FileOutputStream(FILE_NAME))) {

            out.writeObject(students);
            out.writeObject(subjects);
            out.writeObject(records);

        } catch (IOException e) {

            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    // Load all data
    @SuppressWarnings("unchecked")
    public static void loadData(
            ArrayList<Student> students,
            ArrayList<Subject> subjects,
            ArrayList<AttendanceRecord> records) {

        File file = new File(FILE_NAME);

        if (!file.exists()) {
            System.out.println("No previous data found.");
            return;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(
                             new FileInputStream(FILE_NAME))) {

            students.clear();
            subjects.clear();
            records.clear();

            students.addAll(
                    (ArrayList<Student>) in.readObject()
            );

            subjects.addAll(
                    (ArrayList<Subject>) in.readObject()
            );

            records.addAll(
                    (ArrayList<AttendanceRecord>) in.readObject()
            );

            System.out.println("Previous data loaded successfully!");

        } catch (IOException | ClassNotFoundException e) {

            System.out.println("Error loading data: " + e.getMessage());
        }
    }
}
