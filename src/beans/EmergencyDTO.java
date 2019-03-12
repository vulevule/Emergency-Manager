package beans;

import beans.EmergencyLevel.Level;

public class EmergencyDTO {
	
	private String name;
	private String municipality;
	private String description;
	private String location;
	private String territory;
	private String level;
	private String picture;
	
	public EmergencyDTO() {
	}

	public EmergencyDTO(String name, String municipality, String description, String location, String territory,
			String level, String picture) {
		super();
		this.name = name;
		this.municipality = municipality;
		this.description = description;
		this.location = location;
		this.territory = territory;
		this.level = level;
		this.picture = picture;
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

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getTerritory() {
		return territory;
	}

	public void setTerritory(String territory) {
		this.territory = territory;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}
	
	
}
