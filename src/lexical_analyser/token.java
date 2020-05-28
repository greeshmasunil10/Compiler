package lexical_analyser;

public class token {

	private String token_type;
	private String token_name;
	private int linenum;
	public token(String ptype, String pname, int pnum) {
		this.token_type= ptype;
		this.token_name=pname;
		this.linenum=pnum;
	}
	public String getType() {
		return this.token_type;
	}
	public String getName() {
		return this.token_name;
	}
	public int getLineNum() {
		return this.linenum;
	}
	public String getToken() {
		return("line "+this.linenum+" :("+this.token_type+","+this.token_name+")");
	}
}
