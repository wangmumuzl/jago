package dao;
 
import java.util.List;
 
import entity.User;
 
public interface IUserDao {
	/**
	 * �޸�
	 * @param user
	 * @return
	 */
 
	boolean update(User user);
	
	/**
	 * ͨ��username���� һ����¼
	 * @param username
	 * @return
	 */
	User QueryByUserName(String username);
	
	/**
	 * ����ȫ����¼
	 * @return
	 */
	List<User> queryAll();
	
	/**
	 * �����˻�����������֤�û�
	 */
	User queryUser(String username,String password);
}