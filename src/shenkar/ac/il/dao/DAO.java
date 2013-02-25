package shenkar.ac.il.dao;

/**
 * @author Zohar Nyego
 */
import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.util.Collection;
import org.apache.log4j.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistryBuilder;

import com.mysql.jdbc.CommunicationsException;

import shenkar.ac.il.model.Task;
import shenkar.ac.il.model.User;

/**
 * Data Accesses Object (DAO) Implements the interface IUserTodoList
 * 
 * @see {@link shenkar.ac.il.dao.IUserTodoList}
 */
public class DAO implements IUserTodoList {

	private static DAO dao = null;
	private final SessionFactory sessionFactory;
	private static final Logger logger = Logger.getLogger(DAO.class);

	/**
	 * Default constructor. 
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 */
	private DAO() throws HibernateException, CommunicationsException,
			SQLException {
		super();
		Configuration configuration = new Configuration();
		configuration.configure("hibernate.cfg.xml");
		ServiceRegistryBuilder serviceRegistryBuilder = new ServiceRegistryBuilder()
				.applySettings(configuration.getProperties());
		configuration.addAnnotatedClass(Task.class);
		configuration.addAnnotatedClass(User.class);
		sessionFactory = configuration
				.buildSessionFactory(serviceRegistryBuilder
						.buildServiceRegistry());

		logger.trace("[DAO]: DAO created successfully!,\n tododb update the schema.");
	}

	/**
	 * Return DAO singleton instance.
	 * 
	 * @return singleton (DAO) Data Accesses Object.
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 */
	public static DAO getInstance() throws HibernateException,
			CommunicationsException, SQLException {
		if (dao == null) {
			dao = new DAO();
		}
		logger.trace("[getInstance]: return instance!.");
		return dao;
	}

	/**
	 * Add new User record to the data base.
	 * 
	 * @param user
	 *            - instance of User.
	 * @return the string encrypted password for this user.
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 * @throws GeneralSecurityException
	 */
	@Override
	public String addUser(User user) throws HibernateException,
			CommunicationsException, SQLException, GeneralSecurityException {
		Session session = sessionFactory.openSession();
		logger.trace("[addUser]: session opened!.");
		Transaction transaction = session.beginTransaction();
		String pass = null;
		try {
			pass = md5(user.getUserPass());
			user.setUserPass(pass);
			session.save(user);
			session.flush();
			transaction.commit();
			logger.trace("[addUser]: new user inserted successfully to users table.");
		} catch (HibernateException e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			logger.error(
					"[addUser]: 'HibernateException' has been thrown while inserting new user!.",
					e);

			throw e;
		} catch (GeneralSecurityException e) {
			logger.fatal(
					"[addUser]: 'GeneralSecurityException' has been thrown while inserting new user!.",
					e);
			throw e;
		} finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[addUser]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[addUser]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;
				}
			}
		}
		return pass;
	}

	/**
	 * Remove the specific user with the associated id & password.
	 * 
	 * @param userId
	 *            - the user id.
	 * @param userPass
	 *            - the user password.
	 * @return true if the user removed data base ,else false.
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 */
	@Override
	public Boolean removeUser(String userId, String userPass)
			throws HibernateException, CommunicationsException, SQLException {
		Session session = sessionFactory.openSession();
		logger.trace("[removeUser]: session opened!.");
		Transaction transaction = session.beginTransaction();

		try {
			User user = null;
			user = (User) session.load(User.class, userId);
			if (user != null) {
				if (user.getUserPass().equals(userPass)) {
					session.delete(user);
					session.flush();
					transaction.commit();
					logger.trace("[removeUser]: user deleted successfully from users table.");
					return true;
				} else {
					return false;
				}
			} else {
				return false;
			}

		} catch (HibernateException e) {

			if (transaction.isActive()) {
				transaction.rollback();
			}
			logger.fatal(
					"[removeUser]: 'HibernateException' has been thrown while to delete exist user!.",
					e);
			throw e;
		} finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[removeUser]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[removeUser]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;
				}

			}
		}

	}

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
	@Override
	public User getUser(String userId, String userPass, boolean md5)
			throws HibernateException, CommunicationsException, SQLException,
			IndexOutOfBoundsException, GeneralSecurityException {
		Session session = sessionFactory.openSession();
		logger.trace("[getUser]: session opened!.");
		User user = null;
		try {
			if (md5) {
				userPass = md5(userPass);
			}
			String hql = "FROM User WHERE userId=:user_id AND userPass=:user_pass";
			Query query = session.createQuery(hql);
			query.setParameter("user_id", userId);
			query.setParameter("user_pass", userPass);
			user = (User) query.list().get(0);
			logger.trace("[getUser]: user found in table users!.");
		} catch (HibernateException e) {
			logger.fatal(
					"[getUser]: 'HibernateException' has been thrown while trying to get user!.",
					e);
			throw e;
		} catch (IndexOutOfBoundsException e) {
			logger.warn(
					"[getUser]: 'IndexOutOfBoundsException' has been thrown (user not found in table users !).",
					e);
			throw e;
		} catch (GeneralSecurityException e) {
			logger.fatal(
					"[getUser]: 'GeneralSecurityException' has been thrown while trying to get userInfo !.",
					e);
			throw e;
		}

		finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[getUser]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[getUser]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;
				}
			}
		}
		return user;
	}

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
	 * @throws NullPointerException
	 *             if the user not found!.
	 */
	@Override
	public long addUserTask(String userId, String userPass, Task task)
			throws HibernateException, CommunicationsException, SQLException,
			NullPointerException {
		Session session = sessionFactory.openSession();
		logger.trace("[addUserTask]: session opened!.");
		Transaction transaction = session.beginTransaction();
		User user = null;
		Collection<Task> tasks = null;
		long userAgent = 0L;
		try {
			user = (User) session.get(User.class, userId);
			if (user.getUserPass().equals(userPass)) {
				tasks = user.getUserTasks();
				task.setUserId(userId);
				tasks.add(task);
				user.setUserTasks(tasks);
				userAgent = System.currentTimeMillis();
				user.setUserAgent(userAgent);
				session.update(user);
				session.flush();
				transaction.commit();
				logger.trace("[addUserTask]: new user task added to tasks table successfully!.");
			}
		} catch (HibernateException e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			logger.fatal(
					"[addUserTask]: 'HibernateException' has been thrown while trying add new user task!.",
					e);
			throw e;
		} catch (NullPointerException e) {
			logger.warn(
					"[addUserTask]: 'NullPointerException' has been thrown (user not found in table users !).",
					e);
			throw e;
		}

		finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[addUserTask]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[addUserTask]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;
				}
			}
		}
		return userAgent;
	}

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
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 * @throws NullPointerException
	 *             if the user not found!.
	 */
	@Override
	public long removeUserTask(String userId, String userPass, long taskIds[])
			throws HibernateException, CommunicationsException, SQLException,
			NullPointerException {
		Session session = sessionFactory.openSession();
		logger.trace("[removeUserTask]: session opened!.");
		Transaction transaction = session.beginTransaction();
		User user = null;
		long userAgent = 0L;
		try {
			user = (User) session.get(User.class, userId);
			if (user.getUserPass().equals(userPass)) {
				for (long id : taskIds) {
					String hql = "DELETE FROM Task"
							+ " WHERE taskId = :task_id";
					Query query = session.createQuery(hql);
					query.setParameter("task_id", id);
					query.executeUpdate();
				}
				userAgent = System.currentTimeMillis();
				user.setUserAgent(System.currentTimeMillis());
				session.update(user);
				session.flush();
				transaction.commit();
				logger.trace("[removeUserTask]: user tasks removed from tasks table successfully!.");
			}
		} catch (HibernateException e) {
			if (transaction.isActive()) {
				transaction.rollback();
			}
			logger.fatal("[removeUserTask]: 'HibernateException' has been thrown while trying to remove user tasks!.");
			throw e;
		} catch (NullPointerException e) {
			logger.warn(
					"[removeUserTask]: 'NullPointerException' has been thrown (user not found in table users !).",
					e);
			throw e;
		} finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[removeUserTask]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[removeUserTask]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;
				}
			}
		}
		return userAgent;
	}

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
	 * @throws HibernateException
	 * @throws CommunicationsException
	 * @throws SQLException
	 * @throws NullPointerException
	 *             if the user not found!.
	 */
	@Override
	public Boolean updateUserTask(String userId, String userPass, Task task)
			throws HibernateException, CommunicationsException, SQLException,
			NullPointerException {

		Session session = sessionFactory.openSession();
		logger.trace("[updateUserTask]: session opened!.");
		Transaction transaction = session.beginTransaction();
		try {
			User user = (User) session.get(User.class, userId);
			if (user.getUserPass().equals(userPass)) {

				task.setUserId(userId);
				session.update("Task", task);
				session.flush();
				transaction.commit();
				logger.trace("[updateUserTask]: user task updated successfully! .");
				return true;
			} else {
				return false;
			}
		} catch (HibernateException e) {
			transaction.rollback();
			if (transaction.isActive()) {
				transaction.rollback();
			}
			logger.fatal("[removeUserTask]: 'HibernateException' has been thrown while trying to update user task!.");
			throw e;
		} catch (NullPointerException e) {
			logger.warn(
					"[updateUserTask]: 'NullPointerException' has been thrown (user not found in table users !).",
					e);
			throw e;
		} finally {
			if (session != null) {
				try {
					session.close();
					logger.trace("[updateUserTask]: session closed!.");
				} catch (HibernateException e) {
					logger.fatal(
							"[updateUserTask]: 'HibernateException' has been thrown while trying to close session!.",
							e);
					throw e;

				}
			}
		}

	}

	/**
	 * Return encrypted MD5 String.
	 * 
	 * @param pass
	 *            - string to be encrypted.
	 * @return MD5 encrypted password.
	 * @throws GeneralSecurityException
	 */
	private String md5(String pass) throws GeneralSecurityException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(pass.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		logger.trace("[md5]:User password encrypted!.");
		return sb.toString();
	}

}
