package parser;
import java.io.IOException;
import java.security.KeyStore.Entry.Attribute;
import java.util.ArrayList;
import java.util.LinkedList;

public class TreeNode {

	String NodeName="";
	String NodeKind="";
	String Token="%";
	String data="";
	String type="";
	String datatype="";
	int NodeIndex=0;
	String LineNumber=" ";
	TreeNode parent;
	ArrayList<TreeNode> children= new ArrayList<>();
//	ArrayList<Integer> ArraySize= new ArrayList<>();
//	int ArrayDimension=0;
//	ArrayList<String> InheritedList= new ArrayList<>(); 
//	ArrayList<String> ParamsType= new ArrayList<>(); // type of paramaters in function 
//	ArrayList<String> ParamsVariable= new ArrayList<>(); 
//	ArrayList<String> Params= new ArrayList<>(); 
//	ArrayList<ASTNode> funcCallVar= new ArrayList<>(); 
//	ArrayList<ASTNode> arraySizes= new ArrayList<>();
//	ArrayList<ASTNode> arrayVar= new ArrayList<>();
//	String scopename="";
//	String ReturnedType="";

	public TreeNode(String pname, String pkind,String linenum) {
		NodeName = pname;
		NodeKind = pkind;
		type=data="";
		LineNumber=linenum;

	}
	public TreeNode(String pname, String pkind) {
		NodeName = pname;
		NodeKind = pkind;
		type=data="";

	}
	public TreeNode(TreeNode node) {
		this.NodeName= node.NodeName;
		this.NodeKind= node.NodeKind;
		this.LineNumber=node.LineNumber;
	}
	public void addData(String pdata) {
		data=pdata;
	}
	public void isLeaf() {
		children=null;
	}

}
