package beans;

public class Administrator extends User{
	
	public Administrator() {}
	
	@Override
	public String toString() {
		return "Administrator [username=" + username + ", password=" + password + ", name=" + name + ", surname="
				+ surname + ", phoneNumber=" + phoneNumber + ", email=" + email + ", picture=" + picture + "]";
	}
	
}
