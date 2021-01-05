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

    public synchronized void register(Student student) throws Exception {
        if(numOfMaxStudent == listOfStudents.size()) throw new Exception("course is full");
        listOfStudents.add(student);
    }

    public synchronized void unregister(Student student) {
        listOfStudents.remove(student);
    }

    //opcode: 6;
    public List<String> getKdamCourses() {
        return kdamCourses;
    }

    //opcode:7
    public synchronized String courseStat(){
        String toReturn =  "\nCourse:(" +courseNum +") "+ courseName +"\n"
                +"Seats Available:" + listOfStudents.size()+"/"+numOfMaxStudent + "\n"+
                "Students Registered:[";
        for (Student s: listOfStudents){
            toReturn = toReturn + s.getUser() +",";
        }
        if(listOfStudents.size() > 0)
            toReturn = toReturn.substring(0, toReturn.length()-1);
        toReturn = toReturn +"]";
        return toReturn;
    }

    public String getCourseNum() {
        return courseNum;
    }

    public String getKdamCoursesAsString(){
        String toReturn = "\n[";
        for(String s: kdamCourses){
            toReturn += s+",";
        }
        if(kdamCourses.size() > 0)
            toReturn = toReturn.substring(0, toReturn.length()-1);
        return toReturn+"]";

    }
}
