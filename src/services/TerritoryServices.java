package services;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

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

import beans.Emergencies;
import beans.Emergency;
import beans.Territories;
import beans.Territory;
import beans.Users;
import beans.Volunteer;


@Path("/territories")
public class TerritoryServices {
	
	@Context
	HttpServletRequest request;
	@Context
	ServletContext ctx;
	
	@GET
	@Path("/getTerritories")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Territory> getAllTerritories() throws IOException {
		return getTerritories().getTerritories();
	}
	
	@GET
	@Path("/getTerritory/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Territory getTerritory(@PathParam("name") String name) throws IOException {
		name = java.net.URLDecoder.decode(name, "UTF-8");
		for (Territory iter : getTerritories().getTerritories()) {
			if (iter.getName().equals(name)) {
				return iter;
			}
		}
		
		return null;
	}
	
	@GET
	@Path("/getEmTer/{name}")
	@Produces(MediaType.APPLICATION_JSON)
	public Collection<Emergency> getEmTer(@PathParam("name") String name) throws IOException {
		name = java.net.URLDecoder.decode(name, "UTF-8");
		ArrayList<Emergency> retVal = new ArrayList<Emergency>();
		for (Emergency iter : getEmergencies().getEmergencies()) {
			if (iter.getTerritory().getName().equals(name)) {
				retVal.add(iter);
			}
		}
		
		System.out.println("Num of emergencies for " + name + ": " + retVal.size());
		return retVal;
	}
	
	@POST
	@Path("/createTer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Territory createTerritory(Territory ter) throws IOException {
		ter.setName(java.net.URLDecoder.decode(ter.getName(), "UTF-8"));
		return getTerritories().add(ter);
	}
	
	@POST
	@Path("/updateTer")
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	public Territory updateTerritory(Territory ter) throws IOException {
		ter.setName(java.net.URLDecoder.decode(ter.getName(), "UTF-8"));
		for (Territory iter : getTerritories().getTerritories()) {
			if (iter.getName().equals(ter.getName())) {
				iter.setAreaSize(ter.getAreaSize());
				iter.setResidentsNumber(ter.getResidentsNumber());
				updateTerritoryEverywhere(iter);
				return iter;
			}
		}
		
		return null;
	}
	
	@GET
	@Path("/save")
	@Produces(MediaType.TEXT_PLAIN)
	public String save() {
		try {
			String retVal1 = getTerritories().writeToFile(ctx.getRealPath("") + "/data/territories.json");
			String retVal2 = getTerritories().writeToFile("C:\\Users\\Vule\\Web programiranje\\EmergencyManager\\WebContent\\data\\territories.json");
			return retVal1 + " " + retVal2;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
			return "ERROR " + e.getMessage();
		}
	}
	
	private void updateTerritoryEverywhere(Territory ter) throws IOException {
		for (Volunteer vol : getUsers().getVolunteers()) {
			if (vol.getTerritory().getName().equals(ter.getName())) {
				vol.setTerritory(ter);
			}
		}
		
		for (Emergency em : getEmergencies().getEmergencies()) {
			if (em.getTerritory().getName().equals(ter.getName())) {
				em.setTerritory(ter);
			}
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
