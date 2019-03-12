package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Territories {
	private ArrayList<Territory> territories = new ArrayList<Territory>();
	
	public Territories() throws IOException {
		//this("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent"); 
	}
	
	public Territories(String path) throws IOException {
		
		String jsonString = loadFromFile(path + "/data/territories.json");
		System.out.println(jsonString);
		ObjectMapper mapper = new ObjectMapper();
		territories = mapper.readValue(jsonString, Territories.class).getTerritories();
		System.out.println("Territories: " + territories.size());
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

	public Territory add(Territory ter) {
		for (Territory iter : territories) {
			if (iter.getName().equals(ter.getName())) {
				return null;
			}
		}
		
		territories.add(ter);
		return ter;
	}
	
	public ArrayList<Territory> getTerritories() {
		return territories;
	}

	public void setTerritories(ArrayList<Territory> territories) {
		this.territories = territories;
	}
}
