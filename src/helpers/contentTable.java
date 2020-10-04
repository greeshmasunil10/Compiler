package helpers;

import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;

public class contentTable {
	ArrayList<ArrayList<String>>  rows= new ArrayList<>();
	ArrayList header= new ArrayList<>();
	public contentTable() {
		// TODO Auto-generated constructor stub
	}
	
	public void addHeader(ArrayList<String> pheader) {
		this.header=pheader;
	}
	public void addRow(ArrayList<String> row) {
		rows.add(row);
	}
	public void makeTable(String tablename) {
		String[][] rows1 = rows.stream().map(u -> u.toArray(new String[0])).toArray(String[][]::new);
		Object[] header1=header.toArray();
		JFrame f;    
	    f=new JFrame();    
	    f.setTitle(tablename); 
	    JTable jt=new JTable(rows1,header1);    
	    jt.setBounds(100,100,500,800);      
	    jt.setRowHeight(25);
	    jt.setRowHeight(3, 40);
	    JScrollPane sp=new JScrollPane(jt);    
	    f.add(sp);          
	    f.setSize(800,1000);    
	    f.setVisible(true);    
}
}
