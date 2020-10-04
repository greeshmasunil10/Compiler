package helpers;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class contentTable {
	private ArrayList<ArrayList<String>>  rows= new ArrayList<>();
	private ArrayList<String> header= new ArrayList<>();
	private JTable jtable;
	public contentTable() {
	}
	public JTable getTabe() {
		return jtable;
	}
	public void addHeader(ArrayList<String> pheader) {
		this.header=pheader;
	}
	public void addRow(ArrayList<String> row) {
		rows.add(row);
	}
	public void showTable(String tablename) {
		String[][] rows1 = rows.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		Object[] header1=header.toArray();
		JFrame f;    
	    f=new JFrame();    
	    f.setTitle(tablename); 
	    jtable=new JTable(rows1,header1);    
	    jtable.setBounds(100,100,500,800);      
	    jtable.setRowHeight(25);
	    jtable.setRowHeight(3, 40);
	    JScrollPane sp=new JScrollPane(jtable);    
	    f.add(sp);          
	    f.setSize(800,1000);    
	    f.setVisible(true);   
	    filehandler fw= new filehandler("Output/AST.txt");
	    try {
			fw.saveTable(jtable);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}
}
