package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Users {
	private ArrayList<Administrator> admins = new ArrayList<Administrator>();
	private ArrayList<Volunteer> volunteers = new ArrayList<Volunteer>();
	
	public Users() throws IOException {
		//this("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent");
	}
	
	public Users(String path) throws IOException {		
		String jsonString = loadFromFile(path + "/data/users.json");
		System.out.println(jsonString);
		ObjectMapper mapper = new ObjectMapper();		
		admins = mapper.readValue(jsonString, Users.class).getAdmins();
		volunteers = mapper.readValue(jsonString, Users.class).getVolunteers();
		System.out.println("Admins: " + admins.size());
		System.out.println("Volunteers: " + volunteers.size());
	}
	
	public String loadFromFile(String path) throws IOException {
		String jsonString = "";
		String line = "";
		File file = new File(path);
		BufferedReader in = new BufferedReader(new FileReader(file));
		while ((line = in.readLine()) != null) {
			jsonString += line.trim();
		}
		in.close();
		
		return jsonString;
	}
	
	public String writeToFile(String path) {	
		try {
			File file = new File(path);
			PrintWriter out = new PrintWriter(file);		
			ObjectMapper mapper = new ObjectMapper();		
			String jsonString = mapper.writeValueAsString(this);
			jsonString = jsonString.replaceAll("\"loggedIn\":true", "\"loggedIn\":false");
			out.write(jsonString);
			out.close();
			
			return "OK";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "ERROR " + e.getMessage();
		}	
	}

	public ArrayList<Administrator> getAdmins() {
		return admins;
	}

	public void setAdmins(ArrayList<Administrator> admins) {
		this.admins = admins;
	}

	public ArrayList<Volunteer> getVolunteers() {
		return volunteers;
	}

	public void setVolunteers(ArrayList<Volunteer> volunteers) {
		this.volunteers = volunteers;
	}
}

