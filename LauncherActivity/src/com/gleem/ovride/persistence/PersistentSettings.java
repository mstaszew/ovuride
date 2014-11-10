package com.gleem.ovride.persistence;


/** this class will be implemented as a singleton, it will have getters and setters for
 * the username and password, mobile operator, etc. Settings will be rewritten on setXYZ() call
 * to the SD memory
 * 
 * @author Michael Staszewski
 */
//TODO: Migrate all persistent constants and variables from 'Global Constants'
public class PersistentSettings {
	final String FILENAME = "settings.json";
	String bell_username  = "ANNBALDERAMA";
	String bell_pass = "bellx2573";

	// FileOutputStream fos = android.util.openFileOutput(FILENAME, Context.MODE_PRIVATE);
	// fos.write(string.getBytes());
	// fos.close();
}
