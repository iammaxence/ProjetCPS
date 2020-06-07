package tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;


/**
 * The class EcritureCSV allow 
 * to write in a file the time
 * with some information in order
 * to test the performance of 
 * our project
 * 
 * @author GROUP LAMA
 *
 */
public class EcritureCSV {
	
	/**
	 * Print a time in a file with some information
	 * @param name of the caller
	 * @param moment when it's call
	 * @param temps : time epoch unix
	 * @throws IOException
	 */
	public synchronized static void Calculdutemps(String name,String moment, long temps) throws IOException {
		File myfile = new File("./TestPerformances");
		if(!myfile.exists())
			myfile.createNewFile(); // if file already exists will do nothing 
		final PrintStream output = new PrintStream(new FileOutputStream(myfile,true)); //Le fichier contenant les temps pc (publisher -> courtier) et (courtier -> sub)
		output.println(name+ " "+moment+" "+"temps = "+temps);
		output.close();
	
	}
}
