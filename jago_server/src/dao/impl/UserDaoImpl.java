package dao.impl;
 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
 
import dao.IUserDao;
import util.DBCon;
import entity.User;
 
public class UserDaoImpl implements IUserDao {
	DBCon util = new DBCon();
 
	@Override
	public boolean update(User user) {
		return util.update(
				"update user set username=?,password=?,groupId=? where studentId=?",
				user.getUsername(), user.getPassword(), user.getGroupId(),
				user.getUserid()) > 0;
	}
 
	@Override
	public User QueryByUserName(String username) {
		return _user(util.query("select * from user where username=?", username));
	}
 
	@Override
	public List<User> queryAll() {
		return  _list(util.query("select * from user"));
	}
	
	private User _user(ResultSet rs){
		User user=null;
		try {
			if(rs.next()){
				user=new User();
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				user.setGroupId(rs.getInt("groupId"));
				user.setName(rs.getString("name"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			util.closeAll();
		}
		return user;
	}
	
	private List<User> _list(ResultSet rs){
		List<User> _list=new ArrayList<User>();
		try {
			while(rs.next()){
				User user=new User();
				user.setPassword(rs.getString("password"));
				user.setUsername(rs.getString("username"));
				user.setUserid(rs.getInt("userid"));
				_list.add(user);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			util.closeAll();
		}
		return _list;
	}

	/* (non-Javadoc)
	 * @see dao.IUserDao#ensureUser(java.lang.String, java.lang.String)
	 */
	@Override
	public User queryUser(String username, String password) {
		return _user(util.query("select * from user where username=? and password=?", username,password));
	}
 
}