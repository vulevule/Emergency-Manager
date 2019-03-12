package services;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

import beans.Comment;
import beans.Emergencies;
import beans.Emergency;
import beans.EmergencyDTO;
import beans.EmergencyLevel;
import beans.EmergencyState;
import beans.Territories;
import beans.Territory;
import beans.User;
import beans.UserComment;
import beans.Users;
import beans.Volunteer;

@Path("/emergencies")
public class EmergencyServices {
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/test")
	@Produces(MediaType.TEXT_PLAIN)
	public String test() {
		System.out.println("Hello Jersey");
		return "Hello Jersey";
	}
	
	@GET
	@Path("/save")
	@Produces(MediaType.TEXT_PLAIN)
	public String save() {
		try {
			String retVal1 = getEmergencies().writeToFile(ctx.getRealPath("") + "/data/emergencies.json");
			String retVal2 = getEmergencies().writeToFile("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent\\data\\emergencies.json");
			return retVal1 + " " + retVal2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "ERROR " + e.getMessage();
		}
	}
	
	@GET
	@Path("/getEmergencies")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Emergency> getAllEmergencies() throws IOException {
		return getEmergencies().getEmergencies();
	}
	
	@POST
	@Path("/addEmergency")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String addEmergency(EmergencyDTO em) throws IOException {
		String imageFile = em.getPicture();
		
		Emergency emToAdd = new Emergency(em.getName(), em.getMunicipality(), em.getDescription(), em.getLocation(), null, null, "", EmergencyState.State.ACTIVE, null);		
		emToAdd.setId(getEmergencies().getEmergencies().size() + 1);
		emToAdd.setPicture(emToAdd.getId() + ".jpg");
		
		String name = java.net.URLDecoder.decode(em.getTerritory(), "UTF-8");
		for (Territory iter : getTerritories().getTerritories()) {
			if (iter.getName().equals(name)) {
				emToAdd.setTerritory(iter);
				break;
			}
		}
		
		if (em.getLevel().equals("BLUE")) {
			emToAdd.setLevel(EmergencyLevel.Level.BLUE);
		} else if (em.getLevel().equals("ORANGE")) {
			emToAdd.setLevel(EmergencyLevel.Level.ORANGE);
		} else {
			emToAdd.setLevel(EmergencyLevel.Level.RED);
		}
		
		getEmergencies().add(emToAdd);
				
		return saveImage(imageFile, emToAdd.getId()+"");
	}
	
	@GET
	@Path("/archive/{id}")
	@Produces(MediaType.TEXT_PLAIN)
	public String archiveEmergency(@PathParam("id") int id) throws IOException {
		for (Emergency em : getEmergencies().getEmergencies()) {
			if (id == em.getId()) {
				em.setState(EmergencyState.State.ARCHIVED);
				return "OK";
			}
		}
		
		return "ERROR";
	}
	
	@POST
	@Path("/comment")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String comment(UserComment uc) throws IOException {
		String text = java.net.URLDecoder.decode(uc.getText(), "UTF-8");
		int emId = uc.getEmergencyId();
		
		Comment comment = new Comment(text, (User) ctx.getAttribute("loggedUser"));
		
		for(int i = 0; i < getEmergencies().getEmergencies().size(); i++) {
			if (getEmergencies().getEmergencies().get(i).getId() == emId) {
				getEmergencies().getEmergencies().get(i).getComments().add(comment);
				return "OK";
			}
		}
		
		return "ERROR";
	}
	
	@GET
	@Path("/saveVolunteer/{id}/{username}")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String saveVolunteer(@PathParam("id") int id, @PathParam("username") String username) throws IOException {
		for (Emergency em : getEmergencies().getEmergencies()) {
			if (em.getId() == id) {
				em.setVolunteer(getVolunteer(username));
				return "OK";
			}
		}
		
		return "ERROR";
	}
	
	private Volunteer getVolunteer(String username) throws IOException {
		for (Volunteer v : getUsers().getVolunteers()) {
			if (v.getUsername().equals(username)) {
				return v;
			}
		}
		
		return null;
	}
	
	private Emergencies getEmergencies() throws IOException {
		Emergencies emergencies = (Emergencies) ctx.getAttribute("emergencies");
		if (emergencies == null) {
			emergencies = new Emergencies(ctx.getRealPath(""));
			ctx.setAttribute("emergencies", emergencies);
		}	
		return emergencies;
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
	
	private Users getUsers() throws IOException {
		Users users = (Users) ctx.getAttribute("users");
		if (users == null) {
			System.out.println("Users je NULL");
			users = new Users(ctx.getRealPath(""));
			ctx.setAttribute("users", users);
		}	
		return users;
	}
	
	private String saveImage(String imageFile, String name) {
		String path1 = ctx.getRealPath("") + "pictures/emergencies/" + name + ".jpg";
		String path2 = "C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent\\pictures\\emergencies\\" + name + ".jpg";
	
		System.out.println("PATH: " + path1);
		
		File outputFile1 = new File(path1);
		File outputFile2 = new File(path2);
		String base64Image = imageFile.split(",")[1];
		byte[] imageBytes = javax.xml.bind.DatatypeConverter.parseBase64Binary(base64Image);
		BufferedImage img;
		try {
			img = ImageIO.read(new ByteArrayInputStream(imageBytes));				
			System.out.println("SUCCESS: " + ImageIO.write(img, "jpg", outputFile1));;
			ImageIO.write(img, "jpg", outputFile2);
			
			return "OK";
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "ERROR while saving image " + e.getMessage();
		}	
	}
}
