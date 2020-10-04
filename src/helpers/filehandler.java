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
	public static void makeTable(String data[][], String column[]) {
			JFrame f;    
		    f=new JFrame();    
//		    String data1[][]={ {"101","Amit","670000"},    
//		                          {"102","Jai","780000"},    
//		                          {"101","Sachin","700000"}};    
//		    String column1[]={"ID","NAME","SALARY"};         
		    JTable jt=new JTable(data,column);    
		    jt.setBounds(30,40,200,300);          
		    JScrollPane sp=new JScrollPane(jt);    
		    f.add(sp);          
		    f.setSize(300,400);    
		    f.setVisible(true);    
	}
//	public static void finishTable()
}
