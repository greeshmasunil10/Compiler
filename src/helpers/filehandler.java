package helpers;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class filehandler {

	public filehandler() {
	}
	public  static void file_writer(String filename, String content) {
		 try {
			 File f = new File("Output/"+filename+".txt");
			 FileWriter fw= new FileWriter(f);
			 fw.write(content);
			 fw.close();
	        } catch (IOException iox) {
	            //do stuff with exception
	            iox.printStackTrace();
	        }
	}
}
