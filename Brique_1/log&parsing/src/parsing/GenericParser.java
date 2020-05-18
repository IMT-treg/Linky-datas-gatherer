package parsing;

import java.text.SimpleDateFormat;
import java.util.HashMap;
/*
 * This abstract class allow to create a frame for the diffent parsers.
 */
public abstract class GenericParser {
	// ATTRIBUTES
	public static SimpleDateFormat formatDateLecture = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	
	// CONSTRUCTOR
	public GenericParser() {
		super();
	}
	// METHODS
	public void readDatas() {
	}
}
