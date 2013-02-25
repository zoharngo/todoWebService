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
 * Task model bean class.
 * implement {@link java.io.Serializable}
 */
@XmlRootElement
@Entity
@Table(name = "Tasks", schema = "todo_db")
public class Task implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	private Long taskId;
	@Column(nullable = false)
	private String userId;
	private String description;
	private Integer pendingIntentId;

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
			Integer pendingIntentId) {
		super();
		this.taskId = taskId;
		this.userId = userId;
		this.description = description;
		this.pendingIntentId = pendingIntentId;
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

	public Integer getPendingIntentId() {
		return pendingIntentId;
	}

	public void setPendingIntentId(Integer pendingIntentId) {
		this.pendingIntentId = pendingIntentId;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Task [taskId=" + taskId + ", userId=" + userId
				+ ", description=" + description + ", pendingIntentId="
				+ pendingIntentId + "]";
	}

	public JSONObject toJSON() {
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("taskId", taskId);
			jsonObject.put("userId", userId);
			jsonObject.put("description", description);
			jsonObject.put("pendingIntentId", pendingIntentId);
			// jsonObject.put("user", user.toJSON().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jsonObject;
	}

	public static void main(String[] args) {

	}
}
