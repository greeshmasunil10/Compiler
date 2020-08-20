package lexical_analyser;

import java.util.HashMap;

public class TokenNames {
	
	public static HashMap<String,String> names = new HashMap<>();
	public static void init() {
		names.put("+", "Addition operator");
		names.put("-", "Subtraction operator");
		names.put("*", "Multiplication operator");
		names.put("/", "Division operator");
		names.put("&&", "Logical AND Operator");
		names.put("||", "OR operator");
		names.put("(", "Left Paranthisis");
		names.put(")", "Right Paranthiss");
		names.put("{", "Left Braces");
		names.put("}", "Right Braces");
		names.put("[", "Left Bracket");
		names.put("]", "Right Bracket");
		names.put(";", "Semicolon");
		names.put("=", "Assignment operator");
		names.put("==", "Equality operator");
		names.put(">", "Greater than");
		names.put(">=", "Greater than or equal to");
		names.put("<", "Less than");
		names.put("<=", "Less than or equal to");
		names.put("!", "Not operator");
		names.put(",", "Comma");
		names.put(".", "Dot operator");
		names.put("!=", "Not equal to");
//		name.put("", "");
	}
	public static String getTokenName(String token) {
		return names.get(token);
	} 

}
