package beans;

public class VolunteerDTO extends User implements VolunteerState {
	private State state;
	private String territory;
	
	public VolunteerDTO() {}

	public VolunteerDTO(String username, String password, String name, String surname, String phoneNumber, String email,
			String picture, State state, String territory) {
		super(username, password, name, surname, phoneNumber, email, picture);
		this.state = state;
		this.territory = territory;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	@Override
	public String toString() {
		return "VolunteerDTO [state=" + state + ", territory=" + territory + ", username=" + username + ", password="
				+ password + ", name=" + name + ", surname=" + surname + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", picture=" + picture + ", loggedIn=" + loggedIn + "]";
	}
	
	
}
