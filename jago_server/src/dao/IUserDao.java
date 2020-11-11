package dao;
 
import java.util.List;
 
import entity.User;
 
public interface IUserDao {
	/**
	 * 修改
	 * @param user
	 * @return
	 */
 
	boolean update(User user);
	
	/**
	 * 通过username查找 一条记录
	 * @param username
	 * @return
	 */
	User QueryByUserName(String username);
	
	/**
	 * 查找全部记录
	 * @return
	 */
	List<User> queryAll();
	
	/**
	 * 根据账户名、密码验证用户
	 */
	User queryUser(String username,String password);
}