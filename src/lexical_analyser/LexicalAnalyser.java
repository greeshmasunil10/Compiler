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
	private int currentPointer;
	private int endPointer=-1;
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
		System.out.println("No of tokens:"+tokenList.size());
	}

	private void scanLine(String line, int linenum) {
		List<String> currentLine= Arrays.asList(line.split("\\s+"));
		for(String item : currentLine) {
			endPointer=-1;
			for(int i=0;i<item.length();i++) {
				currentPointer= i;
				currentSequence= item;
				if(i<=endPointer) {
					continue;
				}
				else
					state0(item.charAt(i),linenum);
			}
		}
	}

	private String peekElement() {
		if( currentPointer+1 < currentSequence.length()) {
			int nextIndex= currentPointer+1;
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
			if(isKeyword(buffer))
				finalState("KEYWORD",buffer,linenum);
			else
				finalState("IDENTIFIER",buffer,linenum);
		}
		if(Character.isDigit(item)) {
			buffer= stateDigit(Character.toString(item),item,linenum);
			if(buffer.contains("."))
				finalState("FLOAT",buffer,linenum);
			else
				finalState("INTEGER",buffer,linenum);
		}
	}

	private String stateDigit(String buffer, char item, int linenum) {
		if(peekElement()!="") {
			char next= peekElement().charAt(0);
			if(Character.isDigit(next) || next=='.') {
				buffer+=next;
				updatePointer();
				buffer= stateDigit(buffer,next,linenum);
			}
		}
		return buffer;
	}
	
	private String stateChar(String buffer, char item, int linenum) {
		if(peekElement()!="") {
			char next= peekElement().charAt(0);
			if(Character.isAlphabetic(next)) {
				buffer+=next;
				updatePointer();
				buffer= stateChar(buffer,next,linenum);
			}
		}
		return buffer;
	}

	private boolean isKeyword(String buffer) {
		if(buffer.equals("main")||buffer.equals("if")||buffer.equals("then")
				||buffer.equals("else")||buffer.equals("return")||buffer.equals("write")
				||buffer.equals("read")||buffer.equals("class")||buffer.equals("int")
				||buffer.equals("float")||buffer.equals("for"))
			return true;
		else
			return false;
	}

	private void finalState(String tokentype, String tokenname, int linenum) {
		token temp= new token(tokentype,tokenname,linenum);
		tokenList.add(temp);
	} 
	private void errorState(String errorName, String item, int linenum) {
		token temp= new token(errorName,item,linenum);
		errorList.add(temp);
	}

	private void updatePointer() {
		endPointer=currentPointer+1;
		currentPointer++;
	}

	private void stateLEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Less_Or_Equal_To","<=",linenum);
			updatePointer();
		}
		else
			finalState("Less_Than_Operator","<",linenum);
	}
	private void stateGEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Greater_Than_Or_Equal_To",">=",linenum);
			updatePointer();
		}
		else
			finalState("Greater_Than_Operator",">",linenum);
	}
	private void statEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState("Equality_Operator","==",linenum);
			updatePointer();
		}
		else
			finalState("Assignment_Operator","=",linenum);
	}
	private void stateAnd(String item, int linenum) {
		String next= peekElement();
		if(next.equals("&")) {
			finalState("Logical_And_Operator","&&",linenum);
			updatePointer();
		}
		else if(next!="")
			errorState("Invalid token: Remove this symbol to correct the error","&",linenum);
	}
}
