package parser;

import java.awt.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.*;
import java.util.List;

import lexical_analyser.LexicalAnalyser;






public class Parser {
	static ASTree tree= new ASTree();
    static int TokenNumber=0;
    static int NoOfMatchedTokens=0;
	static String lookahead;
	private static BufferedWriter DerivationsBuffer,SyntaxErrorBuffer;
	private static BufferedWriter ASTBuffer,SymbolTableBuffer;
	static String[] matchedTokens= new String[10000];
	 ASTNode prognode;

	public Parser() {

	}
	
	public void startParsing() {
		tree.addNode("prog","NT"," ");
		lookahead=LexicalAnalyser.getTokens().get(TokenNumber).getToken();
		File f1= new File("Resources/SyntaxDerivations.txt");
		try {
			if(f1.createNewFile())
				System.out.println("SyntaxDerivations File created");
			else 
				System.out.println("SyntaxDerivations File already exists");
			FileOutputStream fos = new FileOutputStream(f1);
			 DerivationsBuffer = new BufferedWriter(new OutputStreamWriter(fos));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		File f2= new File("Resources/SyntaxErrorsfound.txt");
		try {
			if(f2.createNewFile())
				System.out.println("SyntaxErrorsfound File created");
			else 
				System.out.println("SyntaxErrorsfound File already exists");
			FileOutputStream fos1 = new FileOutputStream(f2);
			 SyntaxErrorBuffer = new BufferedWriter(new OutputStreamWriter(fos1));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(prog() && match("$"))
		{
			System.out.println("No errors!");
			WriteErrors("Syntax Analysis succes. No errors found");
			ASTree.tableObj.makeTable("AST Tree Nodes");
		}
		else
		{
			System.out.println("Errors exist!");

		}
		try {
			DerivationsBuffer.close();
			SyntaxErrorBuffer.close();
		    tree.closebuffer();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("i:" + TokenNumber);
		System.out.println("finsllook:" + lookahead);
		System.out.println("matched tokens  :");
		for(int k=0;k<NoOfMatchedTokens-1;k++) {
			System.out.print(Parser.matchedTokens[k]);
		}
		System.out.print("\nThe end");
	}
	
private boolean Errorcheck(List<String>  FIRST, List<String>  FOLLOW) {
	if(FOLLOW!=null && FIRST!=null) {
		
		if (isLookin(FIRST) ||  isLookin(FOLLOW)) 
               return true;
        else
        {
        	WriteErrors("Syntax Error at token number: " + (TokenNumber+1)+ " ,For token: '" + LexicalAnalyser.getTokens().get(TokenNumber).getToken() + "' at line : " + LexicalAnalyser.getTokens().get(TokenNumber).getLineNum());
        }
		return false;
	}
	return false;
	}
private boolean Errorcheck(List<String>  FIRST) {
	if( FIRST!=null) {
		
		if (isLookin(FIRST)) 
               return true;
        else
        {
        	WriteErrors("Syntax Error at token number: " + (TokenNumber+1) + " ,For token: '" + LexicalAnalyser.getTokens().get(TokenNumber).getToken() + "' at line : " + LexicalAnalyser.getTokens().get(TokenNumber).getLineNum());
        }
		return false;
	}
	return false;
	}
/**
 *  
 * @param firstfollow
 * @return
 */
	public  boolean isLookin(List<String> firstfollow) {
		//String temp[]=firstfollow.toArray(new String[0]);
		for(String in: firstfollow)
		{
			
			if(in.compareTo(lookahead)==0) {
				return true;
			}
		}
		return false;
		
	}
	public String getNext()
	{
		if(LexicalAnalyser.getTokens().get(TokenNumber).getToken()!="$")
		{
		  TokenNumber++;
		}
		return LexicalAnalyser.getTokens().get(TokenNumber).getToken();
	}
	/**
	 * Matches lookahead with the function parameter
	 * @param temp
	 * @return
	 */
	public boolean match(String temp)
	{
		if(temp.compareTo(lookahead)==0) {
			System.out.println("match:" + lookahead);
			//result[m]=look; m++;
			matchedTokens[NoOfMatchedTokens]=lookahead; NoOfMatchedTokens++;
			if(lookahead!="$") {
			 tree.addNode(lookahead, LexicalAnalyser.getTokens().get(TokenNumber).getToken(),
					 LexicalAnalyser.getTokens().get(TokenNumber).getName(),
					 Integer.toString(LexicalAnalyser.getTokens().get(TokenNumber).getLineNum()));
			 tree.goup();
			}
			lookahead=getNext();
			return true;
		}
		else
			System.out.println("no match");
    	WriteErrors("Syntax Error at token number: " + (TokenNumber+1)  + " ,Mismatched token '" + LexicalAnalyser.getTokens().get(TokenNumber).getToken()+ "' at line : " + LexicalAnalyser.getTokens().get(TokenNumber).getLineNum());
		return false;
		
	}
	public boolean match1(String temp)
	{
		if(temp.compareTo(lookahead)==0) {
			System.out.println("match:" + lookahead);
			//result[m]=look; m++;
			matchedTokens[NoOfMatchedTokens]=lookahead; NoOfMatchedTokens++;
			if(lookahead!="$") {
				 tree.addNode(lookahead, LexicalAnalyser.getTokens().get(TokenNumber).getToken(),
						 LexicalAnalyser.getTokens().get(TokenNumber).getToken());
				 tree.goup();
				}
			lookahead=getNext();
			return true;
		}else
			System.out.println("no match");
			return false;
		
	}
public void WriteDerivation(String s) {
	try {
	DerivationsBuffer.write(s);DerivationsBuffer.newLine();
	
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}
public static void WriteErrors(String s) {
	try {
	SyntaxErrorBuffer.write(s);SyntaxErrorBuffer.newLine(); 
	
	}catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

	public boolean prog() {// E-> TE'
		System.out.println("inside prog");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "class","main", "integer", "float", "id" ));
		Errorcheck(First1);
		if(isLookin(First1))
			
		{
			if( classDeclSet() && funcDefSet() &&   match("main")  && funcBody() &&  match(";")) {
				
				System.out.println("prog ->     classDeclSet    funcDefSet   'main'   funcBody ';'");
				WriteDerivation("prog ->     classDeclSet    funcDefSet   'main'   funcBody ';'");
				System.out.println("look:" + lookahead);
				return true;
			}
			/*else {
				if(!match(";")) {
					WriteErrors("syntax error at" + TokenNumber + "expected token: ';' at end of program");
				}
			}*/
		}
		//WriteErrors("Syntax Error at token number: " + TokenNumber + " ,For token: '" + tok.toks[TokenNumber] + "' at line : " + String.valueOf(tok.findLineNumber[TokenNumber]));
		return false;
		
	}
	/**
	 * For each NonTerminal
	 * @return
	 */
	public boolean classDeclSet() {
	
	System.out.println("inside classDeclSet");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "class" ));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("class", "main", "integer", "float", "id" ));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("classDeclSet","NT","");
			if( classDecl() && classDeclSet()) {
				System.out.println("classDeclSet -> classDecl classDeclSet");
				WriteDerivation("classDeclSet -> classDecl classDeclSet");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
			
				System.out.println("classDeclSet		-> EPSILON");
				WriteDerivation("classDeclSet		-> EPSILON");
				System.out.println("look:" + lookahead);
				
				return true;
			
		}
		
		return false;}
	
	public boolean funcDefSet() {
		System.out.println("inside funcDefSet");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "integer", "float", "id" ));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "main" ));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("funcDefSet","NT","");
			if( funcDef() && funcDefSet()) {
				WriteDerivation("funcDefSet 		-> funcDef funcDefSet");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("funcDefSet 		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean classDecl() {
           System.out.println("inside classDecl");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "class" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("classDecl","NT","");
			if( match("class") &&  match("id")  && inheritedfuncs() && match("{") &&  classBody() &&  match("}") && match(";")) {
				WriteDerivation("classDecl 		->  'class'   'id'   inheritedfuncs '{ '  classBody '}' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean inheritedfuncs() {
		System.out.println("inside inheritedfuncs looking:" + lookahead);
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( ":" ));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "{" ));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("inheritedfuncs","NT","");
			if(match(":") && match("id") && inheritedFuncSet()) {
				WriteDerivation("inheritedfuncs		-> ':'   'id' inheritedFuncSet");
				System.out.println("look :" + lookahead);
				tree.goup();
				return true;
			}
		} 
		else if(isLookin(Follow1))
		{
				WriteDerivation("inheritedfuncs		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean inheritedFuncSet() {
		System.out.println("inside inheritedFuncSet");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "," ));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "{" ));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("inheritedfuncSet","NT","");
			if(match(",") && match("id") && inheritedFuncSet()) {
				WriteDerivation("inheritedFuncSet	-> ','   'id' inheritedFuncSet");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("inheritedFuncSet	-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean classBody() {
		System.out.println("inside classBody");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "integer", "float", "id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "}"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("classBody","NT","");
			if(type() &&   match("id") &&  VarAndFuncDecl()) {
				WriteDerivation("classBody		-> type   'id' VarAndFuncDecl");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("classBody		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean VarAndFuncDecl() {
		System.out.println("inside VarAndFuncDecl");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( ";", "["));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "("));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("VarAndFuncDecl","NT","");
			if(varDecl() && classBody()) {
				WriteDerivation("VarAndFuncDecl		->   varDecl classBody");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			if(funcDecl() && otherFuncDecl()) {
				WriteDerivation("VarAndFuncDecl	->   funcDecl otherFuncDecl");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean varDecl() {
     System.out.println("inside varDecl");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( ";","[" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("varDecl","NT"," ");
			tree.varDeclNode.add(tree.tnode);
			if( arraySizeList() && match(";")) {
				WriteDerivation("varDecl 			->   arraySizeList ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean otherFuncDecl() {
		System.out.println("inside otherFuncDecl");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "integer", "float", "id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "}"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("otherFuncDecl","NT"," ");
			if(type() &&  match("id") && otherFuncDeclrecursion()) {
				WriteDerivation("otherFuncDecl			-> type   'id' otherFuncDeclrecursion");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("otherFuncDecl			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean otherFuncDeclrecursion() {
          System.out.println("inside otherFuncDeclrecursion");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "(" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("otherFuncDeclrecursion","NT"," ");
			if(funcDecl() && otherFuncDecl()) {
				WriteDerivation("otherFuncDeclrecursion		->   funcDecl otherFuncDecl");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean funcDecl() {
        System.out.println("inside funcDecl");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "(" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("funcDecl","NT"," ");
			if(match("(") && fParamsSet() && match(")") && match(";")) {
				WriteDerivation("funcDecl 			->   '(' fParamsSet ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean funcDef() {
        System.out.println("inside funcDef");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "integer", "float", "id" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("funcDef","NT"," ");
			if(funcHead() &&   funcBody() && match(";")) {
				WriteDerivation("funcDef 			->   funcHead   funcBody ;");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean funcHead() {
        System.out.println("inside funcHead");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "integer", "float", "id" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("funcHead","NT"," ");
			if(type() && funcName() &&   match("(") && fParamsSet() && match(")") ) {
				WriteDerivation("funcHead 			-> type funcName   '(' fParamsSet ')'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean funcName() {
        System.out.println("inside funcName");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("funcName","NT"," ");
			if( match("id") && scopeandFunc() ) {
				WriteDerivation("funcName 			->   'id' scopeandFunc");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean scopeandFunc() {
		System.out.println("inside scopeandFunc");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("sr"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "("));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("scopeandFunc","NT"," ");
			if(match("sr")  && match("id")) {
				WriteDerivation("scopeandFunc 		->   'sr'   'id'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("scopeandFunc 		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean funcBody() {
        System.out.println("inside funcBody");       
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("{" ));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("funcBody","NT"," ");
			if(match("{") && funcStatements() & match("}") ) {
				WriteDerivation("funcBody 			-> '{' funcStatements '}'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean funcStatements() {
		System.out.println("inside funcStatements");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "float", "integer"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "id", "if", "for", "read", "write", "return"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "}"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("funcStatements","NT"," ");
			if(varStat() && funcStatements()) {
				WriteDerivation("funcStatements		-> varStat funcStatements");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("funcStatements","NT"," ");
			if(varStatNew() && funcStatNew()) {
				WriteDerivation("funcStatements		-> varStatNew funcStatNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(Follow1))
		{
			WriteDerivation("funcStatements		-> EPSILON");
			System.out.println("look:" + lookahead);
			return true;
		
	     }
		return false;}

	public boolean funcStatNew() {
		System.out.println("inside funcStatNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id", "if", "for", "read", "write", "return"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "}"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("funcStatNew","NT"," ");
			if(varStatNew() && funcStatNew()) {
				WriteDerivation("funcStatNew	-> varStatNew funcStatNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("funcStatNew	-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean varStat() {
        System.out.println("inside varStat");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("float", "integer"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("varStat","NT"," ");
			if(typefloatorint() &&   match("id") &&   varDecl() ) {
				WriteDerivation("varStat				-> typefloatorint   'id'   varDecl");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean typefloatorint() {
		System.out.println("inside typefloatorint");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("float"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "integer"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("typefloatorint","NT"," ");
			if(match("float")) {
				WriteDerivation("typefloatorint				->   'float'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("typefloatorint","NT"," ");
			if(match("integer")) {
				WriteDerivation("typefloatorint				->   'integer'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean varStatNew() {
		System.out.println("inside varStatNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "if", "for", "read", "write", "return"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("varStatNew","NT"," ");
			tree.varStatNew.add(tree.tnode);
			if(match("id") && varStatTail()) {
				WriteDerivation("varStatNew			->   'id' varStatTail");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("varStatNew","NT"," ");
			if(statementOther()) {
				WriteDerivation("varStatNew			-> statementOther");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean varStatTail() {
		System.out.println("inside varStatTail");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "[", ".", ";"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList( "("));
		List<String> First4= new ArrayList<String>() ;
		First4.addAll(Arrays.asList( "="));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		First.addAll(First4);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("varStatTail","NT"," ");
//			tree.varStatTail.add(tree.tnode);
			if(match("id") &&   varDecl()) {
				WriteDerivation("varStatTail			->   'id'   varDecl");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("varStatTail","NT"," ");
//			tree.varStatTail.add(tree.tnode);
			if(indiceList() && idnestList() && match1(";")) {
				WriteDerivation("varStatTail			->  indiceList idnestList ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}	

		}else if(isLookin(First3))
		{
			tree.addNode("varStatTail","NT"," ");
//			tree.varStatTail.add(tree.tnode);
			if(match1("(") && aParams() && match1(")") && idnestList() && assignStatTail() && match1(";")) {
				WriteDerivation("varStatTail			->     '(' aParams ')' idnestList assignStatTail ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
			
		}
		 if(isLookin(First4))
		{
			 tree.addNode("varStatTail","NT"," ");
			 tree.exprNode.add(tree.tnode);
			 
			if(match("=") && expr() && match(";")) {
				WriteDerivation("varStatTail    -> '=' idornum ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			} 
		}
		return false;}
		
	public boolean idornum() {
        System.out.println("inside idornum");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("id"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList("intNum"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("idornum","NT"," ");
			if(match("intNum")) {
				WriteDerivation("idornum -> 'intNum'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First2))
		{
			tree.addNode("idornum","NT"," ");
			if(expr()) {
				WriteDerivation("assignStatTail		-> assignOp   expr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
			
		return false;}
	
	public boolean assignStatTail() {
        System.out.println("inside assignStatTail");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("="));
		Errorcheck(First1);

		if(isLookin(First1))
		{
			tree.addNode("assignStatTail","NT"," ");
			if(assignOp() &&   expr() ) {
				WriteDerivation("assignStatTail		-> assignOp   expr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	

	public boolean statementOther() {
		System.out.println("inside statementOther");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("if"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("for"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList("read"));
		List<String> First4= new ArrayList<String>() ;
		First4.addAll(Arrays.asList( "write"));
		List<String> First5= new ArrayList<String>() ;
		First5.addAll(Arrays.asList( "return"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		First.addAll(First4);
		First.addAll(First5);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("statementOther","NT"," ");
			if(match("if") && match("(") &&  expr() && match(")") && match("then") && statBlock() && match("else") &&  statBlock() &&   match(";")) {
				WriteDerivation("statementOther		->   'if' '(' expr ')' 'then'   statBlock   'else'   statBlock   ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("statementOther","NT"," ");
			if(match("for") && match("(") && type() &&  match("id") && assignOp() && match("intNum") && match(";") && relExpr() &&match(";") && assignStat() && match(")") && statBlock() && match(";")) {
				WriteDerivation("statementOther		->   'for' '(' type   'id'  assignOp expr ';' relExpr ';' assignStat ')' statBlock ';'"); 
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			} 
			//else
				//WriteErrors("Syntax Error at token number: " + TokenNumber + " ,For token: " + tok.toks[TokenNumber]);
		}else if(isLookin(First3))
		{  
			tree.addNode("statementOther","NT"," ");
			if(match("read") && match("(") && variable() && match(")") && match(";")) {
				WriteDerivation("statementOther		->  'read' '(' variable ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First4))
		{
			tree.addNode("statementOther","NT"," ");
			if(match("write") && match("(") && expr() && match(")") && match(";")) {
				WriteDerivation("statementOther		->   'write' '(' expr ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First5))
		{
			tree.addNode("statementOther","NT"," ");
			if(match("return") && match("(") && expr() && match(")") && match(";")) {
				WriteDerivation("statementOther		->   'return' '(' expr ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		//WriteErrors("Syntax Error at token number: " + TokenNumber + " ,For token: " + tok.toks[TokenNumber]);
		return false;}

	public boolean statementList() {
		System.out.println("inside statementList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("if", "for", "read", "write", "return", "id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "}"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("statementList","NT"," ");
			if(statement() && statementList()) {
				WriteDerivation("statementList 		-> statement statementList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("statementList 		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean arraySizeList() {
		System.out.println("inside arraySizeList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("["));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( ",", ";", ")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("arraySizeList","NT"," ");
			if(arraySize() && arraySizeList()) {
				WriteDerivation("arraySizeList		-> arraySize arraySizeList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("arraySizeList		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;
			
		}
		return false;}

	public boolean statement() {
		System.out.println("inside statement");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("if"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList("for"));
		List<String> First4= new ArrayList<String>() ;
		First4.addAll(Arrays.asList( "read"));
		List<String> First5= new ArrayList<String>() ;
		First5.addAll(Arrays.asList( "write"));
		List<String> First6= new ArrayList<String>() ;
		First6.addAll(Arrays.asList( "return"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		First.addAll(First4);
		First.addAll(First5);
		First.addAll(First6);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("statement","NT"," ");
			if(assignStat() && match(";")) {
				WriteDerivation("statement 			-> assignStat ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("statement","NT"," ");
			if(match("if") && match("(") && expr() && match(")") && match("then") && statBlock() &&  match("else") &&   statBlock() &&   match(";")) {
				WriteDerivation("statement 			->   'if' '(' expr ')' 'then'   statBlock   'else'   statBlock   ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("statement","NT"," ");
			if(match("for") && match("(") && type() && match("id") &&  assignOp() && expr() && match(";") && relExpr() && match(";") && assignStat() && match(")") && statBlock() && match(";")) {
				WriteDerivation("statement 			->   'for' '(' type 'id'  assignOp expr ';' relExpr ';' assignStat ')' statBlock ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First4))
		{
			tree.addNode("statement","NT"," ");
			if(match("read") && match("(") && variable() && match(")") &&  match(";")) {
				WriteDerivation("statement 			->   'read' '(' variable ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First5))
		{
			tree.addNode("statement","NT"," ");
			if(match("write") && match("(") && expr() && match(")") && match(";")) {
				WriteDerivation("statement 			->   'write' '(' expr ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First6))
		{
			tree.addNode("statement","NT"," ");
			if(match("return") && match("(") && expr() && match(")") && match(";")) {
				WriteDerivation("statement 			->   'return' '(' expr ')' ';'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		
		return false;}

	public boolean assignStat() {
        System.out.println("inside assignStat");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("assignStat","NT"," ");
			if(variable() && assignOp() &&   expr() ) {
				WriteDerivation("assignStat 			-> variable   assignOp   expr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean statBlock() {
		System.out.println("inside statBlock");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList( "{"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "if", "for", "read", "write", "return", "id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("else", ";"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("statBlock","NT"," ");
			if(match("{") && statementList() && match("}")) {
				WriteDerivation("fstatBlock 			-> '{' statementList '}'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true; 
			}
		} 
		else if(isLookin(First2))
		{
			tree.addNode("statBlock","NT"," ");
			if(statement()	) {
				WriteDerivation("statBlock 			-> statement");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(Follow1))
		{
			WriteDerivation("statBlock 			-> EPSILON");
			System.out.println("look:" + lookahead);
			return true;
		
	     }
		return false;}

	public boolean expr() {
        System.out.println("inside expr");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum", "floatNum", "(", "not", "id", "+", "-"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("expr","NT"," ");
//			tree.exprNode.add(tree.tnode);
			if(arithExpr() && exprNew() ) {
				WriteDerivation("expr 				-> arithExpr exprNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;
		}
	
	public boolean exprNew() {
		System.out.println("inside exprNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("eq", "neq", "lt", "gt", "leq", "geq"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( ",", ")", ";"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("exprNew","NT"," ");
			if(relOp() && arithExpr()) {
				WriteDerivation("exprNew 			->   relOp arithExpr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("exprNew 			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}
	
	public boolean relExpr() {
        System.out.println("inside relExpr");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum", "floatNum", "(", "not", "id", "+", "-"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("relExpr","NT"," ");
			if(arithExpr() &&   relOp() && arithExpr() ) {
				WriteDerivation("relExpr 			-> arithExpr   relOp arithExpr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean arithExpr() {
        System.out.println("inside arithExpr");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum", "floatNum", "(", "not", "id", "+", "-"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("arithExpr","NT"," ");
			if(term() && arithExprNew() ) {
				WriteDerivation("arithExpr 			-> term arithExprNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean arithExprNew() {
		System.out.println("inside arithExprNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("+", "-", "or"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("]", ")", "eq", "neq", "lt", "gt", "leq", "geq", ";", ","));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("arithExprNew","NT"," ");
			if(addOp() && term() && arithExprNew()) {
				WriteDerivation("arithExprNew 		->   addOp term arithExprNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("arithExprNew 		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean sign() {
		System.out.println("inside sign");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("+"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "-"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("sign","NT"," ");
			if(match("+")) {
				WriteDerivation("sign 				-> '+'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("sign","NT"," ");
			if(match("-")) {
				WriteDerivation("sign 				-> '-'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean term() {
        System.out.println("inside term");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum", "floatNum", "(", "not", "id", "+", "-"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("term","NT"," ");
			if(factor() && termNew() ) {
				WriteDerivation("term 				-> factor termNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean termNew() {
		System.out.println("inside termNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("*", "/", "and"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("+", "-", "or", "", ")","]", "eq", "neq", "lt", "gt", "leq", "geq", ";", ","));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("termNew","NT"," ");
			if(multOp() && factor() && termNew()) {
				WriteDerivation("termNew 			->   multOp factor termNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("termNew 			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean factor() {
		System.out.println("inside factor");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("intNum"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList("floatNum"));
		List<String> First4= new ArrayList<String>() ;
		First4.addAll(Arrays.asList( "("));
		List<String> First5= new ArrayList<String>() ;
		First5.addAll(Arrays.asList( "not"));
		List<String> First6= new ArrayList<String>() ;
		First6.addAll(Arrays.asList( "+", "-"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		First.addAll(First4);
		First.addAll(First5);
		First.addAll(First6);
		Errorcheck(First);
		 if(isLookin(First1))
		{
			 tree.addNode("factor","NT"," ");
			if(varFunc()) {
				WriteDerivation("factor 				->   varFunc");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First2))
		{
			tree.addNode("factor","NT"," ");
			if(match("intNum")) {
				WriteDerivation("factor 				->   'intNum'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("factor","NT"," ");
			if(match("floatNum")) {
				WriteDerivation("factor 				-> 'floatNum'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First4))
		{
			tree.addNode("factor","NT"," ");
			if(match("(") && arithExpr() && match(")")) {
				WriteDerivation("factor 				-> '(' arithExpr ')'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First5))
		{
			tree.addNode("factor","NT"," ");
			if(match("not") && factor()) {
				WriteDerivation("factor 				-> not factor");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}	else if(isLookin(First6))
		{
			tree.addNode("factor","NT"," ");
			if(sign() && factor()) {
				WriteDerivation("factor 				-> sign factor");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean variable() {
        System.out.println("inside variable");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("variable","NT"," ");
			if(match("id") &&  variableNew()) {
				WriteDerivation("variable 			->   'id'   variableNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean variableNew() {
		System.out.println("inside variableNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("[", "."));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "("));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList( "=",")"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
        Errorcheck(First,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("variableNew","NT"," ");
			if(indiceList() && idnestList()) {
				WriteDerivation("variableNew			->   indiceList idnestList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("variableNew","NT"," ");
			if(match("(") && aParams() && match(")") && idnestList()) {
				WriteDerivation("variableNew			-> '(' aParams ')' idnestList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(Follow1))
		{
			WriteDerivation("variableNew			-> EPSILON");
			System.out.println("look:" + lookahead);
			return true;	
	}
		return false;}

	public boolean varFunc() {
        System.out.println("inside varFunc");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("varFunc","NT"," ");
			if(match("id") && indiceList() && idnestListNew() && varFuncTail() ) {
				WriteDerivation("varFunc 			->   'id'     indiceList idnestListNew varFuncTail");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		
		return false;}
	
	public boolean varFuncTail() {
		System.out.println("inside varFuncTail");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("("));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("*", "/", "and", "+", "]","-", "or", "", ")", "eq", "neq", "lt", "gt", "leq", "geq", ";", ","));
        Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{ 
			tree.addNode("varFuncTail","NT"," ");
			if(match("(") && aParams() && match(")") &&  varFuncTail2()) {
				WriteDerivation("varFuncTail			->   '(' aParams ')'  varFuncTail2");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("varFuncTail			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}
	
	public boolean varFuncTail2() {
		System.out.println("inside varFuncTail2");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("."));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("*", "/", "and", "+", "]","-", "or", "", ")", "eq", "neq", "lt", "gt", "leq", "geq", ";", ","));
        Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("varFuncTail2","NT"," ");
			if(idnest()) {
				WriteDerivation("varFuncTail2		-> idnest");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("varFuncTail2		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean idnestListNew() {
		System.out.println("inside idnestListNew");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("."));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList("(", "*", "/", "and", "]","+", "-", "or", "", ")", "eq", "neq", "lt", "gt", "leq", "geq", ";", ","));
        Errorcheck(First1,Follow1);
        if(isLookin(First1))
		{
        	tree.addNode("idnestListNew","NT"," ");
			if(idnestNew() && idnestListNew()) {
				WriteDerivation("idnestListNew		-> idnestNew idnestListNew");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("idnestListNew		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean indiceList	() {

		System.out.println("inside indiceList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("["));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(".", ";", "*", "/", "and", "+", "-", "]","or", "", ")", "eq", "neq", "lt", "gt", "leq", "geq", ",", "="));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("("));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
        Errorcheck(First,Follow1);

		if(isLookin(First1))
		{
			tree.addNode("indiceList","NT"," ");
			if(indice() && indiceList()) {
				WriteDerivation("indiceList			-> indice indiceList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First2))
		{
			tree.addNode("indiceList","NT"," ");
			if(match("(") && funcallPar()&& match(")")) {
				WriteDerivation("indiceList			-> indice indiceList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("idnestListNew		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}
	
	public boolean funcallPar() {
        System.out.println("inside funcalParam");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("funcallPar","NT"," ");
			if(aParams()) {
				WriteDerivation("FuncallPar 			-> id");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(Follow1))
		{
			WriteDerivation("funcallPar		-> EPSILON");
			System.out.println("look:" + lookahead);
			return true;	
	}
		return false;}
	

	public boolean idnestNew() {
        System.out.println("inside idnestNew");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("."));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("idnestNew","NT"," ");
			if(match(".") && idnestNewTail()) {
				WriteDerivation("idnestNew 			-> '.' idnestNewTail");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean idnestNewTail() {
		System.out.println("inside idnestNewTail");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("id"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "("));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
        Errorcheck(First);
		if(isLookin(First1))
		{ 
			tree.addNode("idnestNewTail","NT"," ");
			if(match("id") && indiceList()) {
				WriteDerivation("idnestNewTail		->   'id' indiceList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("idnestNewTail","NT"," ");
			if(match("(") && aParams() && match(")")) {
				WriteDerivation("idnestNewTail		-> '(' aParams ')'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	

	public boolean indice() {
        System.out.println("inside indice");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("["));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("indice","NT"," ");
			if(match("[") && arithExpr() && match("]")) {
				WriteDerivation("indice 				-> '[' arithExpr ']'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean idnestList() {
		System.out.println("inside idnestList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("."));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(";", "=", ")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("idnestList","NT"," ");
			if(idnest() && idnestList()) {
				WriteDerivation("idnestList			-> idnest idnestList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("idnestList			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean idnest() {
        System.out.println("inside idnest");       
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("."));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("idnest","NT"," ");
			if(match(".") && match("id") && indiceList()) {
				WriteDerivation("idnest 				-> '.'   'id' indiceList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean arraySize() {
        System.out.println("inside arraySize");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("["));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("arraySize","NT"," ");
			if(match("[") &&  match("intNum") && match("]")) {
				WriteDerivation("arraySize 			-> '['   'intNum' ']'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean type() {
		System.out.println("inside type");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("integer"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "float"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList( "id"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
        Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("type","NT"," ");
			if(match("integer")) {
				WriteDerivation("type 				->   'integer'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("type","NT"," ");
			if(match("float")) {
				WriteDerivation("type 				->   'float'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("type","NT"," ");
			if(match("id")) {
				WriteDerivation("type 				->   'id'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean fParamsSet() {
		System.out.println("inside fParamsSet");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("integer", "float", "id"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("fParamsSet","NT"," ");
			tree.fparmsNode.add(tree.tnode);
			if(type() && match("id") && arraySizeList() && fParamsTailList()) {
				WriteDerivation("fParamsSet 			->    type   'id'   arraySizeList     fParamsTailList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("fParamsSet 			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean fParamsTailList() {
		System.out.println("inside fParamsTailList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList(","));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("fParamsTailList","NT"," ");
			if(fParamsTail() &&   fParamsTailList()) {
				WriteDerivation("fParamsTailList		->   fParamsTail   fParamsTailList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("fParamsTailList		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean aParams() {
		System.out.println("inside aParams");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("intNum", "floatNum", "(", "not", "id", "+", "-"));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("aParams","NT"," ");
			if(expr() && aParamsTailList()) {
				WriteDerivation("aParams 			-> expr aParamsTailList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("aParams 			-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		return false;}

	public boolean aParamsTailList() {
		System.out.println("inside aParamsTailList");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList(","));
		List<String> Follow1= new ArrayList<String>() ;
		Follow1.addAll(Arrays.asList(")"));
		Errorcheck(First1,Follow1);
		if(isLookin(First1))
		{
			tree.addNode("aParamsTailList","NT"," ");
			if(aParamsTail() && aParamsTailList()) {
				WriteDerivation("aParamsTailList		-> aParamsTail aParamsTailList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(Follow1))
		{
				WriteDerivation("aParamsTailList		-> EPSILON");
				System.out.println("look:" + lookahead);
				return true;	
		}
		
		return false;}

	public boolean fParamsTail() {
		System.out.println("inside fParamsTail");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList(","));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("fParamsTail","NT"," ");
			if(match(",") && type() &&   match("id") &&   arraySizeList()) {
				WriteDerivation("fParamsTail 		-> ',' type   'id'   arraySizeList");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}


	public boolean aParamsTail() {
        System.out.println("inside aParamsTail");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList(","));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("aParamsTail","NT"," ");
			if(match(",") && expr()) {
				WriteDerivation("aParamsTail 		-> ',' expr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}

	public boolean assignOp() {
        System.out.println("inside assignOp");
        
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("="));
		Errorcheck(First1);
		if(isLookin(First1))
		{
			tree.addNode("assignOp","NT"," ");
			if(match("=")) {
				WriteDerivation("aParamsTail 		-> ',' expr");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		
		return false;}

	public boolean relOp() {
		System.out.println("inside relOp");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("eq"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList("neq"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList("lt"));
		List<String> First4= new ArrayList<String>() ;
		First4.addAll(Arrays.asList( "gt"));
		List<String> First5= new ArrayList<String>() ;
		First5.addAll(Arrays.asList( "leq"));
		List<String> First6= new ArrayList<String>() ;
		First6.addAll(Arrays.asList( "geq"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		First.addAll(First4);
		First.addAll(First5);
		First.addAll(First6);
        Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("relOp","NT"," ");
			if(match("eq")) {
				WriteDerivation("relOp 				->   'eq'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("relOp","NT"," ");
			if(match("neq")) {
				WriteDerivation("relOp 				->   'neq'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("relOp","NT"," ");
			if(match("lt")) {
				WriteDerivation("relOp 				->   'lt'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First4))
		{
			tree.addNode("relOp","NT"," ");
			if(match("gt")) {
				WriteDerivation("relOp 				->   'gt'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First5))
		{
			tree.addNode("relOp","NT"," ");
			if(match("leq")) {
				WriteDerivation("relOp 				->   'leq'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First6))
		{
			tree.addNode("relOp","NT"," ");
			if(match("geq")) {
				WriteDerivation("relOp 				->   'geq'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}	
		return false;}

	public boolean addOp() {
		System.out.println("inside addOp");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("+"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "-"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList( "or"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
		Errorcheck(First);
		if(isLookin(First1))
		{
			tree.addNode("addOp","NT"," ");
			if(match("+")) {
				WriteDerivation("addOp 				->   '+'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))	
		{
			tree.addNode("addOp","NT"," ");
			if(match("-")) {
				WriteDerivation("addOp 				->   '-'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("addOp","NT"," ");
			if(match("or")) {
				WriteDerivation("addOp 				->   'or'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}
	
	public boolean multOp() {
		System.out.println("inside multOp");
		List<String> First1= new ArrayList<String>() ;
		First1.addAll(Arrays.asList("*"));
		List<String> First2= new ArrayList<String>() ;
		First2.addAll(Arrays.asList( "/"));
		List<String> First3= new ArrayList<String>() ;
		First3.addAll(Arrays.asList( "and"));
		List<String> First= new ArrayList<String>() ;
		First.addAll(First1);
		First.addAll(First2);
		First.addAll(First3);
        Errorcheck(First);	
		if(isLookin(First1))
		{
			tree.addNode("multOp","NT"," ");
			if(match("*")) {
				WriteDerivation("multOp 				->   '*'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		else if(isLookin(First2))
		{
			tree.addNode("multOp","NT"," ");
			if(match("/")) {
				WriteDerivation("multOp 				->   '/'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}else if(isLookin(First3))
		{
			tree.addNode("multOp","NT"," ");
			if(match("and")) {
				WriteDerivation("multOp 				->   'and'");
				System.out.println("look:" + lookahead);
				tree.goup();
				return true;
			}
		}
		return false;}


    
	



	
	
}
