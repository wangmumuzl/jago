/**
 * 
 */
package dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import dao.IPlayerDao;
import entity.PlayerInfo;
import util.DBCon;

/**
 * @author ÖìÁÕ
 *
 */
public class PlayerDaoImpl implements IPlayerDao{
	DBCon util = new DBCon();
	
	/* (non-Javadoc)
	 * @see dao.IPlayerDao#update(core.player.Player)
	 */
	@Override
	public boolean update(PlayerInfo player) {
		return util.update(
				"update playersubmitinfo set submitDate=?,parentPath=?,name=?,comment=? and gameId=? where groupId=?",
				player.getSubmitDate(),player.getParentPath(),player.getPlayerName(),player.getComment()
				,player.getGameId(),player.getGroupId()) > 0;
	}

	/* (non-Javadoc)
	 * @see dao.IPlayerDao#QueryById(int)
	 */
	@Override
	public PlayerInfo QueryById(int groupId,int gameId) {
		// TODO Auto-generated method stub
		return _player(util.query("select * from playersubmitinfo where groupId=? and gameId=?", groupId,gameId));
	}
	
	private PlayerInfo _player(ResultSet rs){
		PlayerInfo player=null;
		try {
			if(rs.next()){
				player=new PlayerInfo();
				player.setComment(rs.getString("comment"));
				player.setGameId(rs.getInt("gameId"));
				player.setGroupId(rs.getInt("groupId"));
				player.setParentPath(rs.getString("parentPath"));
				player.setPlayerName(rs.getString("name"));
				player.setSubmitDate(rs.getString("submitDate"));
				player.setClassName(rs.getString("className"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			util.closeAll();
		}
		return player;
	}

	/* (non-Javadoc)
	 * @see dao.IPlayerDao#QueryByName(java.lang.String)
	 */
	@Override
	public PlayerInfo QueryByName(String name) {
		// TODO Auto-generated method stub
		return _player(util.query("select * from playersubmitinfo where name=?", name));
	}

	/* (non-Javadoc)
	 * @see dao.IPlayerDao#queryAllPlayer()
	 */
	@Override
	public ConcurrentHashMap<String,PlayerInfo> queryAllPlayer() {
		// TODO Auto-generated method stub
		ResultSet rs=util.query("select * from playersubmitinfo");
		PlayerInfo player=null;
		ConcurrentHashMap<String,PlayerInfo> playerList=new ConcurrentHashMap<String,PlayerInfo>();
		try {
			while(rs.next()){
				player=new PlayerInfo();
				player.setComment(rs.getString("comment"));
				player.setGameId(rs.getInt("gameId"));
				player.setGroupId(rs.getInt("groupId"));
				player.setParentPath(rs.getString("parentPath"));
				player.setPlayerName(rs.getString("name"));
				player.setSubmitDate(rs.getString("submitDate"));
				player.setClassName(rs.getString("className"));
				playerList.put(player.getPlayerName(),player);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return playerList;
	}
}
