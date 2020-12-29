package bgu.spl.net.passiveObjects;

import java.util.ArrayList;
import java.util.List;

public class Course {
    private String courseNum;
    private String courseName;
    private List<String> kdamCourses;
    private int numOfMaxStudent;
    private List<Student> listOfStudents;

    public Course(String courseNum, String courseName, List<String> kdamCourses, int numOfMaxStudent, int numOfStudent) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdamCourses = kdamCourses;
        this.numOfMaxStudent = numOfMaxStudent;
        this.listOfStudents = new ArrayList<>();
    }

    public void register(Student student) throws Exception {
        if(numOfMaxStudent == listOfStudents.size()) throw new Exception("course is full");
        listOfStudents.add(student);
    }

    public void unregister(Student student) {
        listOfStudents.remove(student);
    }

    //opcode: 6;
    public List<String> getKdamCourses() {
        return kdamCourses;
    }

    //opcode:7
    public String courseStat(){
        String toReturn =  "Course:(" +courseNum +") "+ courseName +"/n"
                +"Seats Available:" + listOfStudents.size()+"/"+numOfMaxStudent + "/n"+
                "Students Registered:[";
        for (Student s: listOfStudents){
            toReturn = toReturn + s.getUser() +",";
        }
        toReturn = toReturn.substring(0, toReturn.length()-1);
        toReturn = toReturn +"]";
        return toReturn;
    }
}
