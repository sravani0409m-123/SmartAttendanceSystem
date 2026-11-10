import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);

    static ArrayList<Student> students = new ArrayList<>();
    static ArrayList<Subject> subjects = new ArrayList<>();
    static ArrayList<AttendanceRecord> records = new ArrayList<>();

    public static void main(String[] args) {

        // Load previously saved data
        DataStorage.loadData(students, subjects, records);

        int choice;

        do {
            System.out.println("\n===== SMART ATTENDANCE SYSTEM =====");
            System.out.println("1. Register Students");
            System.out.println("2. Register Subjects");
            System.out.println("3. Mark Attendance");
            System.out.println("4. View Attendance Report");
            System.out.println("5. Exit");

            System.out.print("Enter your choice: ");
            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {

                case 1:
                    registerStudents();
                    break;

                case 2:
                    registerSubjects();
                    break;

                case 3:
                    markAttendance();
                    break;

                case 4:
                    viewAttendanceReport();
                    break;

                case 5:
                    System.out.println("Exiting Smart Attendance System...");
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
            }

            // Save data after every menu operation
            DataStorage.saveData(students, subjects, records);

        } while (choice != 5);

        sc.close();
    }

    // Student Registration
    static void registerStudents() {

        System.out.print("How many students? ");
        int count = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < count; i++) {

            System.out.println("\nEnter Student Details");

            System.out.print("Student ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Student Name: ");
            String name = sc.nextLine();

            System.out.print("Department: ");
            String department = sc.nextLine();

            System.out.print("Section: ");
            String section = sc.nextLine();

            Student s = new Student(id, name, department, section);

            students.add(s);

            System.out.println("Student registered successfully!");
        }
    }

    // Subject Registration
    static void registerSubjects() {

        System.out.print("How many subjects? ");
        int count = sc.nextInt();
        sc.nextLine();

        for (int i = 0; i < count; i++) {

            System.out.print("Subject ID: ");
            int id = sc.nextInt();
            sc.nextLine();

            System.out.print("Subject Name: ");
            String name = sc.nextLine();

            subjects.add(new Subject(id, name));

            System.out.println("Subject registered successfully!");
        }
    }

    // Mark Attendance
    static void markAttendance() {

        if (students.isEmpty() || subjects.isEmpty()) {
            System.out.println(
                    "Please register students and subjects first!"
            );
            return;
        }

        System.out.println("\n===== AVAILABLE SUBJECTS =====");

        for (Subject s : subjects) {
            System.out.println(s.subjectId + " - " + s.subjectName);
        }

        System.out.print("Enter Subject ID: ");
        int selectedId = sc.nextInt();
        sc.nextLine();

        Subject selectedSubject = null;

        for (Subject s : subjects) {

            if (s.subjectId == selectedId) {
                selectedSubject = s;
                break;
            }
        }

        if (selectedSubject == null) {
            System.out.println("Subject not found!");
            return;
        }

        System.out.print("How many class sessions? ");
        int sessionCount = sc.nextInt();
        sc.nextLine();

        for (int session = 1; session <= sessionCount; session++) {

            System.out.println("\n===== SESSION " + session + " =====");

            System.out.print("Enter date (YYYY-MM-DD): ");
            String date = sc.nextLine();

            System.out.println("Enter 1 for Present and 0 for Absent");

            for (Student s : students) {

                System.out.print(s.studentName + ": ");
                int attendanceChoice = sc.nextInt();

                boolean present = (attendanceChoice == 1);

                // Correct attendance record creation
                AttendanceRecord record =
                        new AttendanceRecord(
                                s,
                                selectedSubject,
                                present,
                                date
                        );

                records.add(record);
            }

            sc.nextLine();
        }

        System.out.println("Attendance marked successfully!");
    }

    // View Attendance Report
    static void viewAttendanceReport() {

        if (records.isEmpty()) {
            System.out.println("No attendance records available!");
            return;
        }

        System.out.println("\n===== AVAILABLE SUBJECTS =====");

        for (Subject s : subjects) {
            System.out.println(s.subjectId + " - " + s.subjectName);
        }

        System.out.print("Enter Subject ID for report: ");
        int selectedId = sc.nextInt();
        sc.nextLine();

        Subject selectedSubject = null;

        for (Subject s : subjects) {

            if (s.subjectId == selectedId) {
                selectedSubject = s;
                break;
            }
        }

        if (selectedSubject == null) {
            System.out.println("Subject not found!");
            return;
        }

        System.out.println("\n===== ATTENDANCE REPORT =====");

        for (Student s : students) {

            int totalClasses = 0;
            int presentClasses = 0;

            System.out.println("\n------------------------------");
            System.out.println("Student ID: " + s.studentId);
            System.out.println("Student Name: " + s.studentName);
            System.out.println("Department: " + s.department);
            System.out.println("Section: " + s.section);
            System.out.println("Subject: " + selectedSubject.subjectName);

            System.out.println("\n===== DATE-WISE ATTENDANCE =====");

            for (AttendanceRecord r : records) {

                if (r.student.studentId == s.studentId
                        && r.subject.subjectId == selectedSubject.subjectId) {

                    totalClasses++;

                    if (r.present) {
                        presentClasses++;
                        System.out.println(r.date + " : Present");
                    } else {
                        System.out.println(r.date + " : Absent");
                    }
                }
            }

            double percentage = 0;

            if (totalClasses > 0) {
                percentage =
                        (presentClasses * 100.0) / totalClasses;
            }

            System.out.println("\nPresent: " + presentClasses);
            System.out.println("Total Classes: " + totalClasses);

            System.out.printf(
                    "Attendance: %.2f%%\n",
                    percentage
            );

            if (percentage < 75) {
                System.out.println(
                        "SHORTAGE ALERT: Attendance below 75%!"
                );
            } else {
                System.out.println(
                        "Attendance is satisfactory."
                );
            }
        }
    }
}