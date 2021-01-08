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
        String command = msg.substring(0,2);
        String details = msg.substring(2);
        msg = command + " " + details;
        return act(msg.split(" "));
    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }

    private String act(String[] msg) {
        ServerMessage m = null;
        String command = msg[0];
        if (command.equals("01")) {
            if(currStudent == null){
                Student res = db.getStudentList().putIfAbsent(msg[1], new Student(msg[1], msg[2], true));
                if (res == null)
                    m = new ServerMessage(12, 1, "");
                else
                    m = new ServerMessage(13, 1, "");
            } else
                m = new ServerMessage(13, 1, "");
        } else if (command.equals("02")) {
            if(currStudent == null){
                Student res = db.getStudentList().putIfAbsent(msg[1], new Student(msg[1], msg[2], false));
                if (res == null && currStudent == null)
                    m = new ServerMessage(12, 2, "");
                else
                    m = new ServerMessage(13, 2, "");
            } else
                m = new ServerMessage(13, 2, "");
        } else if (command.equals("03")) {
            //check if student is registered
            if (db.getStudentList().containsKey(msg[1]) && currStudent == null) {
                try {
                    db.getStudentList().get(msg[1]).logIn(msg[2]);
                    currStudent = db.getStudentList().get(msg[1]);
                    m = new ServerMessage(12, 3, "");
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    m = new ServerMessage(13, 3, "");
                }
            } else
                m = new ServerMessage(13, 3, "");
        } else if (command.equals("04")) {
            //check if student is registered
            if (currStudent!=null) {
                try {
                    currStudent.logout();
                    currStudent = null;
                    m = new ServerMessage(12, 4, "");
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    m = new ServerMessage(13, 4, "");
                }
            } else
                m = new ServerMessage(13, 4, "");
        } else if (command.equals("05")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]) || currStudent.isAdmin())
                m = new ServerMessage(13, 5, "");
            else {
                try {
                    currStudent.registerToCourse(db.getCourseList().get(msg[1]));
                    m = new ServerMessage(12, 5, "");
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    m = new ServerMessage(13, 5, "");
                }
            }
        } else if (command.equals("06")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 6, "");
            else {
                m = new ServerMessage(12, 6, db.getCourseList().get(msg[1]).getKdamCoursesAsString());
            }
        } else if (command.equals("07")) {
            //todo check if unlogged user can do "kdamCourse"
            //!currStudent.isAdmin() ||
            if (currStudent == null || !currStudent.isAdmin() ||  !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 7, "");
            else {
                m = new ServerMessage(12, 7, db.getCourseList().get(msg[1]).courseStat());
            }
        } else if (command.equals("08")) {
            //todo check if unlogged user can do "kdamCourse"
            if (currStudent == null || !currStudent.isAdmin() ||
                     !db.getStudentList().containsKey(msg[1])  || db.getStudentList().get(msg[1]).isAdmin())
                m = new ServerMessage(13, 8, "");
            else {
                m = new ServerMessage(12, 8, db.getStudentList().get(msg[1]).studentStatus());
            }
        } else if (command.equals("09")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 9, "");
            else {
                m = new ServerMessage(12, 9, currStudent.isRegistered(db.getCourseList().get(msg[1])));
            }
        } else if (command.equals("10")) {
            if (currStudent == null || !db.getCourseList().containsKey(msg[1]))
                m = new ServerMessage(13, 10, "");
            else {
                try {
                    currStudent.unregisterToCourse(db.getCourseList().get(msg[1]));
                    m= new ServerMessage(12, 10, "");
                } catch (Exception e) {
                    //System.out.println(e.getMessage());
                    m= new ServerMessage(13, 10, "");
                }
            }
        } else if(command.equals("11")){
            if(currStudent == null)
                m= new ServerMessage(13, 11, "");
            else {

                m = new ServerMessage(12, 11, currStudent.getRegisteredCourses());
            }
        }else{
            m= new ServerMessage(13, -1, "");
        }
        return m.toString();
    }


}
