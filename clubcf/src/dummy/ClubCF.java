package dummy;

public class ClubCF {
	private String no;
	private String name;
	private String apis;
	private String tags;
	private String stemWord;

	public ClubCF() {
	}

	public ClubCF(String no, String name, String apis, String tags) {
		this.no = no;
		this.name = name;
		this.apis = apis;
		this.tags = tags;
	}

	@Override
	public String toString() {
		String value = ""; 
			   value += "No >>\t" + getNo() + "\n";
			   value += "Name >>\t" + getName() + "\n";
			   value += "API >>\t" + getApis() + "\n";
			   value += "Tags >>\t" + getTags() + "\n";
			   value += "Stem >>\t" + getStemWord() + "\n";
			   //value = getNo() + "\t\t" + getName() + "\t\t" + getApis() + "\t\t" + getTags() + "\t\t" + getStemWord();
		return value;
	}

	public String getNo() {
		return no;
	}

	public String getName() {
		return name;
	}

	public String getApis() {
		return apis;
	}

	public String getTags() {
		return tags;
	}

	public void setNo(String no) {
		this.no = no;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setApis(String apis) {
		this.apis = apis;
	}

	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getStemWord() {
		return stemWord;
	}

	public void setStemWord(String stemWord) {
		this.stemWord = stemWord;
	}
}
