package clubcf.model;
public class Services {

	private long serviceID;
	private String serviceName;
	private String stemWord;
	private String apiName;
	
	
	public String getStemWord() {
		return stemWord;
	}

	public void setStemWord(String stemWord) {
		this.stemWord = stemWord;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public long getServiceID() {
		return serviceID;
	}

	public void setServiceID(long serviceID) {
		this.serviceID = serviceID;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

}
