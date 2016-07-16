package clubcf.model;

import clubcf.dao.service.ServiceDAO;

public class ServicePair {
	private long clusterID;
	private long unRatedServiceID;
	private long otherServiceID;
	private String clusteName;
	private String unRatedServiceName;
	private String otherServiceName;
	private double ratingSimilarity;
	private double enhancedRatingSimilarity;
	
	public ServicePair(long unRatedServiceID, long otherServiceID,long clusterID) {
		setOtherServiceID(otherServiceID);
		setUnRatedServiceID(unRatedServiceID);
		setClusterID(clusterID);
		setServiceName(getUnRatedServiceID(),false);
		setServiceName(getOtherServiceID(),true);
	}
	
	private void setServiceName(long id, boolean other) {
		ServiceDAO dao = new ServiceDAO();
		if(other)
			setOtherServiceName(dao.getServiceName(id));
		else
			setUnRatedServiceName(dao.getServiceName(id));
		
	}
	
	public long getClusterID() {
		return clusterID;
	}
	
	public void setClusterID(long clusterID) {
		this.clusterID = clusterID;
	}
	
	public long getUnRatedServiceID() {
		return unRatedServiceID;
	}
	
	public void setUnRatedServiceID(long unRatedServiceID) {
		this.unRatedServiceID = unRatedServiceID;
	}
	
	public long getOtherServiceID() {
		return otherServiceID;
	}
	
	public void setOtherServiceID(long otherServiceID) {
		this.otherServiceID = otherServiceID;
	}
	
	public String getClusteName() {
		return clusteName;
	}
	
	public void setClusteName(String clusteName) {
		this.clusteName = clusteName;
	}
	
	public String getUnRatedServiceName() {
		return unRatedServiceName;
	}
	
	public void setUnRatedServiceName(String unRatedServiceName) {
		this.unRatedServiceName = unRatedServiceName;
	}
	
	public String getOtherServiceName() {
		return otherServiceName;
	}
	
	public void setOtherServiceName(String otherServiceName) {
		this.otherServiceName = otherServiceName;
	}
	
	public double getRatingSimilarity() {
		return ratingSimilarity;
	}
	
	public void setRatingSimilarity(double ratingSimilarity) {
		this.ratingSimilarity = ratingSimilarity;
	}
	
	public double getEnhancedRatingSimilarity() {
		return enhancedRatingSimilarity;
	}
	
	public void setEnhancedRatingSimilarity(double enhancedRatingSimilarity) {
		this.enhancedRatingSimilarity = enhancedRatingSimilarity;
	}
	
	public long getInterSection() {
		return 0;
	}
	
	public long getRatedUsersCount(long serviceID) {
		return 0;
	}
}
