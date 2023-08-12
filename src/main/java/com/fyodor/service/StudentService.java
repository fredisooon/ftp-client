package main.java.com.fyodor.service;


import main.java.com.fyodor.model.Student;
import main.java.com.fyodor.util.InputUtil;

import javax.json.*;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StudentService {
    private static final String FILE_PATH = "src/main/resources/static/students2.json";

    private JsonObject readJsonObjectFromFile() {
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(FILE_PATH));
            String fileContent = new String(fileBytes, StandardCharsets.UTF_8);
            return Json.createReader(new StringReader(fileContent)).readObject();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<Student> parseStudentsFromJson(JsonArray studentsArray) {
        List<Student> students = new ArrayList<>();
        for (JsonValue studentValue : studentsArray) {
            JsonObject studentObject = (JsonObject) studentValue;
            int id = studentObject.getInt("id");
            String name = studentObject.getString("name");
            students.add(new Student(id, name));
        }
        return students;
    }

    private void writeJsonObjectToFile(JsonObject jsonObject) {
        try {
            Files.write(Paths.get(FILE_PATH), jsonObject.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getStudentsByName() {
        String studentName = InputUtil.readStudentName();
        JsonObject jsonObject = readJsonObjectFromFile();

        if (jsonObject != null) {
            JsonArray studentsArray = jsonObject.getJsonArray("students");
            System.out.println("Список студентов с именем - " + studentName + ": ");
            for (Student student : parseStudentsFromJson(studentsArray)) {
                if (student.getName().equals(studentName)) {
                    System.out.println("ID: " + student.getId() + ", Name: " + student.getName());
                }
            }
        }
    }

    public void getStudentInfoById() {
        int studentId = InputUtil.readStudentId();
        JsonObject jsonObject = readJsonObjectFromFile();

        if (jsonObject != null) {
            JsonArray studentsArray = jsonObject.getJsonArray("students");
            List<Student> students = parseStudentsFromJson(studentsArray);

            for (Student student : students) {
                if (student.getId() == studentId) {
                    System.out.println("ID: " + student.getId() + ", Name: " + student.getName());
                }
            }
        }
    }

    public void addStudent() {
        String studentName = InputUtil.readStudentName();
        JsonObject jsonObject = readJsonObjectFromFile();

        if (jsonObject != null) {
            JsonArray studentsArray = jsonObject.getJsonArray("students");

            int maxId = 0;
            for (JsonValue studentValue : studentsArray) {
                JsonObject studentObject = (JsonObject) studentValue;
                int id = studentObject.getInt("id");
                if (id > maxId) {
                    maxId = id;
                }
            }

            int newStudentId = maxId + 1;

            JsonObject newStudentObject = Json.createObjectBuilder()
                    .add("id", newStudentId)
                    .add("name", studentName)
                    .build();

            JsonArrayBuilder updatedStudentsArrayBuilder = Json.createArrayBuilder(studentsArray)
                    .add(newStudentObject);

            JsonObject updatedJsonObject = Json.createObjectBuilder(jsonObject)
                    .add("students", updatedStudentsArrayBuilder)
                    .build();

            writeJsonObjectToFile(updatedJsonObject);
            System.out.println("Студент успешно добавлен в файл.");
        }
    }

    public void deleteStudentById() {
        int studentId = InputUtil.readStudentId();
        JsonObject jsonObject = readJsonObjectFromFile();

        if (jsonObject != null) {
            JsonArray studentsArray = jsonObject.getJsonArray("students");

            JsonArrayBuilder updatedStudentsArrayBuilder = Json.createArrayBuilder();

            for (JsonValue studentValue : studentsArray) {
                JsonObject studentObject = (JsonObject) studentValue;
                int id = studentObject.getInt("id");

                if (id != studentId) {
                    updatedStudentsArrayBuilder.add(studentValue);
                }
            }

            JsonObject updatedJsonObject = Json.createObjectBuilder(jsonObject)
                    .add("students", updatedStudentsArrayBuilder)
                    .build();

            writeJsonObjectToFile(updatedJsonObject);
            System.out.println("Студент с ID " + studentId + " успешно удален из файла.");
        }
    }
}
