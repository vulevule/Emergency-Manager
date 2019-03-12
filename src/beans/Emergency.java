package beans;

import java.util.ArrayList;
import java.util.Date;

public class Emergency implements EmergencyLevel, EmergencyState{
	private int id;
	private String name;
	private String municipality;
	private String description;
	private Date dateTime;
	private String location;
	private Territory territory;
	private Level level;
	private String picture;
	private State state;
	private Volunteer volunteer;
	private ArrayList<Comment> comments;
	
	public Emergency() {}
	
	public Emergency(String name, String municipality, String description, String location, Territory territory,
			Level level, String picture, State state, Volunteer volunteer) {
		super();
		this.id = 0;
		this.name = name;
		this.municipality = municipality;
		this.description = description;
		this.location = location;
		this.territory = territory;
		this.level = level;
		this.picture = picture;
		this.state = state;
		this.volunteer = volunteer;
		this.dateTime = new Date();
		this.comments = new ArrayList<Comment>();
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMunicipality() {
		return municipality;
	}
	public void setMunicipality(String municipality) {
		this.municipality = municipality;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Territory getTerritory() {
		return territory;
	}
	public void setTerritory(Territory territory) {
		this.territory = territory;
	}
	public Level getLevel() {
		return level;
	}
	public void setLevel(Level level) {
		this.level = level;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public State getState() {
		return state;
	}
	public void setState(State state) {
		this.state = state;
	}
	public Volunteer getVolunteer() {
		return volunteer;
	}
	public void setVolunteer(Volunteer volunteer) {
		this.volunteer = volunteer;
	}

	public ArrayList<Comment> getComments() {
		return comments;
	}

	public void setComments(ArrayList<Comment> comments) {
		this.comments = comments;
	}
	
}
