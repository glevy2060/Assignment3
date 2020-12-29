package bgu.spl.net.impl.echo;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.impl.Database;
import bgu.spl.net.passiveObjects.ServerMessage;
import bgu.spl.net.passiveObjects.Student;

public class MessageProtocol implements MessagingProtocol<String> {
    private Database db = Database.getInstance();
    private Student currStudent = null;

    @Override
    public String process(String msg) {
        //todo check how to handle error message from server to client
        String[] splitMsg = msg.split("\\s+");
        return "";
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private ServerMessage act(String[] msg) {
        ServerMessage m = null;
        String command = msg[0];
        if (command.equals("ADMINREG")) {
            if (!db.getStudentList().containsKey(msg[1])) {
                db.getStudentList().put(msg[1], new Student(msg[1], msg[2], true));
                m = new ServerMessage(12, 1, "");
            } else
                m = new ServerMessage(13, 1, "");
        } else if (command.equals("STUDENTREG")) {
            if (!db.getStudentList().containsKey(msg[1])) {
                db.getStudentList().put(msg[1], new Student(msg[1], msg[2], false));
                m = new ServerMessage(12, 2, "");
            } else
                m = new ServerMessage(13, 2, "");
        } else if (command.equals("LOGIN")) {
            //check if student is registered
            if (db.getStudentList().containsKey(msg[1])) {
                try {
                    db.getStudentList().get(msg[1]).logIn(msg[2]);
                    currStudent = db.getStudentList().get(msg[1]);
                    m = new ServerMessage(12, 3, "");
                } catch (Exception e) {
                    m = new ServerMessage(13, 3, "");
                }
            } else
                m = new ServerMessage(13, 3, "");
        } else if (command.equals("LOGOUT")) {
            //check if student is registered
            if (db.getStudentList().containsKey(msg[1])) {
                try {
                    db.getStudentList().get(msg[1]).logout();
                    currStudent = null;
                    m = new ServerMessage(12, 4, "");
                } catch (Exception e) {
                    m = new ServerMessage(13, 4, "");
                }
            } else
                m = new ServerMessage(13, 4, "");
        } else if (command.equals("COURSEREG")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]) || !currStudent.isAdmin())
                m = new ServerMessage(13, 5, "");
            else {
                try {
                    currStudent.registerToCourse(db.getCourseList().get(msg[1]));
                    m = new ServerMessage(12, 5, "");
                } catch (Exception e) {
                    m = new ServerMessage(13, 5, "");
                }
            }
        } else if (command.equals("KDAMCHECK")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 6, "");
            else {
                m = new ServerMessage(12, 6, "");
            }
        } else if (command.equals("COURSESTAT")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !currStudent.isAdmin() || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 7, "");
            else {
                m = new ServerMessage(12, 7, db.getCourseList().get(msg[1]).courseStat());
            }
        } else if (command.equals("STUDENTSTAT")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !currStudent.isAdmin())
                m = new ServerMessage(13, 8, "");
            else {
                m = new ServerMessage(12, 8, db.getStudentList().get(msg[1]).studentStatus());
            }
        } else if (command.equals("ISREGISTERED")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 9, "");
            else {
                m = new ServerMessage(12, 9, currStudent.isRegistered(db.getCourseList().get(msg[1])));
            }
        } else if (command.equals("UNREGISTER")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 10, "");
            else {
                try {
                    currStudent.unregisterToCourse(db.getCourseList().get(msg[1]));
                    m= new ServerMessage(12, 10, "");
                } catch (Exception e) {
                    m= new ServerMessage(13, 10, "");
                }
            }
        } else if(command.equals("MYCOURSES")){
            if(currStudent == null)
                m= new ServerMessage(13, 11, "");
            else {
                m = new ServerMessage(12, 11, "currStudent.getRegisteredCourses()"); //todo
            }
        }else{
            m= new ServerMessage(13, -1, "");
        }

        return m;
    }


}
