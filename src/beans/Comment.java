package beans;

import java.util.Date;

public class Comment {
	private String text;
	private Date date;
	private User user;
	
	public Comment() {}
	
	public Comment(String text, User user) {
		this.text = text;
		this.user = user;
		this.date = new Date();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
