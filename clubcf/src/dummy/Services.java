package dummy;
public class Services {
	private float s1;
	private float s2;
	private float s3;
	private float s4;
	private float s5;
	private float s6;
	private float s7;
	private float s8;
	private float s9;
	private float s10;

	private long serviceID;
	private String serviceName;
	
	public float getS1() {
		return s1;
	}

	public void setS1(float s1) {
		this.s1 = s1;
	}

	public float getS2() {
		return s2;
	}

	public void setS2(float s2) {
		this.s2 = s2;
	}

	public float getS3() {
		return s3;
	}

	public void setS3(float s3) {
		this.s3 = s3;
	}

	public float getS4() {
		return s4;
	}

	public void setS4(float s4) {
		this.s4 = s4;
	}

	public float getS5() {
		return s5;
	}

	public void setS5(float s5) {
		this.s5 = s5;
	}

	public float getS6() {
		return s6;
	}

	public void setS6(float s6) {
		this.s6 = s6;
	}

	public float getS7() {
		return s7;
	}

	public void setS7(float s7) {
		this.s7 = s7;
	}

	public float getS8() {
		return s8;
	}

	public void setS8(float s8) {
		this.s8 = s8;
	}

	public float getS9() {
		return s9;
	}

	public void setS9(float s9) {
		this.s9 = s9;
	}

	public float getS10() {
		return s10;
	}

	public void setS10(float s10) {
		this.s10 = s10;
	}

	public String toString() {
        return String.format("%.3f", getS1()) +  "\t" + String.format("%.3f", getS2()) + "\t" + String.format("%.3f", getS3()) + "\t" + String.format("%.3f", getS4()) + "\t" + String.format("%.3f", getS5()) + "\t" + String.format("%.3f", getS6()) + "\t" + String.format("%.3f", getS7()) + "\t" + String.format("%.3f", getS8()) + "\t" + String.format("%.3f", getS9()) + "\t" + String.format("%.3f", getS10());
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
