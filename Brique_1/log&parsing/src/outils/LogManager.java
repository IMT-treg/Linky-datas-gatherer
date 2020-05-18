package outils;

import java.text.SimpleDateFormat;
import java.util.Date;
/*
 * This Class allow to display the date of every comment made in the app
 */
public class LogManager 
{
	private static final SimpleDateFormat formatDate = new SimpleDateFormat("YYYY/MM/dd HH:mm:ss");
	
	public static void log(String msg)
	{
		Date heureLog = new Date();
		
		System.out.println("["+formatDate.format(heureLog)+"]["+Thread.currentThread().getClass().getSimpleName()+"]"+String.valueOf(msg));
				
	}
	
	
}