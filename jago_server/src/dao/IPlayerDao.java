/**
 * 
 */
package dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.PlayerInfo;

/**
 * @author 朱琳
 *
 */
public interface IPlayerDao {
	/**
	 * 修改
	 * @param player
	 * @return
	 */
 
	boolean update(PlayerInfo player);
	
	/**
	 * 通过groupId查找 一条记录
	 * @param groupId
	 * @return
	 */
	PlayerInfo QueryById(int groupId,int gameId);
	
	/**
	 * 通过name查找 一条记录
	 * @return
	 */
	PlayerInfo QueryByName(String name);
	
	ConcurrentHashMap<String,PlayerInfo> queryAllPlayer();
}
