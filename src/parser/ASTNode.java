package parser;
import java.io.IOException;
import java.security.KeyStore.Entry.Attribute;
import java.util.ArrayList;
import java.util.LinkedList;

public class ASTNode {

	String NodeName="%";
	String data="%";
	String kind="%";
	String type="%";
	String datatype="";
	ArrayList<Integer> ArraySize= new ArrayList<>();
	int ArrayDimension=0;
	ArrayList<String> InheritedList= new ArrayList<>(); 
	String IDName="%";
	ArrayList<String> ParamsType= new ArrayList<>(); // type of paramaters in function 
	ArrayList<String> ParamsVariable= new ArrayList<>(); 
	ArrayList<String> Params= new ArrayList<>(); 
	ArrayList<ASTNode> funcCallVar= new ArrayList<>(); 
	ArrayList<ASTNode> arraySizes= new ArrayList<>();
	ArrayList<ASTNode> arrayVar= new ArrayList<>();
	String LineNumber=" ";
	String scopename="";
	int scopekey=0;
	String ReturnedType="";


	 int key=0;
	ASTNode parent;
	ArrayList<ASTNode> children= new ArrayList<>();
	ArrayList<ASTNode> siblings= new ArrayList<>();

	public ASTNode(String pname, String pkind,String linenum) {
		NodeName = pname;
		kind = pkind;
		type=data="";
		LineNumber=linenum;
//		if(No)
//		datatype="";
//		children=null;
	}
	public ASTNode(String pname, String pkind) {
		NodeName = pname;
		kind = pkind;
		type=data="";
//		LineNumber=linenum;
//		datatype="";
//		children=null;
	}
	public void addData(String pdata) {
		data=pdata;
	}
	public void isLeaf() {
		children=null;
	}

}
