package services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import beans.Administrator;
import beans.Emergencies;
import beans.Emergency;
import beans.EmergencyState;
import beans.Territories;
import beans.Territory;
import beans.User;
import beans.Users;
import beans.Volunteer;
import beans.VolunteerDTO;
import beans.VolunteerState;


@Path("/users")
public class UserServices {
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/save")
	@Produces(MediaType.TEXT_PLAIN)
	public String save() {
		try {
			String retVal1 = getUsers().writeToFile(ctx.getRealPath("") + "/data/users.json");
			String retVal2 = getUsers().writeToFile("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent\\data\\users.json");
			return retVal1 + " " + retVal2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "ERROR " + e.getMessage();
		}
	}
	
	@GET
	@Path("/getUser/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public User getUser(@PathParam("username") String username) throws IOException {
		for (User u : getUsers().getAdmins()) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		
		for (User u : getUsers().getVolunteers()) {
			if (u.getUsername().equals(username)) {
				return u;
			}
		}
		
		return null;
	}
	
	@GET
	@Path("/blockUser/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String blockUser(@PathParam("username") String username) throws IOException {				
		String retVal = "ERROR";
		for (Volunteer u : getUsers().getVolunteers()) {
			if (u.getUsername().equals(username)) {												
				u.setState(VolunteerState.State.BLOCKED);
				retVal = "OK";
				break;
			}
		}
		
		for (int i = 0; i < getEmergencies().getEmergencies().size(); i++) {
			Emergency em = getEmergencies().getEmergencies().get(i);
			if (em.getVolunteer() != null ) {
				if (em.getVolunteer().getUsername().equals(username)) {
					em.setVolunteer(null);
					getEmergencies().getEmergencies().set(i, em);
					retVal = "OK";
					break;
				}
			}		
		}
		
		return retVal;
	}
	
	@GET
	@Path("/unblockUser/{username}")
	@Produces(MediaType.TEXT_PLAIN)
	public String unblockUser(@PathParam("username") String username) throws IOException {				
		String retVal = "ERROR";
		for (Volunteer u : getUsers().getVolunteers()) {
			if (u.getUsername().equals(username)) {
				u.setState(VolunteerState.State.NORMAL);
				retVal = "OK";
				break;
			}
		}
		
		return retVal;
	}
	
	@GET
	@Path("/myEmergencies/{username}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Emergency> getEmergenciesForVolunteer(@PathParam("username") String username) throws IOException {
		ArrayList<Emergency> retVal = new ArrayList<Emergency>();
		for (Emergency em : getEmergencies().getEmergencies()) {
			if (em.getVolunteer() != null) {
				if (em.getVolunteer().getUsername().equals(username)) {
					retVal.add(em);
				}
			}
		}
		
		return retVal;
	}
	
	@POST
	@Path("/saveAdmin")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Administrator saveAdmin(Administrator user) throws IOException {
		for (int i = 0; i < getUsers().getAdmins().size(); i++) {
			if (getUsers().getAdmins().get(i).getUsername().equals(user.getUsername())) {
				Administrator adminToAdd = new Administrator();
				adminToAdd.setName(user.getName());
				adminToAdd.setSurname(user.getSurname());
				adminToAdd.setUsername(user.getUsername());
				adminToAdd.setPassword(user.getPassword());
				adminToAdd.setPhoneNumber(user.getPhoneNumber());
				adminToAdd.setEmail(user.getEmail());
				adminToAdd.setPicture("");
				
				if (!user.getPicture().equals(user.getUsername() + ".jpg")) {
					saveImage(user.getPicture(), user.getUsername());
				}
				adminToAdd.setPicture(user.getUsername() + ".jpg");
				
				getUsers().getAdmins().set(i, adminToAdd);
				
				return adminToAdd;
			}
		}
		
		return null;
	}
	
	@POST
	@Path("/saveVolunteer")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Volunteer saveVolunteer(VolunteerDTO vol) throws IOException {
		for (int i = 0; i < getUsers().getVolunteers().size(); i++) {
			if (getUsers().getVolunteers().get(i).getUsername().equals(vol.getUsername())) {
				Volunteer volToAdd = new Volunteer(vol.getUsername(), vol.getPassword(), vol.getName(), vol.getSurname(), vol.getPhoneNumber(), vol.getEmail(), "", vol.getState(), null);
				
				String name = java.net.URLDecoder.decode(vol.getTerritory(), "UTF-8");
				for (Territory iter : getTerritories().getTerritories()) {
					if (iter.getName().equals(name)) {
						volToAdd.setTerritory(iter);
						break;
					}
				}
				
				if (!vol.getPicture().equals(vol.getUsername() + ".jpg")) {
					saveImage(vol.getPicture(), vol.getUsername());
				}
				volToAdd.setPicture(vol.getUsername() + ".jpg");
				
				getUsers().getVolunteers().set(i, volToAdd);
				
				return volToAdd;
			}
		}
		
		return null;
	}
	
	@GET
	@Path("/getVolunteers")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Volunteer> getVolunteers() throws IOException {
		return getUsers().getVolunteers();
	}
	
	@GET
	@Path("/getAdmins")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Administrator> getAdmins() throws IOException {
		return getUsers().getAdmins();
	}
	
	
	@GET
	@Path("/getLoggedUser")
	@Produces(MediaType.APPLICATION_JSON)
	public User getLoggedUser() throws IOException {
		User user = (User) ctx.getAttribute("loggedUser");
		return user;
	}
	
	@POST
	@Path("/register")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String register(VolunteerDTO volDTO) throws IOException {
		System.out.println("User to add: " + volDTO);
		String imageFile = volDTO.getPicture();
		volDTO.setPicture(volDTO.getUsername() + ".jpg");
		
		Volunteer volToAdd = new Volunteer(volDTO.getUsername(), volDTO.getPassword(), volDTO.getName(), volDTO.getSurname(), volDTO.getPhoneNumber(), volDTO.getEmail(), volDTO.getPicture(), VolunteerState.State.NORMAL, null);
		
		String name = java.net.URLDecoder.decode(volDTO.getTerritory(), "UTF-8");
		for (Territory iter : getTerritories().getTerritories()) {
			if (iter.getName().equals(name)) {
				volToAdd.setTerritory(iter);
				break;
			}
		}
		
		Volunteer vol = volToAdd.register(getUsers());
		
		if (vol != null) {
			ctx.setAttribute("loggedUser", vol);
			return saveImage(imageFile, vol.getUsername());
		} else {
			return "ERROR";
		}	
	}
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String login(User volToAdd) throws IOException {
		User user = volToAdd.login(getUsers());
		
		if (user != null) {
			if (user.getClass().equals(Volunteer.class)) {
				Volunteer temp = (Volunteer) user;
				if (temp.getState().equals(VolunteerState.State.BLOCKED)) {
					return "BLOCKED";
				}
				
			}
			ctx.setAttribute("loggedUser", user);
			return "OK";
		} else {
			return "ERROR";
		}	
	}
	
	@GET
	@Path("/logout")
	@Produces(MediaType.APPLICATION_JSON)
	public String logOut() throws IOException {
		User user = (User) ctx.getAttribute("loggedUser");
		if (user == null) {
			return "ERROR";
		}
		System.out.println(user.getUsername() + " logging out...");
		user.logOff();
		ctx.setAttribute("loggedUser", null);
		return "OK";
	}
	
	private String saveImage(String imageFile, String name) {
		String path1 = ctx.getRealPath("") + "pictures/users/" + name + ".jpg";
		String path2 = "C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent\\pictures\\users\\" + name + ".jpg";
	
		File outputFile1 = new File(path1);
		File outputFile2 = new File(path2);
		String base64Image = imageFile.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));				
			ImageIO.write(img, "jpg", outputFile1);
			ImageIO.write(img, "jpg", outputFile2);
			
			return "OK";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR while saving image " + e.getMessage();
		}	
	}
	
	private Users getUsers() throws IOException {
		Users users = (Users) ctx.getAttribute("users");
		if (users == null) {
			System.out.println("Users je NULL");
			users = new Users(ctx.getRealPath(""));
			ctx.setAttribute("users", users);
		}	
		return users;
	}
	
	private Territories getTerritories() throws IOException {
		Territories territories = (Territories) ctx.getAttribute("territories");
		if (territories == null) {
			System.out.println("Territories je NULL");
			territories = new Territories(ctx.getRealPath(""));
			ctx.setAttribute("territories", territories);
		}	
		return territories;
	}
	
	private Emergencies getEmergencies() throws IOException {
		Emergencies emergencies = (Emergencies) ctx.getAttribute("emergencies");
		if (emergencies == null) {
			emergencies = new Emergencies(ctx.getRealPath(""));
			ctx.setAttribute("emergencies", emergencies);
		}	
		return emergencies;
	}
}
