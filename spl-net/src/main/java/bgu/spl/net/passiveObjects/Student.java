package bgu.spl.net.passiveObjects;

import java.util.ArrayList;
import java.util.List;

public class Student {
    private String user;
    private String password;
    private List<Course> registeredCourses;
    private boolean isAdmin;
    private boolean isLoggedIn;

    //todo: check coupling (student, course);
    public Student(String user, String password, boolean isAdmin){
        this.user = user;
        this.password = password;
        this.isAdmin = isAdmin;
        this.registeredCourses = new ArrayList<>();
        this.isLoggedIn = false;
    }

    //opcode: 3
    //todo throw the error opcode
    public void logIn(String password) throws Exception{
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
    public void registerToCourse(Course course) throws Exception{
        List<String> kdamCourse = course.getKdamCourses();
        if(!finishedAllKdam(kdamCourse)) throw new Exception("missing kdam courses");
        course.register(this); //throws exception
        registeredCourses.add(course);
    }

    //opcode: 8
    // todo: print course num and not course object
    public String studentStatus(){
        String toReturn = "\n" + "Student: " + user +"\n"+ "Courses: [";
        for(Course c: registeredCourses)
            toReturn = toReturn + c + ",";
        if(registeredCourses.size()>0)
            toReturn = toReturn.substring(0, toReturn.length()-1);
        toReturn = toReturn + "]";
        return toReturn;
    }

    //opcode: 9
    //todo: check the course unmber is valid before calling this method;
    public String isRegistered(Course course){
        if(registeredCourses.contains(course))
            return "REGISTERED";
        else
            return "NOT REGISTERED";
    }

    //opcode: 10
    public void unregisterToCourse(Course course) throws Exception{
        if(!registeredCourses.contains(course)) throw new Exception("not registered to this course already");
        course.unregister(this); //throws exception
        registeredCourses.remove(course);
    }



    private boolean finishedAllKdam(List<String> kdamCourse){
        for(String course: kdamCourse){
            if(!registeredCourses.contains(course))
                return false;
        }
        return true;
    }

    public String getUser() {
        return user;
    }

    //returning as: [num,num,num]
    public String getRegisteredCourses() {
        String s = "[";
        for (Course c: registeredCourses)
            s = s + c.getCourseNum()+",";
        if(registeredCourses.size()>0)
            s = s.substring(0, s.length()-1);
        return s +"]";
    }

    public boolean isAdmin() {
        return isAdmin;
    }

}
