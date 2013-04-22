package shenkar.ac.il.servletContainer;

/**
 * @author Zohar Nyego
 */
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.Collection;

import javax.jws.WebService;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.HibernateException;
import org.hibernate.exception.ConstraintViolationException;
import com.mysql.jdbc.CommunicationsException;
import shenkar.ac.il.dao.DAO;
import shenkar.ac.il.dao.IUserTodoList;
import shenkar.ac.il.model.Task;
import shenkar.ac.il.model.User;

/**
 * 
 * ToDo Tasks Web Service implement
 * {@link shenkar.ac.il.servletContainer.IToDoListWebService}
 * 
 */
@WebService
@Path("/tasks")
public class ToDoListWebService implements IToDoListWebService {

	private IUserTodoList dao = null;
	private static final Logger logger = Logger
			.getLogger(ToDoListWebService.class);;

	/**
	 * Default constructor.
	 * 
	 */
	public ToDoListWebService() {
		super();
		try {
			dao = DAO.getInstance();
			logger.trace("[ToDoListWebService]: created successfully!.");
		} catch (HibernateException e) {
			logger.fatal(
					"[ToDoListWebService]: HibernateException trown while trying get DAO instance!.",
					e);
			System.exit(1);
		} catch (CommunicationsException e) {
			logger.fatal(
					"[ToDoListWebService]: CommunicationsException trown while trying get DAO instance!.",
					e);
			System.exit(1);
		} catch (SQLException e) {
			logger.fatal(
					"[ToDoListWebService]: SQLException trown while trying get DAO instance!.",
					e);
			System.exit(1);
		} catch (RuntimeException e) {
			logger.fatal(
					"[ToDoListWebService]: RuntimeException trown while trying get DAO instance!.",
					e);
			System.exit(1);
		} catch (Exception e) {
			logger.fatal(
					"[ToDoListWebService]: fatal Exception trown while trying get DAO instance!.",
					e);
			System.exit(1);
		}

	}

	/**
	 * This method retrieve to client the user task list represented as json
	 * string array.
	 * 
	 * @param userInfo
	 *            - user details represented as Json element .
	 */
	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public Response getUserTasks(JAXBElement<User> userInfo) {

		try {

			String userId = userInfo.getValue().getUserId();
			String userpass = userInfo.getValue().getUserPass();
			long userAgent = userInfo.getValue().getUserAgent();
			logger.info("[getUserTasks]: requerst arrived!,\n request body is -\n"
					+ userInfo.getValue().toJSON().toString());

			User user = dao.getUser(userId, userpass, false);

			JSONArray jsonTasks = new JSONArray();

			if (user != null) {
				if (user.getUserAgent() != userAgent) {

					jsonTasks = new JSONArray();
					Collection<Task> userTasks = user.getUserTasks();

					for (Task task : userTasks) {
						jsonTasks.put(task.toJSON());
					}
					logger.info("[getUserTasks]: response ,JsonArray of tasks!.");
					return Response.ok(jsonTasks.toString()).build();
				} else {
					logger.warn("[getUserTasks]: response ,'User not found!'.");
					return Response.ok("User not found!.").build();
				}
			} else {
				logger.info("[getUserTasks]: response ,'no update necessary!'.");
				return Response.ok("UpdateNotNecessary").build();
			}

		} catch (CommunicationsException e) {

			logger.error("[getUserTasks]: response ,'CommunicationsException!'.");
			return Response.ok("CommunicationsException").build();

		} catch (SQLException e) {

			logger.error("[getUserTasks]: response ,'SQLException!'.");
			return Response.ok("SQLException").build();

		} catch (GeneralSecurityException e) {

			logger.error("[getUserTasks]: response ,'GeneralSecurityException!'.");
			return Response.ok("GeneralSecurityException").build();
		}

	}

	/**
	 * This method retrieve to client the user details as represented json
	 * string object.
	 * 
	 * @param userInfo
	 *            - user details represented as Json element .
	 */
	@Override
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	@Path("/users/get_user")
	public Response getUserInfo(JAXBElement<User> userInfo) {

		try {
			String userId = userInfo.getValue().getUserId();
			String userpass = userInfo.getValue().getUserPass();
			logger.info("[getUserInfo]: requerst arrived!,\n request body is -\n"
					+ userInfo.getValue().toJSON().toString());
			User user = dao.getUser(userId, userpass, true);
			if (user == null) {
				logger.warn("[getUserInfo]: response ,'User not found!'.");
				return Response.ok("User not found!.").build();
			}
			String resposeMessage = user.toJSON().toString();

			logger.info("[getUserInfo]: response, json user record.");
			return Response.ok(resposeMessage).build();

		} catch (CommunicationsException e) {

			logger.error("[getUserInfo]: response ,'CommunicationsException!'.");
			return Response.ok("CommunicationsException").build();

		} catch (SQLException e) {

			logger.error("[getUserInfo]: response ,'SQLException!'.");
			return Response.ok("SQLException").build();

		} catch (GeneralSecurityException e) {

			logger.error("[getUserInfo]: response ,'GeneralSecurityException!'.");
			return Response.ok("GeneralSecurityException").build();
		}

	}

	/**
	 * This method receives new user from client represented as json String
	 * object convert the json to {@link shenkar.ac.il.model.User} valid bean
	 * and passes the the bean object to DAO for insert to DataBase. .
	 * 
	 * @param user
	 *            - new user represented as json object.
	 */
	@Override
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/users/put_user/user")
	public Response putUser(JAXBElement<User> user) {

		try {
			User newUser = user.getValue();
			logger.info("[putUser]: requerst arrived!,\n request body is -\n "
					+ newUser.toJSON().toString());
			newUser.setUserAgent(0L);
			String pass = dao.addUser(newUser);
			logger.info("[putUser]: response, encrypted password text\\plain.");
			return Response.ok("CREATED:" + pass).build();
		} catch (ConstraintViolationException e) {
			logger.warn("[putUser]: 'ConstraintViolationException' trown while trying to insert new user record!.");
			return Response.ok(e.getMessage()).build();

		} catch (CommunicationsException e) {
			logger.error("[putUser]: response ,'CommunicationsException!'.");
			return Response.ok("CommunicationsException").build();

		} catch (SQLException e) {
			logger.error("[putUser]: response ,'SQLException!'.");
			return Response.ok("SQLException").build();

		} catch (GeneralSecurityException e) {
			logger.error("[putUser]: response ,'GeneralSecurityException!'.");
			return Response.ok("GeneralSecurityException").build();
		}

	}

	/**
	 * This method receives new task from client represented as json String
	 * object convert the json to {@link shenkar.ac.il.model.Task} valid bean
	 * and passes the the bean object to DAO for insert to DataBase. .
	 * 
	 * @param task
	 *            - new user task represented as json object.
	 */
	@Override
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/put_task")
	public Response putTask(JAXBElement<Task> task) {

		try {
			logger.info("[putTask]: requerst arrived!,\n request body is -\n "
					+ task.getValue().toJSON().toString());
			Task newTask = task.getValue();
			User userInfo = newTask.getUser();
			Long userAgent = dao.addUserTask(userInfo.getUserId(),
					userInfo.getUserPass(), newTask);
			if (userAgent == -1) {
				logger.warn("[putTask]: response ,'User not found!'.");
				return Response.ok("User not found!.").build();

			}
			String resposeMessage = userAgent.toString();
			logger.info("[putTask]: response, user agent text\\plain.");
			return Response.ok(resposeMessage).build();

		} catch (CommunicationsException e) {

			logger.error("[putTask]: response ,'CommunicationsException!'.");
			return Response.ok("CommunicationsException").build();

		} catch (SQLException e) {

			logger.error("[putTask]: response ,'SQLException!'.");
			return Response.ok("SQLException").build();
		}
	}

	/**
	 * This method receives tasks id's from client represented as json String
	 * array and extract the id's to array of long. passes long array to DAO for
	 * deletion of tasks from database.
	 * 
	 * @param deletionTasks
	 *            - tasks id's represented as json array object.
	 */
	@Override
	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/delete_tasks/tasks_id_arr")
	public Response deleteTasks(JSONObject deletionTasks) {

		try {
			logger.info("[deleteTasks]: requerst arrived!,\n request body is -\n "
					+ deletionTasks.toString());
			JSONObject userInf = (JSONObject) deletionTasks.get("userInfo");
			JSONArray taskIds = (JSONArray) deletionTasks.get("taskIdArr");
			long ids[] = new long[taskIds.length()];

			for (int index = 0; index < taskIds.length(); index++) {
				ids[index] = taskIds.getLong(index);
			}

			String userId = (String) userInf.get("userId");
			String userPass = (String) userInf.get("userPass");
			Long userAgent = dao.removeUserTask(userId, userPass, ids);
			if (userAgent == -1) {
				logger.warn("[deleteTasks]: response ,'User not found!'.");
				return Response.ok("User not found!.").build();
			}
			String responseString = userAgent.toString();
			logger.info("[deleteTasks]: response, user agent text\\plain.");
			return Response.ok(responseString).build();
		} catch (CommunicationsException e) {

			logger.error("[deleteTasks]: response ,'CommunicationsException!'.");
			return Response.ok("CommunicationsException").build();

		} catch (SQLException e) {

			logger.error("[deleteTasks]: response ,'SQLException!'.");
			return Response.ok("SQLException").build();

		} catch (JSONException e) {

			logger.error("[deleteTasks]: response ,'JSONException!'.");
			return Response.ok("JSONException").build();
		}

	}
}
