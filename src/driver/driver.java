package driver;

import java.nio.file.*;
import java.util.*;


import lexical_analyser.LexicalAnalyser;
import parser.Parser;

public class driver {

	public driver() {
		// TODO Auto-generated constructor stub
	}

	public static void main(String[] args) {
		driver obj = new driver();
		obj.menu();
	}

	public void menu() {
		while(true) {
			System.out.println("\nMenu\n1.Enter program\n2.Read from file\nEnter choice:");
			Scanner sc= new Scanner(System.in);
			String choice = sc.nextLine();
			if(choice.equals("1")) {

			}else if(choice.equals("2")) {
				while(true) {
					System.out.println("Enter file name:");
					String filename= sc.nextLine();
					String path="Input\\"+filename+".txt";
					Path p = Paths.get(path);
					boolean exists = Files.exists(p);
					boolean notExists = Files.notExists(p);
					if (notExists) {
						System.err.println("File doesn't exist.");
						System.out.println("Enter again:");
						continue;
					}
					LexicalAnalyser lex = new LexicalAnalyser();
					lex.Tokenise(path);
					Parser p1 =new Parser();
					p1.startParsing();
					break;
				}
			}
			else {
				System.err.println("Invalid choice.");
				continue;
			}
			break;
		}
	}

}
