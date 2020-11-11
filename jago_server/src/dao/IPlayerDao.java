/**
 * 
 */
package dao;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import entity.PlayerInfo;

/**
 * @author ����
 *
 */
public interface IPlayerDao {
	/**
	 * �޸�
	 * @param player
	 * @return
	 */
 
	boolean update(PlayerInfo player);
	
	/**
	 * ͨ��groupId���� һ����¼
	 * @param groupId
	 * @return
	 */
	PlayerInfo QueryById(int groupId,int gameId);
	
	/**
	 * ͨ��name���� һ����¼
	 * @return
	 */
	PlayerInfo QueryByName(String name);
	
	ConcurrentHashMap<String,PlayerInfo> queryAllPlayer();
}
