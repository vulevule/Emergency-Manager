package beans;

public class Volunteer extends User implements VolunteerState {
	private State state;
	private Territory territory;
	
	public Volunteer() {}
	
	public Volunteer(String username, String password, String name, String surname, String phoneNumber, String email,
			String picture, State state, Territory territory) {
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

	public Territory getTerritory() {
		return territory;
	}

	public void setTerritory(Territory territory) {
		this.territory = territory;
	}

	@Override
	public String toString() {
		return "Volunteer [state=" + state + ", territory=" + territory.getName() + ", username=" + username + ", password="
				+ password + ", name=" + name + ", surname=" + surname + ", phoneNumber=" + phoneNumber + ", email="
				+ email + ", picture=" + picture + "]";
	}
	
	public Volunteer register(Users users) {
		for (Volunteer vol : users.getVolunteers()) {
			if (vol.getUsername().equals(username)) {
				loggedIn = false;
				return null;
			}
		}
		users.getVolunteers().add(this);
		
		loggedIn = true;
		return this;
	}
}
