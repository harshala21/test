package clubcf.model;

import clubcf.dao.service.ServiceDAO;

public class Services {

	private long serviceID;
	private String serviceName;
	private String stemWord;
	private String apiName;
	private String symanticStemWords;
	private double predictedRatingDifference;
	
	
	public String getSymanticStemWords() {
		return symanticStemWords;
	}

	public void setSymanticStemWords(String symanticStemWords) {
		this.symanticStemWords = symanticStemWords;
	}

	public String getStemWord() {
		return stemWord;
	}

	public void setStemWord(String stemWord) {
		this.stemWord = stemWord;
	}

	public String getApiName() {
		return apiName;
	}
	
	public Services(){
		
	}
	public Services(long serviceID,String serviceName,double predictedRatingDifference,String apiName){
		setServiceID(serviceID);
		setServiceName(serviceName);
		setPredictedRatingDifference(predictedRatingDifference);
		setApiName(apiName);
	}

	public Services(long serviceID, String serviceName, String stemWord, String apiName) {
		this.serviceID = serviceID;
		this.serviceName = serviceName;
		this.stemWord = stemWord;
		this.apiName = apiName;
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

	public double getPredictedRatingDifference() {
		return predictedRatingDifference;
	}

	public void setPredictedRatingDifference(double predictedRatingDifference) {
		this.predictedRatingDifference = predictedRatingDifference;
	}

}
