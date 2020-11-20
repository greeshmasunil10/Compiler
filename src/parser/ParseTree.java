package parser;
import java.io.IOException;
import java.util.*;

import helpers.contentTable;
import helpers.filehandler;

public class ParseTree {
	contentTable parse_table= new contentTable();
	TreeNode parse_root;
	TreeNode ast_root;
	TreeNode current_parse_node;
	TreeNode current_ast_node;
	int parse_node_index = 0;
	ArrayList<TreeNode> ast_trees= new ArrayList<>();
	ArrayList<TreeNode> varStatNew= new ArrayList<>();
	ArrayList<TreeNode> exprNode= new ArrayList<>();
	ArrayList<TreeNode> fparmsNode= new ArrayList<>();
	ArrayList<TreeNode> varDeclNode= new ArrayList<>();

	public ParseTree() {
		parse_root = null;
		ArrayList<String> tableHeader= new ArrayList<>(Arrays.asList("Node","Kind","Token","Parent Node","Line"));
		parse_table.addHeader(tableHeader);
	}

	public void addNodeAsTerminal(String nodename, String kind, String pIDName, String linenum) {
		TreeNode node = new TreeNode(nodename, kind, linenum);
		TreeNode astnode = new TreeNode(nodename, kind, linenum);
		node.NodeIndex = parse_node_index++;
		node.Token = pIDName;
		astnode.Token = pIDName;
		ArrayList<String> row;
		if (parse_root == null) {
			parse_root = node;
			current_parse_node = node;
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, "no parent",linenum));
		} else {
			current_parse_node.children.add(node);
			for (TreeNode child : current_parse_node.children) {
				child.parent = current_parse_node;// tnode=node;
			}
			goDown(node);
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, node.parent.NodeName,linenum));
		}
		parse_table.addRow(row);
	}

	public void addNodeAsNT(String nodename, String kind, String pIDName) {
		TreeNode node = new TreeNode(nodename, kind);
		node.NodeIndex = parse_node_index++;
		node.Token = pIDName;
		ArrayList<String> row;
		if (parse_root == null) {
			parse_root = node;
			current_parse_node = node;
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, "no parent"));
		} else {
			current_parse_node.children.add(node);
			for (TreeNode child : current_parse_node.children) {
				child.parent = current_parse_node;// tnode=node;
			}
			goDown(node);
			row= new ArrayList<>(Arrays.asList(node.NodeName, node.NodeKind, node.Token, node.parent.NodeName,""));
		}
		parse_table.addRow(row);
	}

	public void goDown(TreeNode node) {
		current_parse_node = node;
	}

	public void goUp() {
		if (current_parse_node.parent != null)
			current_parse_node = current_parse_node.parent;
		else
			System.out.println("Null parent error!");
	}
	public void getASTtree(TreeNode node) {
		if(node.NodeKind!="NT") {
			TreeNode astnode= node;
			
		}
		for (TreeNode child : node.children)
			getASTtree(child);
		if(node.NodeKind!="NT") {
			this.current_ast_node=node;
		}
	}
	public void traverse(TreeNode node) {
		System.out.println(node.NodeName);
		for (TreeNode child : node.children)
			traverse(child);
	}
}
