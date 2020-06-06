package tests;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class EcritureCSV {
	
	
	public static void Calculdutemps(String name,String moment, long temps) throws IOException {
		File myfile = new File("./TestPerformances");
		if(!myfile.exists())
			myfile.createNewFile(); // if file already exists will do nothing 
		final PrintStream output = new PrintStream(new FileOutputStream(myfile,true)); //Le fichier contenant les temps pc (publisher -> courtier) et (courtier -> sub)
		output.println(name+ " "+moment+" "+"temps = "+temps);
		output.close();
	
	}
}
