package bgu.spl.net.impl;
import bgu.spl.net.passiveObjects.Course;
import bgu.spl.net.passiveObjects.Student;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Passive object representing the bgu.spl.net.impl.Database where all courses and users are stored.
 * <p>
 * This class must be implemented safely as a thread-safe singleton.
 * You must not alter any of the given public methods of this class.
 * <p>
 * You can add private fields and methods to this class as you see fit.
 */
public class Database {
	Map<String, Student> studentList;
	Map<String ,Course> courseList;

	private static class singeltonHolder{
		private static Database instance = new Database();
	}

	//to prevent user from creating new bgu.spl.net.impl.Database
	private Database() {
		studentList = new ConcurrentHashMap<>();
		courseList = new ConcurrentHashMap<>();
		initialize("./Courses.txt");
	}

	/**
	 * Retrieves the single instance of this class.
	 */
	public static Database getInstance() {
		return singeltonHolder.instance;
	}
	
	/**
	 * loades the courses from the file path specified 
	 * into the bgu.spl.net.impl.Database, returns true if successful.
	 */
	public boolean initialize(String coursesFilePath) { //todo check public
		Course newCourse = null;
		try {
			File courses = new File(coursesFilePath);
			Scanner myReader = new Scanner(courses);
			int index = 0;
			while (myReader.hasNextLine()){
				String data = myReader.nextLine();
				String[] c = data.split("\\|");
				if(c[2].length() == 2){
					newCourse = new Course(c[0], c[1], new ArrayList<>(), Integer.parseInt(c[3]), index);
				} else{
					c[2] = c[2].substring(1, c[2].length()-1);
					String[] kdamCourse = c[2].split(",");
					newCourse = new Course(c[0], c[1], Arrays.asList(kdamCourse.clone()), Integer.parseInt(c[3]), index);
				}
				courseList.put(c[0], newCourse);
				index ++;
			}
			myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("file not found");
			return false;
		}

		for(Map.Entry<String, Course> c : courseList.entrySet())
			c.getValue().sort();
		return true;
	}

	public Map<String, Student> getStudentList() {
		return studentList;
	}


	public Map<String,Course> getCourseList() {
		return courseList;
	}

}
