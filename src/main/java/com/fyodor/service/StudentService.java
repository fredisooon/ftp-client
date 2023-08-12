package main.java.com.fyodor.service;

import main.java.com.fyodor.json.JSONParser;
import main.java.com.fyodor.model.Student;
import main.java.com.fyodor.util.InputUtil;
import javax.json.*;
public class StudentService {
    public void getStudentByName() {
        String studentName = InputUtil.readStudentName();
        System.out.println("Student - " + studentName);
    }
    public void getStudentInfoById() {
        int studentId = InputUtil.readStudentId();
        System.out.println("Info about student - " + JSONParser.getStudentById(studentId));
    }
    public void addStudent() {
        Student student = new Student(1, "NEW STUDENT");
        System.out.println(student.toString());
    }
    public void deleteStudentById() {
        long studentId = InputUtil.readStudentId();
        System.out.println("Student with id " + studentId + " is deleted");
    }
}
