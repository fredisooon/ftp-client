package main.java.com.fyodor.json;

import main.java.com.fyodor.model.Student;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONParser {

    private static final String FILE_PATH = "/Users/fyodor/Desktop/test/fpt-client/src/main/resources/static/students2.json";

//    public static void main(String[] args) {
//        try {
//            String json = new String(Files.readAllBytes(Paths.get(FILE_PATH)));
//
//            // Получение списка студентов по имени
//            List<Student> students = getStudentsByName(json, "Александр");
//            System.out.println("Students with name 'Александр': " + students);
//
//            // Получение информации о студенте по id
//            Student student = getStudentById(json, 2);
//            System.out.println("Student with id 2: " + student);
//
//            // Добавление нового студента
//            Student newStudent = new Student(generateUniqueId(json), "Новый студент");
//            json = addStudent(json, newStudent);
//            System.out.println("JSON after adding new student:\n" + json);
//
//            // Удаление студента по id
//            json = removeStudentById(json, 1);
//            System.out.println("JSON after removing student with id 1:\n" + json);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

    // Получение списка студентов по имени
    private static List<Student> getStudentsByName(String json, String name) {
        List<Student> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\{\"id\": (\\d+), \"name\": \"" + name + "\"}");
        Matcher matcher = pattern.matcher(json);

        while (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            result.add(new Student(id, name));
        }

        return result;
    }

    // Получение информации о студенте по id
    public static Student getStudentById(int id) {

        String fileContent = "";
        try {
            byte[] fileBytes = Files.readAllBytes(Paths.get(FILE_PATH));
            fileContent = new String(fileBytes, StandardCharsets.UTF_8); // Указываем кодировку
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonReader jsonReader = Json.createReader(new StringReader(fileContent));
        JsonObject jsonObject = jsonReader.readObject();
        jsonReader.close();

        JsonArray studentsArray = jsonObject.getJsonArray("students");
        List<Student> students = new ArrayList<>();
        for (int i = 0; i < studentsArray.size(); i++) {
            JsonObject studentObject = studentsArray.getJsonObject(i);
            int id1 = studentObject.getInt("id");
            String name = studentObject.getString("name");
            students.add(new Student(id1, name));
        }

        // Теперь у вас есть список студентов
        for (Student student : students) {
            if (student.getId() == id) {
                System.out.println("ID: " + student.getId() + ", Name: " + student.getName());
                return student;
            }
        }
        return null;
    }

    // Добавление студента в JSON
    private static String addStudent(String json, Student student) {
        String newStudent = String.format("{\"id\": %d, \"name\": \"%s\"}", student.getId(), student.getName());
        int insertIndex = json.lastIndexOf(']');
        return json.substring(0, insertIndex) + "," + newStudent + json.substring(insertIndex);
    }

    // Удаление студента из JSON по id
    private static String removeStudentById(String json, int id) {
        return json.replaceAll("\\{\"id\": " + id + ", \"name\": \"\\w+\"},?", "");
    }

    // Генерация уникального id для нового студента
    private static int generateUniqueId(String json) {
        Pattern pattern = Pattern.compile("\"id\": (\\d+)");
        Matcher matcher = pattern.matcher(json);

        int maxId = 0;
        while (matcher.find()) {
            int id = Integer.parseInt(matcher.group(1));
            if (id > maxId) {
                maxId = id;
            }
        }

        return maxId + 1;
    }
}
