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
 * @author ����
 *
 */
public class Server {
	private ServerSocket serverSocket;
	private ServerThread serverThread;
	private ArrayList<ClientThread> clients;
	private int max=100;//��������¼����
	private int port=6666;//���ö˿�
	private IUserDao userDao;
	private IPlayerDao playerDao;
	private ConcurrentHashMap<String,PlayerInfo> playerList;
	private String allPlayer="";
	
	// ����������
	public void serverStart() throws java.net.BindException {
		try {
			userDao = new UserDaoImpl();
			playerDao = new PlayerDaoImpl();
			clients = new ArrayList<ClientThread>();
			serverSocket = new ServerSocket(port);
			serverThread = new ServerThread(serverSocket, max);
			serverThread.start();
			System.out.println("�������ѿ���");
			
			queryInfo();
		} catch (BindException e) {
			throw new BindException("�˿ں��ѱ�ռ�ã��뻻һ����");
		} catch (Exception e1) {
			e1.printStackTrace();
			throw new BindException("�����������쳣��");
		}
	}
	
	//������������ѯ�����ݿ��е��������ּ����еĶ�ս���
	public void queryInfo(){
		//��ѯ����������������
		playerList=playerDao.queryAllPlayer();
		if(playerList!=null){
			for (PlayerInfo palyer : playerList.values()) {
				allPlayer += palyer.getPlayerName()+ "@";
			}
		}
		//��ѯ��������˶�ս������game
		//gameId@blackName@whiteName@winerName@step@date@resultPath
		
	}
	
	// �������߳�
	class ServerThread extends Thread {
		private ServerSocket serverSocket;
		private int max;// ��������

		// �������̵߳Ĺ��췽��
		public ServerThread(ServerSocket serverSocket, int max) {
			this.serverSocket = serverSocket;
			this.max = max;
		}

		public void run() {
			Socket socket=null;
			BufferedReader r=null;
			PrintWriter w=null;
			while (true) {// ��ͣ�ĵȴ��ͻ��˵�����
				try {
					socket = serverSocket.accept();
				    r = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					w = new PrintWriter(socket.getOutputStream());
					//���տͻ��˵Ļ����û���Ϣ
					String inf = r.readLine();
					StringTokenizer st = new StringTokenizer(inf, "@");
					String username=st.nextToken();//�û���
					String password=st.nextToken();
					String ip=st.nextToken();//�ͻ���ip
					if (clients.size() == max) {// ����Ѵ���������
						// �������ӳɹ���Ϣ
						w.println("MAX@���������Բ���" + username + ip + "�����������������Ѵ����ޣ����Ժ������ӣ�");
						w.flush();
						socket.close();
						r.close();
						w.close();
						// �ͷ���Դ
						continue;
					}
					//��ͻ��˷��ͳɹ�����Ϣ
					User user = userDao.queryUser(username, password);
					if(user!=null){
						user.setIp(ip);
						//��ͻ��˴�������game��Ϣ
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

	// Ϊһ���ͻ��˷�����߳�
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

		// �ͻ����̵߳Ĺ��췽��
		public ClientThread(Socket socket,User user) {
			try {
				this.user = user;
				this.socket = socket;
				reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				writer = new PrintWriter(socket.getOutputStream());
				// �������ӳɹ���Ϣ
//				writer.println(user.getName() + user.getIp() + "����������ӳɹ�!");
//				writer.flush();
				System.out.println(user.getName() + user.getIp() + "����������ӳɹ�!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		@SuppressWarnings("deprecation")
		public void run() {// ���Ͻ��տͻ��˵���Ϣ�����д���
			String message = null;
			while (true) {
				try {
					message = reader.readLine();// ���տͻ�����Ϣ
					System.out.println("sever���տͻ��˵���Ϣ===================");
					System.out.println(message);
					if (message.equals("CLOSE"))// ��������
					{
						// �Ͽ������ͷ���Դ
						reader.close();
						writer.close();
						socket.close();

						System.out.println(user.getName()+"�����ˣ�");
						// ɾ�������ͻ��˷����߳�
						for (int i = clients.size() - 1; i >= 0; i--) {
							if (clients.get(i).getUser() == user) {
								ClientThread temp = clients.get(i);
								System.out.println();
								clients.remove(i);// ɾ�����û��ķ����߳�
//								temp.stopFlag = true;// ֹͣ���������߳�
								return;
							}
						}
					} else{
						//����
						StringTokenizer stringTokenizer = new StringTokenizer(message, "@");
						String flag = stringTokenizer.nextToken(); // ��ʶ��
						
						if(flag.equals("PLAYCHESS")){
							String clientPlayerName = stringTokenizer.nextToken(); // �ͻ�������
							String serverPlayerName = stringTokenizer.nextToken(); // ���������
							String order = stringTokenizer.nextToken();//���ֺ���
							System.out.println("order"+order);
							System.out.println("serverPlayerName:"+serverPlayerName);
							String serverClassName = playerList.get(serverPlayerName).getClassName();
							System.out.println(serverClassName);
							//���Ҳ�����Ӧ������ô�죿
							Class clazz = Class.forName(serverClassName);
							Player serverPlayer = (Player) clazz.newInstance();
							SocketDelegate clientPlayer = new SocketDelegate(clientPlayerName,socket);
							this.addObserver(clientPlayer);
							Game game =null;
							int timeLimit = 30000;
							
							//�ж����ֺ���
							if(order.equals("true")){
								game = new Game(clientPlayer, serverPlayer, timeLimit);
							}else{
								game = new Game(serverPlayer, clientPlayer, timeLimit);
							}
							game.start();
						}else if(flag.equals("SERVERGAME")){
							//�����������ֶ�ս
							String blackPlayerName = stringTokenizer.nextToken(); // �ͻ�������
							String whitePlayerName = stringTokenizer.nextToken(); // ���������
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
							//�ж����ֺ���
							game.start();
						}
						else{//���������
							System.out.println("�ͻ���ѡ�������������");
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
