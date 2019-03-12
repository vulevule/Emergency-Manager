package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Emergencies {
	private ArrayList<Emergency> emergencies = new ArrayList<Emergency>();
	
	public Emergencies() throws IOException {
		//this("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent");
	}
	
	public Emergencies(String path) throws IOException {
		String jsonString = loadFromFile(path + "/data/emergencies.json");
		System.out.println(jsonString);
		ObjectMapper mapper = new ObjectMapper();
		emergencies = mapper.readValue(jsonString, Emergencies.class).getEmergencies();
		System.out.println("Emergencies: " + emergencies.size());
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
			out.write(jsonString);
			out.close();
			
			return "OK";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "ERROR " + e.getMessage();
		}	
	}
	
	public boolean add(Emergency em) {
		emergencies.add(em);
		return true;
	}
	
	public ArrayList<Emergency> getEmergencies() {
		return emergencies;
	}

	public void setEmergencies(ArrayList<Emergency> emergencies) {
		this.emergencies = emergencies;
	}
	
	
}
