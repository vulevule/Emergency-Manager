package beans;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Test {

	public static void main(String[] args) throws IOException {
		String path = "src/data/users.json";
		String jsonStrin = "";
		String line = "";
		File file = new File(path);
		BufferedReader in = new BufferedReader(new FileReader(file));
		while ((line = in.readLine()) != null) {
			jsonStrin += line.trim();
		}
		System.out.println(jsonStrin);
		ObjectMapper mapper = new ObjectMapper();
		Users users = mapper.readValue(jsonStrin, Users.class);
		
		for (Administrator admin : users.getAdmins()) {
			System.out.println(admin.toString());
		}
		
		for (Volunteer volunteer : users.getVolunteers()) {
			System.out.println(volunteer.toString());
		}
		
		in.close();
	}

}
