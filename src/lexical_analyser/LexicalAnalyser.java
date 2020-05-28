package lexical_analyser;

import java.io.File;
import java.util.Scanner;

public class LexicalAnalyser {

	public LexicalAnalyser(String filename) {
		File f1= new File(filename);
		if(f1.createNewFile())
			System.out.println("File created");
		else
			System.out.println("File already exists"); 
		Scanner sc= new Scanner(f1);				
		System.out.println("Content of the file:");
	}

}
