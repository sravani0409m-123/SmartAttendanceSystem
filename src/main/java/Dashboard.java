import javafx.application.Application;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Dashboard extends Application {

    private BorderPane mainLayout;

    private ObservableList<Student> studentList =
            FXCollections.observableArrayList();

    private ObservableList<Subject> subjectList =
            FXCollections.observableArrayList();

    private ObservableList<AttendanceRecord> recordList =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) {

        DataStorage.loadData(
                Main.students,
                Main.subjects,
                Main.records
        );

        studentList.setAll(Main.students);
        subjectList.setAll(Main.subjects);
        recordList.setAll(Main.records);

        mainLayout = new BorderPane();

        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(25));
        sidebar.setPrefWidth(220);
        sidebar.setStyle("-fx-background-color: #172554;");

        Label appTitle = new Label("SMART ATTENDANCE");
        appTitle.setStyle(
                "-fx-text-fill: white;" +
                "-fx-font-size: 18px;" +
                "-fx-font-weight: bold;"
        );

        Button dashboardButton = createNavButton("Dashboard");
        Button studentsButton = createNavButton("Students");
        Button subjectsButton = createNavButton("Subjects");
        Button attendanceButton = createNavButton("Mark Attendance");
        Button reportsButton = createNavButton("Reports");

        sidebar.getChildren().addAll(
                appTitle,
                dashboardButton,
                studentsButton,
                subjectsButton,
                attendanceButton,
                reportsButton
        );

        mainLayout.setLeft(sidebar);

        dashboardButton.setOnAction(e -> showDashboard());
        studentsButton.setOnAction(e -> showStudentsPage());
        subjectsButton.setOnAction(e -> showSubjectsPage());
        attendanceButton.setOnAction(e -> showAttendancePage());
        reportsButton.setOnAction(e -> showReportsPage());

        showDashboard();

        Scene scene = new Scene(mainLayout, 1100, 650);

        stage.setTitle("Smart Attendance System");
        stage.setScene(scene);
        stage.show();
    }

    private Button createNavButton(String text) {

        Button button = new Button(text);
        button.setMaxWidth(Double.MAX_VALUE);

        button.setStyle(
                "-fx-background-color: #1e3a8a;" +
                "-fx-text-fill: white;" +
                "-fx-font-size: 14px;" +
                "-fx-padding: 12px;"
        );

        return button;
    }

    // DASHBOARD

    private void showDashboard() {

        VBox content = new VBox(25);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f1f5f9;");

        Label heading = new Label("Dashboard");
        heading.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        HBox cards = new HBox(20);

        cards.getChildren().addAll(
                createCard(
                        "Total Students",
                        String.valueOf(Main.students.size())
                ),
                createCard(
                        "Total Subjects",
                        String.valueOf(Main.subjects.size())
                ),
                createCard(
                        "Attendance Records",
                        String.valueOf(Main.records.size())
                )
        );

        content.getChildren().addAll(heading, cards);
        mainLayout.setCenter(content);
    }

    private VBox createCard(String title, String value) {

        VBox card = new VBox(15);
        card.setPadding(new Insets(25));
        card.setPrefWidth(250);

        card.setStyle(
                "-fx-background-color: white;" +
                "-fx-background-radius: 12px;" +
                "-fx-effect: dropshadow(gaussian, lightgray, 10, 0, 0, 3);"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 15px;" +
                "-fx-text-fill: #64748b;"
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 30px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1e3a8a;"
        );

        card.getChildren().addAll(titleLabel, valueLabel);

        return card;
    }

    // STUDENT MANAGEMENT

    private void showStudentsPage() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f1f5f9;");

        Label heading = new Label("Student Management");
        heading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Student ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Student Name");

        TextField departmentField = new TextField();
        departmentField.setPromptText("Department");

        TextField sectionField = new TextField();
        sectionField.setPromptText("Section");

        HBox form = new HBox(
                10,
                idField,
                nameField,
                departmentField,
                sectionField
        );

        Button addButton = new Button("Add Student");
        Button deleteButton = new Button("Delete Selected");

        TableView<Student> table = new TableView<>();

        TableColumn<Student, Integer> idColumn =
                new TableColumn<>("Student ID");

        idColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().studentId
                ).asObject()
        );

        TableColumn<Student, String> nameColumn =
                new TableColumn<>("Student Name");

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().studentName
                )
        );

        TableColumn<Student, String> departmentColumn =
                new TableColumn<>("Department");

        departmentColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().department
                )
        );

        TableColumn<Student, String> sectionColumn =
                new TableColumn<>("Section");

        sectionColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().section
                )
        );

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                departmentColumn,
                sectionColumn
        );

        table.setItems(studentList);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        addButton.setOnAction(e -> {

            String idText = idField.getText().trim();
            String name = nameField.getText().trim();
            String department = departmentField.getText().trim();
            String section = sectionField.getText().trim();

            if (idText.isEmpty() || name.isEmpty()
                    || department.isEmpty() || section.isEmpty()) {

                showAlert("Please fill all fields.");
                return;
            }

            int id;

            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                showAlert("Student ID must be a number.");
                return;
            }

            for (Student student : studentList) {
                if (student.studentId == id) {
                    showAlert("Student ID already exists.");
                    return;
                }
            }

            Student student = new Student(
                    id,
                    name,
                    department,
                    section
            );

            studentList.add(student);
            Main.students.add(student);

            saveAllData();

            idField.clear();
            nameField.clear();
            departmentField.clear();
            sectionField.clear();
        });

        deleteButton.setOnAction(e -> {

            Student selected =
                    table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showAlert("Please select a student to delete.");
                return;
            }

            studentList.remove(selected);
            Main.students.remove(selected);

            saveAllData();
        });

        HBox buttons = new HBox(15, addButton, deleteButton);

        content.getChildren().addAll(
                heading,
                form,
                buttons,
                table
        );

        mainLayout.setCenter(content);
    }

    // SUBJECT MANAGEMENT

    private void showSubjectsPage() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f1f5f9;");

        Label heading = new Label("Subject Management");
        heading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        TextField idField = new TextField();
        idField.setPromptText("Subject ID");

        TextField nameField = new TextField();
        nameField.setPromptText("Subject Name");

        HBox form = new HBox(10, idField, nameField);

        Button addButton = new Button("Add Subject");
        Button deleteButton = new Button("Delete Selected");

        TableView<Subject> table = new TableView<>();

        TableColumn<Subject, Integer> idColumn =
                new TableColumn<>("Subject ID");

        idColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(
                        data.getValue().subjectId
                ).asObject()
        );

        TableColumn<Subject, String> nameColumn =
                new TableColumn<>("Subject Name");

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(
                        data.getValue().subjectName
                )
        );

        table.getColumns().addAll(idColumn, nameColumn);

        table.setItems(subjectList);
        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        addButton.setOnAction(e -> {

            String idText = idField.getText().trim();
            String name = nameField.getText().trim();

            if (idText.isEmpty() || name.isEmpty()) {
                showAlert("Please fill all fields.");
                return;
            }

            int id;

            try {
                id = Integer.parseInt(idText);
            } catch (NumberFormatException ex) {
                showAlert("Subject ID must be a number.");
                return;
            }

            for (Subject subject : subjectList) {
                if (subject.subjectId == id) {
                    showAlert("Subject ID already exists.");
                    return;
                }
            }

            Subject subject = new Subject(id, name);

            subjectList.add(subject);
            Main.subjects.add(subject);

            saveAllData();

            idField.clear();
            nameField.clear();
        });

        deleteButton.setOnAction(e -> {

            Subject selected =
                    table.getSelectionModel().getSelectedItem();

            if (selected == null) {
                showAlert("Please select a subject to delete.");
                return;
            }

            subjectList.remove(selected);
            Main.subjects.remove(selected);

            saveAllData();
        });

        HBox buttons = new HBox(15, addButton, deleteButton);

        content.getChildren().addAll(
                heading,
                form,
                buttons,
                table
        );

        mainLayout.setCenter(content);
    }

    // MARK ATTENDANCE

    private void showAttendancePage() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f1f5f9;");

        Label heading = new Label("Mark Attendance");
        heading.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        ComboBox<Subject> subjectBox = new ComboBox<>(subjectList);
        subjectBox.setPromptText("Select Subject");
        subjectBox.setPrefWidth(250);

        subjectBox.setConverter(new StringConverter<Subject>() {
            @Override
            public String toString(Subject subject) {
                if (subject == null) return "";
                return subject.subjectId + " - " + subject.subjectName;
            }

            @Override
            public Subject fromString(String string) {
                return null;
            }
        });

        DatePicker datePicker = new DatePicker(LocalDate.now());

        HBox selectionRow = new HBox(15, subjectBox, datePicker);
        selectionRow.setAlignment(Pos.CENTER_LEFT);

        Label instructionLabel = new Label(
                "All students are Absent by default. Tick the checkbox for students who are Present."
        );

        ObservableList<AttendanceMarkRow> attendanceRows =
                FXCollections.observableArrayList();

        TableView<AttendanceMarkRow> table = new TableView<>();

        // Important: Enable editing so the checkboxes can be clicked.
        table.setEditable(true);
        table.setItems(attendanceRows);

        TableColumn<AttendanceMarkRow, Number> idColumn =
                new TableColumn<>("Roll Number");

        idColumn.setCellValueFactory(data ->
                new SimpleIntegerProperty(data.getValue().student.studentId)
        );

        TableColumn<AttendanceMarkRow, String> nameColumn =
                new TableColumn<>("Student Name");

        nameColumn.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().student.studentName)
        );

        TableColumn<AttendanceMarkRow, Boolean> attendanceColumn =
                new TableColumn<>("Present");

        attendanceColumn.setCellValueFactory(data ->
                data.getValue().present
        );

        attendanceColumn.setCellFactory(
                javafx.scene.control.cell.CheckBoxTableCell.forTableColumn(
                        attendanceColumn
                )
        );

        // Important: Enable editing for the checkbox column too.
        attendanceColumn.setEditable(true);

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                attendanceColumn
        );

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        // Load students in ascending roll-number order and restore saved values.
        Runnable refreshAttendanceSheet = () -> {

            attendanceRows.clear();

            if (subjectBox.getValue() == null ||
                    datePicker.getValue() == null) {
                return;
            }

            Subject selectedSubject = subjectBox.getValue();
            String selectedDate = datePicker.getValue().toString();

            studentList.stream()
                    .sorted((a, b) ->
                            Integer.compare(a.studentId, b.studentId))
                    .forEach(student -> {

                        // New attendance entries are Absent by default.
                        boolean isPresent = false;

                        // Restore attendance already saved for this subject and date.
                        for (AttendanceRecord record : recordList) {

                            if (record.student.studentId == student.studentId
                                    && record.subject.subjectId == selectedSubject.subjectId
                                    && record.date.equals(selectedDate)) {

                                isPresent = record.present;
                                break;
                            }
                        }

                        attendanceRows.add(
                                new AttendanceMarkRow(student, isPresent)
                        );
                    });
        };

        subjectBox.valueProperty().addListener(
                (observable, oldSubject, newSubject) ->
                        refreshAttendanceSheet.run()
        );

        datePicker.valueProperty().addListener(
                (observable, oldDate, newDate) ->
                        refreshAttendanceSheet.run()
        );

        Button allPresentButton = new Button("Mark All Present");
        Button saveButton = new Button("Save Attendance");

        HBox buttonRow = new HBox(15, allPresentButton, saveButton);

        allPresentButton.setOnAction(e -> {
            for (AttendanceMarkRow row : attendanceRows) {
                row.present.set(true);
            }
        });

        saveButton.setOnAction(e -> {

            Subject selectedSubject = subjectBox.getValue();
            LocalDate selectedDate = datePicker.getValue();

            if (selectedSubject == null) {
                showAlert("Please select a subject.");
                return;
            }

            if (selectedDate == null) {
                showAlert("Please select a date.");
                return;
            }

            if (attendanceRows.isEmpty()) {
                showAlert("No students are registered. Please add students first.");
                return;
            }

            String date = selectedDate.toString();

            // Update existing records or create new records for every student.
            for (AttendanceMarkRow row : attendanceRows) {

                AttendanceRecord existingRecord = null;

                for (AttendanceRecord record : recordList) {

                    if (record.student.studentId == row.student.studentId
                            && record.subject.subjectId == selectedSubject.subjectId
                            && record.date.equals(date)) {

                        existingRecord = record;
                        break;
                    }
                }

                if (existingRecord != null) {

                    // Update the existing record instead of creating a duplicate.
                    existingRecord.present = row.present.get();

                } else {

                    AttendanceRecord newRecord = new AttendanceRecord(
                            row.student,
                            selectedSubject,
                            row.present.get(),
                            date
                    );

                    recordList.add(newRecord);
                    Main.records.add(newRecord);
                }
            }

            saveAllData();
            showAlert("Attendance saved successfully for all students.");
        });

        content.getChildren().addAll(
                heading,
                selectionRow,
                instructionLabel,
                buttonRow,
                table
        );

        mainLayout.setCenter(content);
    }

    // ATTENDANCE REPORTS

    private void showReportsPage() {

        VBox content = new VBox(20);
        content.setPadding(new Insets(30));
        content.setStyle("-fx-background-color: #f1f5f9;");

        Label heading = new Label("Attendance Reports");
        heading.setStyle("-fx-font-size: 28px; -fx-font-weight: bold;");

        ComboBox<Subject> subjectBox = new ComboBox<>(subjectList);
        subjectBox.setPromptText("Select Subject");
        subjectBox.setPrefWidth(280);

        subjectBox.setConverter(new StringConverter<Subject>() {

            @Override
            public String toString(Subject subject) {
                if (subject == null) {
                    return "";
                }
                return subject.subjectId + " - " + subject.subjectName;
            }

            @Override
            public Subject fromString(String string) {
                return null;
            }
        });

        Label summaryLabel = new Label(
                "Select a subject to view its attendance report."
        );

        ObservableList<ReportRow> reportRows =
                FXCollections.observableArrayList();

        TableView<ReportRow> table = new TableView<>();

        TableColumn<ReportRow, Integer> idColumn =
                new TableColumn<>("Student ID");

        idColumn.setCellValueFactory(data ->
                data.getValue().studentId.asObject()
        );

        TableColumn<ReportRow, String> nameColumn =
                new TableColumn<>("Student Name");

        nameColumn.setCellValueFactory(data ->
                data.getValue().studentName
        );

        TableColumn<ReportRow, Integer> totalColumn =
                new TableColumn<>("Total Classes");

        totalColumn.setCellValueFactory(data ->
                data.getValue().totalClasses.asObject()
        );

        TableColumn<ReportRow, Integer> attendedColumn =
                new TableColumn<>("Classes Attended");

        attendedColumn.setCellValueFactory(data ->
                data.getValue().classesAttended.asObject()
        );

        TableColumn<ReportRow, String> percentageColumn =
                new TableColumn<>("Attendance %");

        percentageColumn.setCellValueFactory(data ->
                data.getValue().percentage
        );

        TableColumn<ReportRow, String> statusColumn =
                new TableColumn<>("Status");

        statusColumn.setCellValueFactory(data ->
                data.getValue().status
        );

        // Show only shortage alerts in red.
        statusColumn.setCellFactory(column ->
                new TableCell<ReportRow, String>() {

                    @Override
                    protected void updateItem(String status, boolean empty) {

                        super.updateItem(status, empty);

                        if (empty || status == null || status.isEmpty()) {
                            setText(null);
                            setStyle("");
                        } else {
                            setText(status);

                            setStyle(
                                    "-fx-text-fill: red;" +
                                    "-fx-font-weight: bold;"
                            );
                        }
                    }
                }
        );

        table.getColumns().addAll(
                idColumn,
                nameColumn,
                totalColumn,
                attendedColumn,
                percentageColumn,
                statusColumn
        );

        table.setItems(reportRows);

        table.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        subjectBox.valueProperty().addListener(
                (observable, oldSubject, selectedSubject) -> {

                    updateReport(
                            selectedSubject,
                            reportRows,
                            summaryLabel
                    );
                }
        );

        if (subjectList.isEmpty()) {
            summaryLabel.setText(
                    "Add a subject first, then take attendance to view reports."
            );
        }

        content.getChildren().addAll(
                heading,
                subjectBox,
                summaryLabel,
                table
        );

        mainLayout.setCenter(content);
    }

    // UPDATE REPORT

    private void updateReport(
            Subject selectedSubject,
            ObservableList<ReportRow> reportRows,
            Label summaryLabel
    ) {

        reportRows.clear();

        if (selectedSubject == null) {
            summaryLabel.setText(
                    "Select a subject to view its attendance report."
            );
            return;
        }

        // Count unique class dates for the selected subject.
        Set<String> classDates = new HashSet<>();

        for (AttendanceRecord record : recordList) {

            if (record.subject.subjectId == selectedSubject.subjectId) {
                classDates.add(record.date);
            }
        }

        int totalClasses = classDates.size();

        // Create one report row for every student.
        for (Student student : studentList) {

            Set<String> attendedDates = new HashSet<>();

            for (AttendanceRecord record : recordList) {

                if (record.student.studentId == student.studentId
                        && record.subject.subjectId == selectedSubject.subjectId
                        && record.present) {

                    attendedDates.add(record.date);
                }
            }

            int classesAttended = attendedDates.size();

            double percentage = totalClasses == 0
                    ? 0.0
                    : (classesAttended * 100.0) / totalClasses;

            String percentageText =
                    String.format("%.2f%%", percentage);

            // Only students below 75% receive a shortage alert.
            String status = "";

            if (totalClasses > 0 && percentage < 75.0) {
                status = "SHORTAGE ALERT";
            }

            reportRows.add(new ReportRow(
                    student.studentId,
                    student.studentName,
                    totalClasses,
                    classesAttended,
                    percentageText,
                    status
            ));
        }

        if (totalClasses == 0) {

            summaryLabel.setText(
                    selectedSubject.subjectName
                            + " - No attendance has been recorded yet."
            );

        } else {

            int shortageCount = 0;

            for (ReportRow row : reportRows) {

                if (row.status.get().equals("SHORTAGE ALERT")) {
                    shortageCount++;
                }
            }

            summaryLabel.setText(
                    selectedSubject.subjectName
                            + " | Total Classes: " + totalClasses
                            + " | Students with Shortage: " + shortageCount
            );
        }
    }

    // ATTENDANCE MARK ROW CLASS

    private static class AttendanceMarkRow {

        Student student;
        SimpleBooleanProperty present;

        AttendanceMarkRow(Student student, boolean present) {
            this.student = student;
            this.present = new SimpleBooleanProperty(present);
        }
    }

    // REPORT ROW CLASS

    private static class ReportRow {

        SimpleIntegerProperty studentId;
        SimpleStringProperty studentName;
        SimpleIntegerProperty totalClasses;
        SimpleIntegerProperty classesAttended;
        SimpleStringProperty percentage;
        SimpleStringProperty status;

        ReportRow(
                int studentId,
                String studentName,
                int totalClasses,
                int classesAttended,
                String percentage,
                String status
        ) {

            this.studentId =
                    new SimpleIntegerProperty(studentId);

            this.studentName =
                    new SimpleStringProperty(studentName);

            this.totalClasses =
                    new SimpleIntegerProperty(totalClasses);

            this.classesAttended =
                    new SimpleIntegerProperty(classesAttended);

            this.percentage =
                    new SimpleStringProperty(percentage);

            this.status =
                    new SimpleStringProperty(status);
        }
    }

    // SAVE DATA

    private void saveAllData() {

        DataStorage.saveData(
                Main.students,
                Main.subjects,
                Main.records
        );
    }

    // ALERT

    private void showAlert(String message) {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);

        alert.setTitle("Smart Attendance System");
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}