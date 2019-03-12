package beans;

public class UserComment {
	private int emergencyId;
	private String text;
	
	public UserComment() {
		// TODO Auto-generated constructor stub
	}

	public UserComment(int emergencyId, String text) {
		this.emergencyId = emergencyId;
		this.text = text;
	}

	public int getEmergencyId() {
		return emergencyId;
	}

	public void setEmergencyId(int emergencyId) {
		this.emergencyId = emergencyId;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	
}
