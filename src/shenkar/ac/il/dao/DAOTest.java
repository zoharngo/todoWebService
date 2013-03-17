/**
 * 
 */
package shenkar.ac.il.dao;

import static org.junit.Assert.*;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.sql.SQLException;
import org.hibernate.HibernateException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import shenkar.ac.il.model.Task;
import shenkar.ac.il.model.User;
import com.mysql.jdbc.CommunicationsException;

/**
 * @author Zohar
 * 
 */
public class DAOTest {

	/**
	 * Test method for {@link shenkar.ac.il.dao.DAO#getInstance()}.
	 * 
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 */

	@Test
	public void testGetInstance() throws HibernateException,
			CommunicationsException, SQLException, RuntimeException, Exception {
		assertSame(DAO.getInstance(), DAO.getInstance());
	}

	/**
	 * Test method for
	 * {@link shenkar.ac.il.dao.DAO#addUser(shenkar.ac.il.model.User)}.
	 * 
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 * @throws GeneralSecurityException
	 */
	@Test
	public void testAddUser() throws HibernateException, RuntimeException,
			Exception, CommunicationsException, SQLException,
			GeneralSecurityException {
		User user = new User("test1", "test1", "test1", "12345", "test1@");

		DAO dao = DAO.getInstance();
		assertEquals("user test1 as been added!.", md5("12345"),
				dao.addUser(user));
	}

	/**
	 * Test method for
	 * {@link shenkar.ac.il.dao.DAO#removeUser(java.lang.String, java.lang.String)}
	 * .
	 * 
	 * @throws GeneralSecurityException
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 */
	@Test
	public void testRemoveUser() throws Exception {
		DAO dao = DAO.getInstance();
		Boolean result = dao.removeUser("test2", md5("123456"));
		assertTrue("User test2 as been removed!.", result == true);
	}

	/**
	 * Test method for
	 * {@link shenkar.ac.il.dao.DAO#getUser(java.lang.String, java.lang.String, boolean)}
	 * .
	 * 
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 * @throws GeneralSecurityException
	 * @throws IndexOutOfBoundsException
	 */
	@Test
	public void testGetUser() throws Exception {
		DAO dao = DAO.getInstance();
		assertEquals("User test3 found!", "test3",
				dao.getUser("test3", "1234567", true).getUserId());
	}

	/**
	 * Test method for
	 * {@link shenkar.ac.il.dao.DAO#addUserTask(java.lang.String, java.lang.String, shenkar.ac.il.model.Task)}
	 * .
	 * 
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 * @throws GeneralSecurityException
	 * @throws NullPointerException
	 */
	@Test
	public void testAddUserTask() throws Exception {
		Task task = new Task(11111L, "test3", "test2 message description",
				"location", -1L);
		DAO dao = DAO.getInstance();
		long userAgent = dao.addUserTask("test3", md5("1234567"), task);
		assertTrue("Task as been added!",
				userAgent < System.currentTimeMillis());
	}

	/**
	 * Test method for
	 * {@link shenkar.ac.il.dao.DAO#removeUserTask(java.lang.String, java.lang.String, long[])}
	 * 
	 * 
	 * @throws SQLException
	 * @throws CommunicationsException
	 * @throws HibernateException
	 * @throws GeneralSecurityException
	 * @throws NullPointerException
	 */
	@Test
	public void testRemoveUserTask() throws Exception {
		DAO dao = DAO.getInstance();
		long[] ids = new long[1];
		ids[0] = 11111L;
		long userAgent = dao.removeUserTask("test2", md5("123456"), ids);
		assertTrue("Task as been Removed!",
				userAgent < System.currentTimeMillis());
	}

	@BeforeClass
	public static void setUpBefureClass() throws Exception {
		DAO dao = DAO.getInstance();
		User user_2 = new User("test2", "test2", "test2", "123456", "test2@");
		User user_3 = new User("test3", "test3", "test3", "1234567", "test3@");
		Task task_user_3 = new Task(22222L, "test3", "remove task test.",
				"location", -1L);
		dao.addUser(user_2);
		dao.addUser(user_3);
		dao.addUserTask("test3", md5("1234567"), task_user_3);
	}

	@AfterClass
	public static void setUpAfterClass() throws Exception {
		DAO dao = DAO.getInstance();
		dao.removeUser("test1", md5("12345"));
		dao.removeUser("test3", md5("1234567"));
	}

	private static String md5(String pass) throws GeneralSecurityException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(pass.getBytes());

		byte byteData[] = md.digest();

		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}

}
