package bgu.spl.net.passiveObjects;

import bgu.spl.net.impl.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Course {
    private String courseNum;
    private String courseName;
    private List<String> kdamCourses;
    private int numOfMaxStudent;
    private List<Student> listOfStudents;
    private Database db;
    private int index;

    public Course(String courseNum, String courseName, List<String> kdamCourses, int numOfMaxStudent, int index) {
        this.courseNum = courseNum;
        this.courseName = courseName;
        this.kdamCourses = kdamCourses;
        this.numOfMaxStudent = numOfMaxStudent;
        this.listOfStudents = new ArrayList<>();
        this.index = index;
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
        int seatsAvailable = numOfMaxStudent - listOfStudents.size();
        String toReturn =  "\nCourse: (" +courseNum +") "+ courseName +"\n"
                +"Seats Available: " +seatsAvailable +"/"+numOfMaxStudent + "\n"+
                "Students Registered: [";
        List<String> studentsAsStrings=new ArrayList<>();
        for (Student s: listOfStudents){
           studentsAsStrings.add(s.getUser());
        }
        studentsAsStrings.sort(Comparator.comparing(String::toString));
        for(String s : studentsAsStrings){
            toReturn += s +",";
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
        sort();
        for(String c:  kdamCourses)
            toReturn += c + ",";

        if(kdamCourses.size() > 0)
            toReturn = toReturn.substring(0, toReturn.length()-1);
        return toReturn+"]";

    }

    private void sort(){
        db = Database.getInstance();
        Collections.sort(kdamCourses, new Comparator<String>() {
            @Override
            public int compare(String c1, String c2) {
                return db.getCourseList().get(c1).getIndex() < db.getCourseList().get(c2).getIndex() ? -1: db.getCourseList().get(c1).getIndex() == db.getCourseList().get(c2).getIndex() ? 0: 1;
            }
        });
    }

    public int getIndex() {
        return index;
    }
}
