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
 * @author ����
 *
 */
public class MainFrame extends Observable{
	private JFrame frame;
	private JList playerList;
	private JMenuBar mb;
	private JMenu more, edit, help;
	private JMenuItem serverBattle, about;//����˵�ѡ��
	
	private JPanel northPanel;
	private JScrollPane rightScroll;
	private DefaultTableModel model;
	private JTable table;
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;
	private DefaultListModel listModel;
	private JLabel welcome;

	private boolean isConnected = false;
	private Thread messageThread;// ���������Ϣ���߳�
	private Socket socket;
	private PrintWriter writer;
	private BufferedReader reader;
	private Map<String, User> onLineUsers = new HashMap<String, User>();// ���������û�
	private String username;//��ǰ�û�
	private SocketDelegate serverPlayer;
	private int gameId;//��ʼ����Ϊ������  1
	private boolean isFirst;
	
	public MainFrame(User user, Socket socket ,String[] allPlayer,boolean isFirst) {
		this.username=user.getName();
		this.gameId=1;
		mb=new JMenuBar();
		more = new JMenu(" �����ս��ʽ");
		serverBattle = new JMenuItem(" �����������ֶ�ս");
		more.add(serverBattle);
		
		help = new JMenu("����");
		about = new JMenuItem("���ڲ���ƽ̨");
		help.add(about);
		
		mb.add(more);
		mb.add(help);
		
		listModel = new DefaultListModel();
		playerList = new JList(listModel);
		
		welcome = new JLabel("   ��ӭ " + this.username + "ͬѧ��");
		welcome.setPreferredSize(new Dimension(200, 40));
		northPanel = new JPanel();
		northPanel.setLayout(new GridLayout(1, 7));
		northPanel.add(welcome);
		// -----��������--------------
		Vector bt = new Vector();
		bt.add("BLACK");
		bt.add("WHITE");
		bt.add("STEP");
		bt.add("WINER");
		bt.add("DATE");
		// -----��������--------------
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
		// -----���ģʽ--------------
		model = new DefaultTableModel(datas, bt);
		table = new JTable(model);
		rightScroll = new JScrollPane(table);
		rightScroll.setRowHeaderView(new RowHeaderTable(table, 40));
		// rightScroll = new JScrollPane(textArea);
		rightScroll.setBorder(new TitledBorder("Games"));
		leftScroll = new JScrollPane(playerList);
		leftScroll.setBorder(new TitledBorder("�����б�"));

		centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll, rightScroll);
		centerSplit.setDividerLocation(100);

		frame = new JFrame("���Ŀ���ƽ̨");
		// ����JFrame��ͼ�꣺
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

		// ��ȡ����������
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
		
		isConnected = true;// �Ѿ���������
		for (String playerName : allPlayer) {
			listModel.addElement(playerName);
		}
		//���������ּ��ս
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
		// �û��б����¼�
		playerList.addMouseListener(new MouseListener() {
			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				if (e.getClickCount() == 2) {
					new Thread(new Runnable() {
						@Override
						public void run() {
							// TODO Auto-generated method stub
							// �ҵ��������֣��½�game
							String player = user.getPlayerName();
							try {
								Class clazz = Class.forName(player);
								Player clientPlayer = (Player) clazz.newInstance();

								serverPlayer = new SocketDelegate((String) playerList.getSelectedValue(), socket);
								mt.addObserver(serverPlayer);
								// �����˷��ź� ��ʼ����
								writer.println("PLAYCHESS@" + username + "@" + (String) playerList.getSelectedValue()+"@" + isFirst);
								writer.flush();
								// ����������������
						
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
		// �رմ���ʱ�¼�
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("windowClosing   " + isConnected);
				if (isConnected) {
					closeConnection();// �ر�����
				}
				System.exit(0);// �˳�����
			}
		});
	}
	/**
	 * �ͻ��������ر�����
	 */
	@SuppressWarnings("deprecation")
	public synchronized boolean closeConnection() {
		try {
			sendMessage("CLOSE");// ���ͶϿ����������������
			messageThread.stop();// ֹͣ������Ϣ�߳�
			// �ͷ���Դ
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

	// ���Ͻ�����Ϣ���߳�
	class MessageThread extends Observable implements Runnable {
		private BufferedReader reader;

		// ������Ϣ�̵߳Ĺ��췽��
		public MessageThread(BufferedReader reader) {
			this.reader = reader;
		}

		// �����Ĺر�����
		public synchronized void closeCon() throws Exception {
			// ����û��б�
			listModel.removeAllElements();
			// �����Ĺر������ͷ���Դ
			if (reader != null) {
				reader.close();
			}
			if (writer != null) {
				writer.close();
			}
			if (socket != null) {
				socket.close();
			}
			isConnected = false;// �޸�״̬Ϊ�Ͽ�
		}

		public void run() {
			String message = "";
			while (true) {
				try {
					message = reader.readLine();
					System.out.println(message);
					StringTokenizer stringTokenizer = new StringTokenizer(message, "/@");
					String command = stringTokenizer.nextToken();// ����
					if (command.equals("CLOSE"))// �������ѹر�����
					{
						closeCon();// �����Ĺر�����
						JOptionPane.showMessageDialog(frame, "�������رգ�", "����", JOptionPane.ERROR_MESSAGE);
						return;// �����߳�
					} else if (command.equals("ADD")) {// ���û����߸��������б�
						String username = "";
						String userIp = "";
						if ((username = stringTokenizer.nextToken()) != null
								&& (userIp = stringTokenizer.nextToken()) != null) {
							User user = new User(username, userIp);
							onLineUsers.put(username, user);
							listModel.addElement(username);
						}
					} else if (command.equals("DELETE")) {// ���û����߸��������б�
						String username = stringTokenizer.nextToken();
						User user = (User) onLineUsers.get(username);
						onLineUsers.remove(user);
						listModel.removeElement(username);
					} else if (command.equals("USERLIST")) {// ���������û��б�
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
					} else if (command.equals("MAX")) {// �����Ѵ�����
						closeCon();// �����Ĺر�����
						JOptionPane.showMessageDialog(frame, "������������������", "����", JOptionPane.ERROR_MESSAGE);
						return;// �����߳�
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
