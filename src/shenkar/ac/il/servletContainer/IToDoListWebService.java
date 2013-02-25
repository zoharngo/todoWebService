package shenkar.ac.il.servletContainer;

/**
 * @author Zohar Nyego
 */
import javax.jws.WebService;
import javax.ws.rs.core.Response;
import javax.xml.bind.JAXBElement;
import org.codehaus.jettison.json.JSONObject;
import shenkar.ac.il.model.Task;
import shenkar.ac.il.model.User;

/**
 * Known Implement class:
 * {@link shenkar.ac.il.servletContainer.IToDoListWebService}
 */
@WebService
public interface IToDoListWebService {
	/**
	 * This method retrieve to client the user task list represented as JAXB
	 * array.
	 * 
	 * @param userInfo
	 *            - user details represented as JAXB element .
	 */
	public Response getUserTasks(JAXBElement<User> userInfo);

	/**
	 * This method retrieve to client the user details as represented JAXB
	 * object.
	 * 
	 * @param userInfo
	 *            - user details represented as JAXB element .
	 */
	public Response getUserInfo(JAXBElement<User> userInfo);

	/**
	 * This method receives new user from client represented as JAXB object
	 * convert the JAXB to {@link shenkar.ac.il.model.User} valid bean and
	 * passes the the bean object to DAO for insert to DataBase. .
	 * 
	 * @param user
	 *            - new user represented as JAXB element.
	 */
	public Response putUser(JAXBElement<User> user);

	/**
	 * This method receives new task from client represented as JAXB object
	 * convert the JAXB to {@link shenkar.ac.il.model.Task} valid bean and
	 * passes the the bean object to DAO for insert to DataBase. .
	 * 
	 * @param task
	 *            - new user task represented as JAXB element.
	 */
	public Response putTask(JAXBElement<Task> task);

	/**
	 * This method receives tasks id's from client represented as JAXB array and
	 * extract the id's to array of long. passes long array to DAO for deletion
	 * of tasks from database.
	 * 
	 * @param deletionTasks
	 *            - tasks id's represented as JAXB array element .
	 */
	public Response deleteTasks(JSONObject deletionTasks);
}
