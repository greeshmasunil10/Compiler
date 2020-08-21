package lexical_analyser;

public class Token {

	private String token_name;
	private String token;
	private String token_type;
	private int linenum;
	
	public Token(String ptype, String pname, int pnum) {
		this.token_name= ptype;
		this.token=pname;
		this.linenum=pnum;
	}
	public void setToken(String token) {
		this.token=token;
	}
	public void setTokenName(String token) {
		this.token=token;
	}
	public void setTokenType(String token) {
		this.token=token;
	}
	public String getToken() {
		return this.token;
	}
	public String getName() {
		return this.token_name;
	}
	public String getType() {
		return this.token_name;
	}
	public int getLineNum() {
		return this.linenum;
	}
	public String toString() {
		return("line "+this.linenum+" :("+this.token_name+","+this.token+")");
	}
}
