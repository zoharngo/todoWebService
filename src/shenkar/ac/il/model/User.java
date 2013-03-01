package shenkar.ac.il.model;
/**
 * @author Zohar Nyego
 */
import java.io.Serializable;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

import javax.persistence.CascadeType;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

@XmlRootElement
@Entity
@Table(name = "Users", schema = "tododb")
/** 
 * User model bean class.
 * implement {@link java.io.Serializable}
 */
public class User implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private String userId;
	private String firstName;
	private String lastName;
	private String userPass;
	@Column(unique = true)
	private String userEmail;
	private long userAgent;

	@OneToMany(targetEntity = Task.class, cascade = CascadeType.ALL, mappedBy = "user", fetch = FetchType.EAGER, orphanRemoval = true)
	private Collection<Task> userTasks;

	public User() {
		super();
	}

	public User(String userId, String firstName, String lastName,
			String userPass, String userEmail) {
		super();
		this.userId = userId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userPass = userPass;
		this.userEmail = userEmail;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserPass() {
		return userPass;
	}

	public void setUserPass(String userPass) {
		this.userPass = userPass;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public Collection<Task> getUserTasks() {
		return userTasks;
	}

	public void setUserTasks(Collection<Task> userTasks) {
		this.userTasks = userTasks;
	}

	public long getUserAgent() {
		return userAgent;
	}

	public void setUserAgent(long userAgent) {
		this.userAgent = userAgent;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", firstName=" + firstName
				+ ", lastName=" + lastName + ", userPass=" + userPass
				+ ", userEmail=" + userEmail + ", userAgent=" + userAgent
				+ ", userTasks=" + userTasks + "]";
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("userId", userId);
			jsonObject.put("firstName", firstName);
			jsonObject.put("lastName", lastName);
			jsonObject.put("userPass", userPass);
			jsonObject.put("userEmail", userEmail);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

}
