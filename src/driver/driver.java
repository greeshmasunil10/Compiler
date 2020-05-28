package driver;

import java.nio.file.*;
import java.util.*;

public class driver {

	public driver() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		driver obj = new driver();
		obj.menu();
	}
	
	public void menu() {
		System.out.println("Menu\n1.Enter program\n2.Read from file\nEnter choice:");
		Scanner sc= new Scanner(System.in);
		String choice = sc.nextLine();
		if(choice.equals("1")) {
			
		}else if(choice.equals("2")) {
			System.out.println("Enter file name:");
			String filename= sc.nextLine();
			Path p = Paths.get("Resources\\"+filename+".txt");
			boolean exists = Files.exists(p);
			boolean notExists = Files.notExists(p);
			if (exists) {
			    System.out.println("File exists!");
			} else if (notExists) {
			    System.out.println("File doesn't exist!");
			}
		}
	}

}
