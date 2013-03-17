package shenkar.ac.il.model;

/**
 * @author Zohar Nyego
 */
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

/**
 * Task model bean class. implement {@link java.io.Serializable}
 */
@XmlRootElement
@Entity
@Table(name = "tasks", schema = "as_23a57d2f8be57b3")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Long taskId;
	@Column(nullable = false)
	private String userId;
	private String description;
	private String location;
	private Long   date;

	@ManyToOne
	@JoinColumn(name = "userId", nullable = false, insertable = false, updatable = false, referencedColumnName = "userId")
	private User user;

	public Task() {
		super();
	}

	public Task(Long taskId) {
		super();
		this.taskId = taskId;
	}

	
	public Task(Long taskId, String userId, String description,
			String location, Long date) {
		super();
		this.taskId = taskId;
		this.userId = userId;
		this.description = description;
		this.location = location;
		this.date = date;
	}

	public void setTaskId(Long taskId) {
		this.taskId = taskId;
	}

	public Long getTaskId() {
		return taskId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getDate() {
		return date;
	}

	public void setDate(Long date) {
		this.date = date;
	}	

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", userId=" + userId
				+ ", description=" + description + ", location=" + location
				+ ", date=" + date + "]";
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("taskId", taskId);
			jsonObject.put("userId", userId);
			jsonObject.put("description", description);
			jsonObject.put("location", location);
			jsonObject.put("date", date);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	
}
