package lexical_analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class LexicalAnalyser {

	private  List<token> tokenList = new ArrayList<token>(); 
	private  List<token> errorList = new ArrayList<token>(); 
	private int currentIndex;
	private int skipUntil=-1;
	private String currentSequence;

	public LexicalAnalyser(String path) {
		loadfile(path);
	}

	private void loadfile(String path) {
		File f1= new File(path);
		try {
			Scanner sc= new Scanner(f1);
			int linenum=0;
			while(sc.hasNextLine())  
			{  
				String line=sc.nextLine(); 
				scanLine(line,++linenum);
			}  
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}	
		for(token item: tokenList)
			System.out.println(item.getToken());
		for(token item: errorList)
			System.out.println(item.getToken());
		System.out.println("the end");
	}

	private void scanLine(String line, int linenum) {
		List<String> currentLine= Arrays.asList(line.split("\\s+"));
		for(String item : currentLine) {
			System.out.println("\n"+item);
			skipUntil=-1;
			for(int i=0;i<item.length();i++) {
				currentIndex= i;
				System.out.println("current:"+ currentIndex+" skip:"+skipUntil+", char="+item.charAt(i));
				currentSequence= item;
				if(i<skipUntil) {
					System.out.println("skipped!"+skipUntil+" char:"+item.charAt(i)+"\n");
					continue;
				}
				else
					state0(item.charAt(i),linenum);
			}
		}
	}

	private String peekElement() {
		if( currentIndex+1 < currentSequence.length()) {
			int nextIndex= currentIndex+1;
			System.out.println("nextIndex:"+nextIndex);
			return(Character.toString(currentSequence.charAt(nextIndex)));
		}
		return "";
	}

	private void state0(char item,int linenum) {
		switch(item) {
		case '}': finalState("Right_Braces","}",linenum); break;
		case '{': finalState("Left_Braces","{",linenum);break;
		case ')': finalState("Right_Paranthesis",")",linenum); break;
		case '(':finalState("Left_Paranthesis","(",linenum); break;
		case '[':finalState("Left_Bracket","[",linenum); break;
		case ']':finalState("Right_Bracket","]",linenum); break;
		case ';':finalState("Semicolon",";",linenum); break;
		case ',':finalState("Comma",",",linenum); break;
		case '.':finalState("Dot_operator",".",linenum); break; 
		case '!':finalState("Inverse","!",linenum); break;
		case '+':finalState("Addition_Operator","+",linenum); break;
		case '-':finalState("Subtraction_Operator","-",linenum); break;
		case '<': stateLEQ("<",linenum);break;
		case '>': stateGEQ(">",linenum);break;
		case '=': statEQ(">",linenum);break;
		case '/': break;
		case '*': break;
		case '&': stateAnd(">",linenum);break;
		case '|': break;
		case ':': break;
		}
		String buffer ="";
		if(Character.isAlphabetic(item)) {
			buffer= stateChar(Character.toString(item),item,linenum);
			if(checkKeyword(buffer))
				finalState("KEYWORD",buffer,linenum);
			else
				finalState("IDENTIFIER",buffer,linenum);
		}
	}

	private boolean checkKeyword(String buffer) {
		if(buffer.equals("main")||buffer.equals("if")||buffer.equals("then")
				||buffer.equals("else")||buffer.equals("return")||buffer.equals("write")
				||buffer.equals("read")||buffer.equals("class")||buffer.equals("int")
				||buffer.equals("float")||buffer.equals("for"))
			return true;
		else
			return false;
	}

	private String stateChar(String buffer, char item, int linenum) {
		if(peekElement()!="") {
			char next= peekElement().charAt(0);
			if(Character.isAlphabetic(next)) {
				buffer+=next;
				currentIndex++;
				skipUntil=currentIndex+1;
				System.out.println("*************"+buffer);
				buffer= stateChar(buffer,next,linenum);
			}
		}
		return buffer;
	}

	private void finalState(String tokentype, String tokenname, int linenum) {
		token temp= new token(tokentype,tokenname,linenum);
		System.out.println("entered"+temp.getToken());
		tokenList.add(temp);
	} 

	private void stateLEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Less_Or_Equal_To","<=",linenum);
			skipUntil=currentIndex+1;
		}
		else
			finalState("Less_Than_Operator","<",linenum);
	}
	private void stateGEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Greater_Than_Or_Equal_To",">=",linenum);
			skipUntil=currentIndex+1;
		}
		else
			finalState("Greater_Than_Operator",">",linenum);
	}
	private void statEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Equality_Operator","==",linenum);
			skipUntil=currentIndex+1;
		}
		else
			finalState("Assignment_Operator","=",linenum);
	}
	private void stateAnd(String item, int linenum) {
		String next= peekElement();
		if(next.equals("&")) {
			finalState("Logical_And_Operator","&&",linenum);
			skipUntil=currentIndex+1;
		}
		else if(next!="")
			errorState("Invalid token: Remove this symbol to correct the error","&",linenum);
	}

	private void errorState(String errorName, String item, int linenum) {
		token temp= new token(errorName,item,linenum);
		errorList.add(temp);
	}
}
