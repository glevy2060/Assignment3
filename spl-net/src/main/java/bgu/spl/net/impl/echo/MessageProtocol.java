package bgu.spl.net.impl.echo;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.Database;
import bgu.spl.net.passiveObjects.Student;

public class MessageProtocol implements MessagingProtocol<String> {
    private Database db = Database.getInstance();
    private Student currStudent = null;

    @Override
    public String process(String msg) {
        //todo check how to handle error message from server to client
        String[] splitMsg = msg.split("\\s+");
        return act(splitMsg);
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private String act(String[] msg) {
        String command = msg[0];
        if (command.equals("ADMINREG")) {
            if (!db.getStudentList().containsKey(msg[1])) {
                db.getStudentList().put(msg[1], new Student(msg[1], msg[2], true));
                return("ACK 1");
            } else
                return("ERROR 1");
        } else if (command.equals("STUDENTREG")) {
            if (!db.getStudentList().containsKey(msg[1])) {
                db.getStudentList().put(msg[1], new Student(msg[1], msg[2], false));
                return("ACK 2");
            } else
                return("ERROR 2");
        } else if (command.equals("LOGIN")) {
            //check if student is registered
            if (db.getStudentList().containsKey(msg[1])) {
                try {
                    db.getStudentList().get(msg[1]).logIn(msg[2]);
                    currStudent = db.getStudentList().get(msg[1]);
                    return("ACK 3");
                } catch (Exception e) {
                    return("ERROR 3");
                }
            } else
                return("ERROR 3");
        } else if (command.equals("LOGOUT")) {
            //check if student is registered
            if (db.getStudentList().containsKey(msg[1])) {
                try {
                    db.getStudentList().get(msg[1]).logout();
                    currStudent = null;
                    return("ACK 4");
                } catch (Exception e) {
                    return("ERROR 4");
                }
            } else
                return("ERROR 4");
        } else if (command.equals("COURSEREG")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]) || !currStudent.isAdmin())
                return("ERROR 5");
            else {
                try {
                    currStudent.registerToCourse(db.getCourseList().get(msg[1]));
                    return("ACK 5");
                } catch (Exception e) {
                    return("ERROR 5");
                }
            }
        } else if (command.equals("KDAMCHECK")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                return("ERROR 6");
            else {
                return("ACK 6");
                //System.out.println(db.getCourseList().get(msg[1]).getKdamCourses());
            }
        } else if (command.equals("COURSESTAT")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !currStudent.isAdmin() || !db.getCourseList().containsKey(msg[1]))
                return("ERROR 7");
            else {
                return("ACK 7");
                //System.out.println(db.getCourseList().get(msg[1]).courseStat());
            }
        } else if (command.equals("STUDENTSTAT")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !currStudent.isAdmin())
                return("ERROR 8");
            else {
                return("ACK 8");
                //System.out.println(db.getStudentList().get(msg[1]).studentStatus());
            }
        } else if (command.equals("ISREGISTERED")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                return("ERROR 9");
            else {
                return("ACK 9");
                //System.out.println(currStudent.isRegistered(db.getCourseList().get(msg[1])));
            }
        } else if (command.equals("UNREGISTER")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                return("ERROR 10");
            else {
                try {
                    currStudent.unregisterToCourse(db.getCourseList().get(msg[1]));
                    return("ACK 10");
                } catch (Exception e) {
                    return("ERROR 10");
                }
            }
        } else if(command.equals("MYCOURSES")){
            if(currStudent == null)
                return("ERROR 11");
            else {
                //System.out.println(currStudent.getRegisteredCourses());
                return("ACK 11");
            }
        }else{
            return("ERROR");
        }
    }

}
