package lexical_analyser;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import helpers.filehandler;


public class LexicalAnalyser {

	private static  List<Token> tokenList = new ArrayList<Token>(); 
	private  List<Token> invalidTokens = new ArrayList<Token>(); 
	private int currentPointer;
	private int endPointer=-1;
	private String currentSequence;
	String output ="";

	public void Tokenise(String path) {
		loadfile(path);
		printTokens();
	}
	
	public static List<Token> getTokens(){
		return tokenList;
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
	}

	private void printTokens() {
		output+="\nLEXICAL ANALYSER"+"\n";
		output+="------------------------"+"\n";
		output+="\nERRORS"+"\n";
		output+="------------------------"+"\n";
		for(Token item: invalidTokens)
			output+=item.toString()+"\n";
		output+="------------------------"+"\n";
		output+="\nTOKENS"+"\n";
		output+="------------------------"+"\n";
		for(Token item: tokenList)
			output+=item.toString()+"\n";
		output+="No of tokens:"+tokenList.size()+"\n";
		System.out.println(output);
		filehandler.file_writer("Lexical Analysis", output);
		tokenList.add(new Token("$","$",0));
	}

	private void scanLine(String line, int linenum) {
		TokenNames.init();
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
		case '}':case '{':case ')':case '(':case '[':case ']':case ';':
		case ',':case '.':case '!':	case '+':case '-':finalState(Character.toString(item),linenum); return;
		case '<':case '>':case '=': statEQ(Character.toString(item),linenum);return;
		case '/': return;
		case '*': return;
		case '&':case '|': logicalOpState(Character.toString(item),linenum);return;
		}
		String buffer ="";
		if(Character.isAlphabetic(item)) {
			buffer= stateChar(Character.toString(item),item,linenum);
			if(isKeyword(buffer))
				finalState("KEYWORD",buffer,linenum);
			else
				finalState("IDENTIFIER","id",linenum);
		}
		else if(Character.isDigit(item)) {
			buffer= stateDigit(Character.toString(item),item,linenum);
			if(buffer.contains("."))
				finalState("FLOAT",buffer,linenum);
			else
				finalState("INTEGER",buffer,linenum);
		}
		else 
			errorState("Invalid token: Remove this symbol to correct the error",Character.toString(item),linenum);
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
				||buffer.equals("read")||buffer.equals("class")||buffer.equals("integer")
				||buffer.equals("float")||buffer.equals("for"))
			return true;
		else
			return false;
	}

	private void finalState(String token, int linenum) {
		Token temp= new Token(TokenNames.getTokenName(token),token,linenum);
		tokenList.add(temp);
	} 
	private void finalState(String tokenName,String token, int linenum) {
		Token temp= new Token(tokenName,token,linenum);
		tokenList.add(temp);
	} 
	private void errorState(String errorName, String item, int linenum) {
		Token temp= new Token(errorName,item,linenum);
		invalidTokens.add(temp);
	}

	private void updatePointer() {
		endPointer=currentPointer+1;
		currentPointer++;
	}

	private void statEQ(String item, int linenum) {
		String next= peekElement();
		if(next.equals("=")) {
			finalState(item+"=",linenum);
			updatePointer();
		}
		else
			finalState(item,linenum);
	}
	private void logicalOpState(String item, int linenum) {
		String next= peekElement();
		if(next.equals(item)) {
			finalState(item+item,linenum);
			updatePointer();
		}
		else 
			errorState("Invalid token: Remove this symbol to correct the error",item,linenum);
	}
}
