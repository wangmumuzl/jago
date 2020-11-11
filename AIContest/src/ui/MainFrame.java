/**
 * 
 */
package ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

import core.game.ui.BeautyGUI;
import core.game.Game;
import core.game.timer.GameTimer;
import core.player.Player;
import core.player.SocketDelegate;
import entity.User;
import jagoclient.Global;
import jagoclient.board.GoFrame;
import jagoclient.board.LocalGoFrame;

/**
 * @author 朱琳
 *
 */
public class MainFrame extends Observable{
	private JFrame frame;
	private JList playerList;
	private JMenuBar mb;
	private JMenu more, edit, help;
	private JMenuItem serverBattle, about;//定义菜单选项
	
	private JPanel northPanel;
	private JScrollPane rightScroll;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;
	private DefaultListModel listModel;
	private JLabel welcome;

	private boolean isConnected = false;
	private Thread messageThread;// 负责接收消息的线程
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private Map<String, User> onLineUsers = new HashMap<String, User>();// 所有在线用户
	private String username;//当前用户
	private SocketDelegate serverPlayer;
	private int gameId;//初始设置为六子棋  1
	private boolean isFirst;
	
	public MainFrame(User user, Socket socket ,String[] allPlayer,boolean isFirst) {
		this.username=user.getName();
		this.gameId=1;
		mb=new JMenuBar();
		more = new JMenu(" 更多对战方式");
		serverBattle = new JMenuItem(" 服务器间棋手对战");
		more.add(serverBattle);
		
		help = new JMenu("帮助");
		about = new JMenuItem("关于博弈平台");
		help.add(about);
		
		mb.add(more);
		mb.add(help);
		
		listModel = new DefaultListModel();
		playerList = new JList(listModel);
		
		welcome = new JLabel("   欢迎 " + this.username + "同学！");
		welcome.setPreferredSize(new Dimension(200, 40));
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 7));
		northPanel.add(welcome);
		// -----创建标题--------------
		Vector bt = new Vector();
		bt.add("BLACK");
		bt.add("WHITE");
		bt.add("STEP");
		bt.add("WINER");
		bt.add("DATE");
		// -----创建内容--------------
		Vector data1 = new Vector();
		data1.add("g002.player.AI");
		data1.add("g002.player.AI");
		data1.add("g002.player.AI");
		data1.add("g002.player.AI");
		data1.add("2019/12/05 17:04:49");
		Vector data2 = new Vector();
		data2.add("g002.player.AI");
		data2.add("g002.player.AI");
		data2.add("g002.player.AI");
		data2.add("g002.player.AI");
		data2.add("2019/12/05 17:04:49");
		Vector data3 = new Vector();
		data3.add("g002.player.AI");
		data3.add("g002.player.AI");
		data3.add("g002.player.AI");
		data3.add("g002.player.AI");
		data3.add("2019/12/05 17:04:49");
		Vector datas = new Vector();
		datas.add(data1);
		datas.add(data2);
		datas.add(data3);
		// -----表格模式--------------
		model = new DefaultTableModel(datas, bt);
		table = new JTable(model);
		rightScroll = new JScrollPane(table);
		rightScroll.setRowHeaderView(new RowHeaderTable(table, 40));
		// rightScroll = new JScrollPane(textArea);
		rightScroll.setBorder(new TitledBorder("Games"));
		leftScroll = new JScrollPane(playerList);
		leftScroll.setBorder(new TitledBorder("棋手列表"));

		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
		centerSplit.setDividerLocation(100);

		frame = new JFrame("博弈开发平台");
		// 更改JFrame的图标：
		// frame.setIconImage(Toolkit.getDefaultToolkit().createImage(Client.class.getResource("qq.jpg")));
		frame.setLayout(new BorderLayout());
		frame.setJMenuBar(mb);
		frame.add(northPanel, "North");
		frame.add(centerSplit, "Center");
		frame.setSize(800, 600);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		frame.setLocation((screen_width - frame.getWidth()) / 2, (screen_height - frame.getHeight()) / 2);
		frame.setVisible(true);

		// 获取与服务端连接
		this.socket = socket;
		try {
			writer = new PrintWriter(socket.getOutputStream());
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		MessageThread mt = new MessageThread(reader);
		messageThread = new Thread(mt);
		messageThread.start();
		
		isConnected = true;// 已经连接上了
		for (String playerName : allPlayer) {
			listModel.addElement(playerName);
		}
		//服务器棋手间对战
		serverBattle.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
			}


			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
			}


			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				new ServerBattle(allPlayer,socket,mt);
			}
		});
		// 用户列表单击事件
		playerList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							// 找到对弈棋手，新建game
							String player = user.getPlayerName();
							try {
								Class clazz = Class.forName(player);
								Player clientPlayer = (Player) clazz.newInstance();

								serverPlayer = new SocketDelegate((String) playerList.getSelectedValue(), socket);
								mt.addObserver(serverPlayer);
								// 向服务端发信号 开始下棋
								writer.println("PLAYCHESS@" + username + "@" + (String) playerList.getSelectedValue()+"@" + isFirst);
								writer.flush();
								// 开启服务端下棋监听
						
								int timeLimit = 30000;
								Game game = null;
								if(isFirst){
									game = new Game(clientPlayer, serverPlayer,timeLimit);
								}else{
									game = new Game(serverPlayer,clientPlayer,timeLimit);
								}
								game.start();
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
					}).start();
				}
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}
		});
		// 关闭窗口时事件
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing   " + isConnected);
				if (isConnected) {
					closeConnection();// 关闭连接
				}
				System.exit(0);// 退出程序
			}
		});
	}
	/**
	 * 客户端主动关闭连接
	 */
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// 发送断开连接命令给服务器
			messageThread.stop();// 停止接受消息线程
			// 释放资源
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;
			return true;
		} catch (IOException e1) {
			e1.printStackTrace();
			isConnected = true;
			return false;
		}
	}
	
	public void sendMessage(String message) {
		System.out.println("sendMessage_"+message);
		writer.println(message);
		writer.flush();
	}

	// 不断接收消息的线程
	class MessageThread extends Observable implements Runnable {
		private BufferedReader reader;

		// 接收消息线程的构造方法
		public MessageThread(BufferedReader reader) {
			this.reader = reader;
		}

		// 被动的关闭连接
		public synchronized void closeCon() throws Exception {
			// 清空用户列表
			listModel.removeAllElements();
			// 被动的关闭连接释放资源
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// 修改状态为断开
		}

		public void run() {
			String message = "";
			while (true) {
				try {
					message = reader.readLine();
					System.out.println(message);
					StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
					String command = stringTokenizer.nextToken();// 命令
					if (command.equals("CLOSE"))// 服务器已关闭命令
					{
						closeCon();// 被动的关闭连接
						JOptionPane.showMessageDialog(frame, "服务器关闭！", "错误", JOptionPane.ERROR_MESSAGE);
						return;// 结束线程
					} else if (command.equals("ADD")) {// 有用户上线更新在线列表
						String username = "";
						String userIp = "";
						if ((username = stringTokenizer.nextToken()) != null
								&& (userIp = stringTokenizer.nextToken()) != null) {
							User user = new User(username, userIp);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("DELETE")) {// 有用户下线更新在线列表
						String username = stringTokenizer.nextToken();
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
					} else if (command.equals("USERLIST")) {// 加载在线用户列表
						int size = Integer.parseInt(stringTokenizer.nextToken());
						String username = null;
						String userIp = null;
						for (int i = 0; i < size; i++) {
							username = stringTokenizer.nextToken();
							userIp = stringTokenizer.nextToken();
							User user = new User(username, userIp);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("MAX")) {// 人数已达上限
						closeCon();// 被动的关闭连接
						JOptionPane.showMessageDialog(frame, "服务器缓冲区已满！", "错误", JOptionPane.ERROR_MESSAGE);
						return;// 结束线程
					} else if(command.equals("SERVERGAME")){
						
						System.out.println("SERVERGAME=======================");
					} else {
						setChanged();
						notifyObservers(message);
					}
					leftScroll.repaint();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
