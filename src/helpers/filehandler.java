package helpers;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import javax.swing.*;    

public class filehandler {
	public BufferedWriter bw;
	private File f;
	public filehandler(String pathname) {
		try {
			 f= new File(pathname);
			f.createNewFile();
			FileOutputStream fos2 = new FileOutputStream(f);
			bw = new BufferedWriter(new OutputStreamWriter(fos2));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void writeString(String stringData) {
		try {
			bw.write(stringData);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

	public void writeLine(String lineData) {
		try {
			bw.write(lineData);
			bw.newLine();
		} catch (IOException e) {			e.printStackTrace();
		}
	}
	public  static void file_writer(String filename, String content) {
		 try {
			 File f = new File("Output/"+filename+".txt");
			 FileWriter fw= new FileWriter(f);
			 fw.write(content);
			 fw.close();
	        } catch (IOException iox) {
	            iox.printStackTrace();
	        }
	}
	public void saveTable(JTable table)throws Exception
	{
		System.out.println(table);
	  for(int i = 0 ; i < table.getColumnCount() ; i++)
	  {
	    bw.write(table.getColumnName(i));
	    bw.write("\t\t\t\t");
	  }

	  for (int i = 0 ; i < table.getRowCount(); i++)
	  {
	    bw.newLine();
	    for(int j = 0 ; j < table.getColumnCount();j++)
	    {
	      bw.write((String)(table.getValueAt(i,j)));
	      bw.write("\t\t\t\t");;
	    }
	  }
	  bw.close();
	}	

}
