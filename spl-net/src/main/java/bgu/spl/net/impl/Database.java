package bgu.spl.net.impl;
import bgu.spl.net.passiveObjects.Course;
import bgu.spl.net.passiveObjects.Student;

import java.util.*;

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
		studentList = new HashMap<>();
		courseList = new HashMap<>();
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
	boolean initialize(String coursesFilePath) {
		// TODO: implement
		return false;
	}

	public Map<String, Student> getStudentList() {
		return studentList;
	}


	public Map<String,Course> getCourseList() {
		return courseList;
	}
}
