/**
 * 
 */
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Observable;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import core.game.Game;
import core.player.Player;
import core.player.SocketDelegate;
import core.player.SocketSender;
import dao.IPlayerDao;
import dao.IUserDao;
import dao.impl.PlayerDaoImpl;
import dao.impl.UserDaoImpl;
import entity.PlayerInfo;
import entity.User;

/**
 * @author 朱琳
 *
 */
public class Server {
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private ArrayList<ClientThread> clients;
	private int max=100;//设置最大登录人数
	private int port=6666;//设置端口
	private IUserDao userDao;
	private IPlayerDao playerDao;
	private ConcurrentHashMap<String,PlayerInfo> playerList;
	private String allPlayer="";
	
	// 启动服务器
	public void serverStart() throws java.net.BindException {
		try {
			userDao = new UserDaoImpl();
			playerDao = new PlayerDaoImpl();
			clients = new ArrayList<ClientThread>();
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket, max);
			serverThread.start();
			System.out.println("服务器已开启");
			
			queryInfo();
		} catch (BindException e) {
			throw new BindException("端口号已被占用，请换一个！");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new BindException("启动服务器异常！");
		}
	}
	
	//服务器开启查询出数据库中的所有棋手及所有的对战结果
	public void queryInfo(){
		//查询服务器端所有棋手
		playerList=playerDao.queryAllPlayer();
		if(playerList!=null){
			for (PlayerInfo palyer : playerList.values()) {
				allPlayer += palyer.getPlayerName()+ "@";
			}
		}
		//查询与服务器端对战的所有game
		//gameId@blackName@whiteName@winerName@step@date@resultPath
		
	}
	
	// 服务器线程
	class ServerThread extends Thread {
		private ServerSocket serverSocket;
		private int max;// 人数上限

		// 服务器线程的构造方法
		public ServerThread(ServerSocket serverSocket, int max) {
			this.serverSocket = serverSocket;
			this.max = max;
		}

		public void run() {
			Socket socket=null;
			BufferedReader r=null;
			PrintWriter w=null;
			while (true) {// 不停的等待客户端的链接
				try {
					socket = serverSocket.accept();
				    r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					w = new PrintWriter(socket.getOutputStream());
					//接收客户端的基本用户信息
					String inf = r.readLine();
					StringTokenizer st = new StringTokenizer(inf, "@");
					String username=st.nextToken();//用户名
					String password=st.nextToken();
					String ip=st.nextToken();//客户端ip
					if (clients.size() == max) {// 如果已达人数上限
						// 反馈连接成功信息
						w.println("MAX@服务器：对不起，" + username + ip + "，服务器在线人数已达上限，请稍后尝试连接！");
						w.flush();
						socket.close();
						r.close();
						w.close();
						// 释放资源
						continue;
					}
					//向客户端发送成功的消息
					User user = userDao.queryUser(username, password);
					if(user!=null){
						user.setIp(ip);
						//向客户端传递所有game信息
						//GameInfo@gameId@blackName@whiteName@winerName@step@date
						w.println("success@"+user.getName());
						w.println(allPlayer);
						w.flush();
						ClientThread client = new ClientThread(socket,user);
						new Thread(client).start();
						clients.add(client);
					}else{
						w.println("fail");
						socket.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// 为一个客户端服务的线程
	class ClientThread extends Observable implements Runnable {
		private Socket socket;
		private BufferedReader reader;
		private PrintWriter writer;
		private User user;
		
		public BufferedReader getReader() {
			return reader;
		}

		public PrintWriter getWriter() {
			return writer;
		}

		public User getUser() {
			return user;
		}

		// 客户端线程的构造方法
		public ClientThread(Socket socket,User user) {
			try {
				this.user = user;
				this.socket = socket;
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				// 反馈连接成功信息
//				writer.println(user.getName() + user.getIp() + "与服务器连接成功!");
//				writer.flush();
				System.out.println(user.getName() + user.getIp() + "与服务器连接成功!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("deprecation")
		public void run() {// 不断接收客户端的消息，进行处理
			String message = null;
			while (true) {
				try {
					message = reader.readLine();// 接收客户端消息
					System.out.println("sever接收客户端的消息===================");
					System.out.println(message);
					if (message.equals("CLOSE"))// 下线命令
					{
						// 断开连接释放资源
						reader.close();
						writer.close();
						socket.close();

						System.out.println(user.getName()+"下线了！");
						// 删除此条客户端服务线程
						for (int i = clients.size() - 1; i >= 0; i--) {
							if (clients.get(i).getUser() == user) {
								ClientThread temp = clients.get(i);
								System.out.println();
								clients.remove(i);// 删除此用户的服务线程
//								temp.stopFlag = true;// 停止这条服务线程
								return;
							}
						}
					} else{
						//下棋
						StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
						String flag = stringTokenizer.nextToken(); // 标识符
						
						if(flag.equals("PLAYCHESS")){
							String clientPlayerName = stringTokenizer.nextToken(); // 客户端棋手
							String serverPlayerName = stringTokenizer.nextToken(); // 服务端棋手
							String order = stringTokenizer.nextToken();//先手后手
							System.out.println("order"+order);
							System.out.println("serverPlayerName:"+serverPlayerName);
							String serverClassName = playerList.get(serverPlayerName).getClassName();
							System.out.println(serverClassName);
							//若找不到对应的类怎么办？
							Class clazz = Class.forName(serverClassName);
							Player serverPlayer = (Player) clazz.newInstance();
							SocketDelegate clientPlayer = new SocketDelegate(clientPlayerName,socket);
							this.addObserver(clientPlayer);
							Game game =null;
							int timeLimit = 30000;
							
							//判断先手后手
							if(order.equals("true")){
								game = new Game(clientPlayer, serverPlayer, timeLimit);
							}else{
								game = new Game(serverPlayer, clientPlayer, timeLimit);
							}
							game.start();
						}else if(flag.equals("SERVERGAME")){
							//服务器间棋手对战
							String blackPlayerName = stringTokenizer.nextToken(); // 客户端棋手
							String whitePlayerName = stringTokenizer.nextToken(); // 服务端棋手
							String blackClassName = playerList.get(blackPlayerName).getClassName();
						    String whiteClassName = playerList.get(whitePlayerName).getClassName();
						    Class blackClazz = Class.forName(blackClassName);
						    Class whiteClazz = Class.forName(whiteClassName);
//							SocketPlayer black = new SocketPlayer(blackPlayerName, socket, (Player)blackClazz.newInstance(),"black");
//							SocketPlayer white = new SocketPlayer(whitePlayerName, socket, (Player)whiteClazz.newInstance(),"white");
//							this.addObserver(black);
//							this.addObserver(white);
							
							int timeLimit = 30;
							
							Game game = new Game((Player)blackClazz.newInstance(), (Player)whiteClazz.newInstance(),timeLimit);
							game.addObserver(new SocketSender(socket));
							//判断先手后手
							game.start();
						}
						else{//聊天和下棋
							System.out.println("客户端选择下棋或者聊天");
							setChanged();
							notifyObservers(message);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	public static void main(String[] args) {
		try {
			new Server().serverStart();
		} catch (BindException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
	}
}
