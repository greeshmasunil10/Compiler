package lexical_analyser;

import java.util.HashMap;

public class TokenNames {
	
	public HashMap<String,String> name = new HashMap<>();
	public TokenNames() {
		name.put("&&", "Logical_AND_Operator");
	}

}
