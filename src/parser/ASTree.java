package parser;
 	import java.io.IOException;
import java.util.*;

import helpers.filehandler;

public class ASTree {
	filehandler ast_file;
	ASTNode root;
	ASTNode tnode;
	ArrayList<ASTNode> varStatNew= new ArrayList<>();
	ArrayList<ASTNode> exprNode= new ArrayList<>();
	ArrayList<ASTNode> fparmsNode= new ArrayList<>();
	ArrayList<ASTNode> varDeclNode= new ArrayList<>();

	

	static int inc = 0;
	int savescopekey;

	public ASTree() {
		root = null;
		tnode = null;
		ast_file = new filehandler("Resources/AST.txt");
		ast_file.writeLine(String.format("%15s %15s %15s %10s %15s %15s %5s %5s %5s %5s %15s %5s %15s", "Node", "|", "Kind",
				"|", "Name", "|", "Data", "|", "Type", "|", "ParentNode", "|", "Children"));
		ast_file.writeLine("_____________________________________________________________________________________________");

	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//
//	}
	public void addNode(String nodename, String kind, String pIDName, String linenum) {
		ASTNode node = new ASTNode(nodename, kind, linenum);
		node.key = inc++;
		node.scopekey=savescopekey;
		node.IDName = pIDName;
		if (root == null) {
			root = node;
			tnode = node;
			ast_file.writeString(String.format("%15s %15s %15s %10s %15s %15s %5s %5s %5s %5s %15s %5s", node.NodeName, "|",
					node.kind, "|", node.IDName, "|", node.data, "|", node.type, "|", "no parent", "|"));

		} else {
			tnode.children.add(node);
			for (ASTNode child : tnode.children) {
				child.parent = tnode;// tnode=node;
			}
			godown(node);
			ast_file.writeString(String.format("%15s %15s %15s %10s %15s %15s %5s %5s %5s %5s %15s %5s", node.NodeName, "|",
					node.kind, "|", node.IDName, "|", node.data, "|", node.type, "|", node.parent.NodeName, "|"));

		}
		for (ASTNode child : tnode.children)
			ast_file.writeString(String.format("%15s", "yoyo "));
		try {
			ast_file.bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNode(String nodename, String kind, String pIDName) {
		ASTNode node = new ASTNode(nodename, kind);
		node.key = inc++;

		node.scopekey=savescopekey;
		node.IDName = pIDName;
		if (root == null) {
			root = node;
			tnode = node;
			ast_file.writeString(String.format("%15s %15s %15s %10s %15s %15s %5s %5s %5s %5s %15s %5s", node.NodeName, "|",
					node.kind, "|", node.IDName, "|", node.data, "|", node.type, "|", "no parent", "|"));
			String data[][],column[];
//			data[data.length]= {node.NodeName, node.kind, node.IDName, node.data, node.type, "no parent"};
//			filehandler.makeTable(data, column);

		} else {
			tnode.children.add(node);
			for (ASTNode child : tnode.children) {
				child.parent = tnode;// tnode=node;
			}
			godown(node);
			ast_file.writeString(String.format("%15s %15s %15s %10s %15s %15s %5s %5s %5s %5s %15s %5s", node.NodeName, "|",
					node.kind, "|", node.IDName, "|", node.data, "|", node.type, "|", node.parent.NodeName, "|"));

		}
		for (ASTNode child : tnode.children)
			ast_file.writeString(String.format("%15s", "yoyo "));
		try {
			ast_file.bw.newLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addNode(ASTNode node1) {
		ASTNode node = new ASTNode(node1.NodeName, node1.kind);
		node.key = node1.key;
		node.IDName = node1.IDName;
		node.type = node1.type;
		node.data = node1.data;
		if (root == null) {
			root = node;
			tnode = node;
		} else {
			tnode.children.add(node);
			for (ASTNode child : tnode.children) {
				child.parent = tnode;// tnode=node;
			}
			godown(node);
		}
	}

	public void godown(ASTNode node) {
		tnode = node;
	}

	public void goup() {
		if (tnode.parent != null)
			tnode = tnode.parent;
		else
			System.out.println("nullprent!!");
	}

	public void addIDName(String name) {
		tnode.IDName = name;
	}

	public void closebuffer() {
		try {
			ast_file.bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
