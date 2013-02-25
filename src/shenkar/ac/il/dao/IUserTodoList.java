package shenkar.ac.il.dao;
/**
 * @author Zohar Nyego
 */
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import com.mysql.jdbc.CommunicationsException;
import shenkar.ac.il.model.Task;
import shenkar.ac.il.model.User;

/**
 * Known Implement class: {@link shenkar.ac.il.dao.DAO}
 */
public interface IUserTodoList {
	/**
	 * Add new User record to the data base.
	 * 
	 * @param user
	 *            - instance of User.
	 * @return the string encrypted password for this user.
	 * @throws SQLException
	 * @throws GeneralSecurityException
	 */
	public String addUser(User user) throws SQLException,
			GeneralSecurityException;

	/**
	 * This function retrieve from data base the user with the associated id &
	 * password. In case of md5 flag is true the user pass will be encrypted
	 * before trying to pull the user from data base.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @param md5
	 *            - this indicate if pass is encrypted or not.
	 * @return The user requested.
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 * @throws GeneralSecurityException
	 * @throws IndexOutOfBoundsException
	 *             if the user not found!.
	 */
	public User getUser(String userId, String userpass, boolean md5)
			throws SQLException, GeneralSecurityException;

	/**
	 * Remove the specific user with the associated id & password.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @return true if the user removed data base ,else false.
	 * @throws SQLException
	 */
	public Boolean removeUser(String userId, String userPass)
			throws SQLException;

	/**
	 * This method insert new task to the user with the associated id & password
	 * . Retrieve the userAgent for the client to know who was the last to
	 * perform an action.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @param task
	 *            - user new task.
	 * @return the userAgent.
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 * 
	 * @throws NullPointerException
	 *             if the user not found!.
	 */
	public long addUserTask(String userId, String userPass, Task task)
			throws SQLException;

	/**
	 * This method delete exists tasks belong to the user with the associated id
	 * & password . Retrieve the userAgent for the client to know who was the
	 * last to perform an action.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @param taskIds
	 *            - array of tasks id's .
	 * @return the userAgent.
	 * @throws SQLException
	 */
	public long removeUserTask(String userId, String userPass, long taskIds[])
			throws SQLException;

	/**
	 * This method update exists tasks belong to the user with the associated id
	 * & password . Retrieve the userAgent for the client to know who was the
	 * last to perform an action.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @param task
	 *            - the updated user task.
	 * @return the userAgent.
	 * @throws SQLException
	 */
	public Boolean updateUserTask(String userId, String userPass, Task task)
			throws SQLException;

}
