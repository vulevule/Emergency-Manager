package beans;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {
	protected String username;
	protected String password;
	protected String name;
	protected String surname;
	protected String phoneNumber;
	protected String email;
	protected String picture;
	protected boolean loggedIn;
	
	public User() {}

	public User(String username, String password, String name, String surname, String phoneNumber, String email,
			String picture) {
		super();
		this.username = username;
		this.password = password;
		this.name = name;
		this.surname = surname;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.picture = picture;
		this.loggedIn = false;
	}
	
	public User login(Users users) {
		for (Administrator admin : users.getAdmins()) {
			if (admin.getUsername().equals(username) && admin.getPassword().equals(password)) {
				admin.loggedIn = true;
				System.out.println("Logged in as " + admin.username);
				return admin;
			}
		}
		
		for (Volunteer vol : users.getVolunteers()) {
			if (vol.getUsername().equals(username) && vol.getPassword().equals(password)) {
				if (vol.getState().equals(VolunteerState.State.BLOCKED)) {
					vol.loggedIn = false;
					System.out.println("User " + vol.username + " BLOCKED!");
					return vol;
				}
				vol.loggedIn = true;
				System.out.println("Logged in as " + vol.username);
				return vol;
			}
		}
		
		System.out.println("Failed to log in");
		loggedIn = false;
		return null;
	}

	public void logOff() {
		loggedIn = false;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isLoggedIn() {
		return loggedIn;
	}

	
	
	
	
}
