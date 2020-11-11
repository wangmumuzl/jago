package util;
 
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
 
 
public class DBCon {
	public static final String DRIVER="com.mysql.jdbc.Driver";
	public static final String URL="jdbc:mysql://localhost:3306/course_website";
	public static final String USER="root";
	public static final String PWD="";
	private Connection con;
	private PreparedStatement ps;
	private ResultSet rs;
	
	
	public DBCon(){
		try {
			//加载驱动程序
			Class.forName(DRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * @return返回数据库连接
	 */
	public Connection getCon(){
		try {
			con=DriverManager.getConnection(URL, USER, PWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}
	
	/**
	 * 关闭资源 
	 */
	public void closeAll(){
		if(rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if(ps!=null)
			try {
				ps.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		if(con!=null)
			try {
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	
	/**
	 * @param sql语句 
	 * @param pras参数列表 
	 * @return受影响的条数
	 */
	public int update(String sql,Object... pras){
		int resu=0;
		con=getCon();
		try {
			ps=con.prepareStatement(sql);
			if(pras!=null){
				for(int i=0;i<pras.length;i++){
					ps.setObject(i+1, pras[i]);
				}
			}
			resu=ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			closeAll();
		}
		return resu;
	}
	
	
	/**
	 * @param sql sql语句 
	 * @param pras
	 * @return 结果集
	 */
	public ResultSet query(String sql,Object... pras){
		con=getCon();
		try {
			ps=con.prepareStatement(sql);
			if(pras!=null){
				for(int i=0;i<pras.length;i++){
					ps.setObject(i+1, pras[i]);
				}
			}
			rs=ps.executeQuery();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
}