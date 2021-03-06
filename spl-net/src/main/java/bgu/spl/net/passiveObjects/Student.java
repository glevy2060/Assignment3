package bgu.spl.net.passiveObjects;

import bgu.spl.net.impl.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Student {
    private String user;
    private String password;
    private List<Course> registeredCourses;
    private boolean isAdmin;
    private boolean isLoggedIn;
    private Database db;

    //todo: check coupling (student, course);
    public Student(String user, String password, boolean isAdmin){
        this.user = user;
        this.password = password;
        this.isAdmin = isAdmin;
        this.registeredCourses = new ArrayList<>();
        this.isLoggedIn = false;
        this.db = Database.getInstance();
    }

    //opcode: 3
    //todo throw the error opcode
    public synchronized void logIn(String password) throws Exception{
        if(!this.password.equals(password)) throw new Exception("incorrect password");
        if(isLoggedIn) throw new Exception("user already logged in");
        isLoggedIn = true;
    }

    //opcode: 4
    //todo throw the error opcode
    public void logout() throws Exception{
        if(!isLoggedIn) throw new Exception("user is already logged out");
        isLoggedIn = false;
    }

    //opcode: 5
    //todo add course num check before calling this function
    public synchronized void registerToCourse(Course course) throws Exception{
        if(registeredCourses.contains(course)) throw new Exception("student already registered to this course");
        List<String> kdamCourse = course.getKdamCourses();
        if(!finishedAllKdam(kdamCourse)) throw new Exception("missing kdam courses");
        course.register(this); //throws exception
        registeredCourses.add(course);
    }

    //opcode: 8
    public synchronized String studentStatus(){
        String toReturn = "\n" + "Student: " + user +"\n"+ "Courses: [";
        sort();
        for(Course c : registeredCourses)
            toReturn = toReturn + c.getCourseNum() + ",";
        if(registeredCourses.size()>0)
            toReturn = toReturn.substring(0, toReturn.length()-1);
        toReturn = toReturn + "]";
        return toReturn;
    }

    //opcode: 9
    //todo: check the course unmber is valid before calling this method;
    public String isRegistered(Course course){
        if(registeredCourses.contains(course))
            return "\nREGISTERED";
        else
            return "\nNOT REGISTERED";
    }

    //opcode: 10
    public synchronized void unregisterToCourse(Course course) throws Exception{
        if(!registeredCourses.contains(course)) throw new Exception("not registered to this course already");
        course.unregister(this); //throws exception
        registeredCourses.remove(course);
    }



    private synchronized boolean finishedAllKdam(List<String> kdamCourse){
        List <String> regNumCourses = new ArrayList<>();
        for(Course c : registeredCourses){
            regNumCourses.add(c.getCourseNum());
        }

        if(kdamCourse.size()>0){
            for(String course: kdamCourse){
                if(!regNumCourses.contains(course))
                    return false;
            }
        }
        return true;
    }

    public String getUser() {
        return user;
    }

    //returning as: [num,num,num]
    public String getRegisteredCourses() {
        String s = "\n[";
        sort();
        for (Course c: registeredCourses)
            s = s + c.getCourseNum()+",";
        if(registeredCourses.size()>0)
            s = s.substring(0, s.length()-1);
        return s +"]";
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    private synchronized void sort(){
        Collections.sort(registeredCourses, new Comparator<Course>() {
            @Override
            public int compare(Course c1, Course c2) {
                return c1.getIndex() < c2.getIndex() ? -1: c1.getIndex() == c2.getIndex() ? 0: 1;
            }
        });
    }

}
