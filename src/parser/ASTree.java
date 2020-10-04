package parser;
 	import java.io.IOException;
import java.util.*;

import helpers.contentTable;
import helpers.filehandler;

public class ASTree {
	static contentTable ASTTable= new contentTable();
	ASTNode root;
	ASTNode currentNode;
	int currentNodeIndex = 0;
	ArrayList<ASTNode> varStatNew= new ArrayList<>();
	ArrayList<ASTNode> exprNode= new ArrayList<>();
	ArrayList<ASTNode> fparmsNode= new ArrayList<>();
	ArrayList<ASTNode> varDeclNode= new ArrayList<>();

	public ASTree() {
		root = null;
		currentNode = null;
		ArrayList<String> tableHeader= new ArrayList<>(Arrays.asList("Node","Kind","Token","Parent Node","Line"));
		ASTTable.addHeader(tableHeader);
	}

	public void addNode(String nodename, String kind, String pIDName, String linenum) {
		ASTNode node = new ASTNode(nodename, kind, linenum);
		node.NodeIndex = currentNodeIndex++;
		node.Token = pIDName;
		ArrayList<String> row;
		if (root == null) {
			root = node;
			currentNode = node;
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, "no parent",linenum));
		} else {
			currentNode.children.add(node);
			for (ASTNode child : currentNode.children) {
				child.parent = currentNode;// tnode=node;
			}
			goDown(node);
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, node.parent.NodeName,linenum));
		}
		ASTTable.addRow(row);
	}

	public void goDown(ASTNode node) {
		currentNode = node;
	}

	public void goUp() {
		if (currentNode.parent != null)
			currentNode = currentNode.parent;
		else
			System.out.println("Null parent error!");
	}
}
