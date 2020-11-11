package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import entity.GameInfo;
import entity.User;

public class Login extends JFrame {

	private static final long serialVersionUID = -6788045638380819221L;
	// �û���
	private JTextField ulName;
	// ����
	private JPasswordField ulPasswd;
	// С����
	private JLabel j1;
	private JLabel j2;
	private JLabel j3;
	// С��ť
	private JButton login;
	// �б��
	private JComboBox<String> clogin;

	// ���ӷ�����
	// ��ַ
	private int port;
	private String hostIp;
	private boolean isFirst;
	private String playerName;
	private ArrayList<GameInfo> games;
	
	public Login(boolean isFirst,String playerName) {
		// ���õ�¼���ڱ���
		this.setTitle("���Ŀ���ƽ̨");
		// ȥ�����ڵ�װ��(�߿�)
		// this.setUndecorated(true);
		// ����ָ���Ĵ���װ�η��
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		// ���������ʼ��
		init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// ���ò���Ϊ���Զ�λ
		this.setLayout(null);
		this.setSize(355, 265);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screen_width - this.getWidth()) / 2, (screen_height - this.getHeight()) / 2);
		// ���ô����ͼ��
//		Image img0 = new ImageIcon("D:/logo.png").getImage();
//		this.setIconImage(img0);
		// �����С���ܸı�
		this.setResizable(false);
		// ������ʾ
		this.setLocationRelativeTo(null);
		// ������ʾ
		this.setVisible(true);
		// �������ӷ�������ַ�Ͷ˿�Ĭ��ֵ
		this.hostIp = "127.0.0.1";
		this.port = 6666;
		this.isFirst = isFirst;
		this.playerName = playerName;
	}

	/**
	 * ���������ʼ��
	 */
	public void init() {
		// ����һ������,���е�ͼƬ��С��setBounds�������ĸ�����Ҫ����һ��(��Ҫ�Լ�����ü�)
		Container container = this.getContentPane();
		j1 = new JLabel();
		// ���ñ���ɫ
		Image img1 = new ImageIcon("./src/img/background.jpg").getImage();
		j1.setIcon(new ImageIcon(img1));
		j1.setBounds(0, 0, 355, 265);
		// qqͷ���趨
		j2 = new JLabel();
		Image img2 = new ImageIcon("./src/img/avatar.jpg").getImage();
		j2.setIcon(new ImageIcon(img2));
		j2.setBounds(55, 80, 50, 53);

		j3 = new JLabel();
		Image img3 = new ImageIcon("./src/img/setup.jpg").getImage();
		j3.setIcon(new ImageIcon(img3));
		j3.setBounds(0, 210, 25, 25);
		j3.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				new SetupSocket(getFrame());
			}
		});
		// �û��������
		ulName = new JTextField();
		ulName.setText("�û���");
		ulName.setForeground(new Color(156, 156, 156));
		ulName.setBounds(120, 80, 150, 30);
		ulName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (ulName.getText().equals("")) {
					ulName.setText("�û���");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ulName.setText("");
			}
		});
		// ע���˺�
		// ���������
		ulPasswd = new JPasswordField();
		ulPasswd.setEchoChar((char) 0);
		ulPasswd.setText("����");
		ulPasswd.setForeground(new Color(156, 156, 156));
		ulPasswd.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (ulPasswd.getText().equals("")) {
					ulPasswd.setText("����");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ulPasswd.setText("");
			}
		});
		ulPasswd.setBounds(120, 130, 150, 30);
		// �һ�����
		// ��ס����
		// �Զ���½
		// �û���½״̬ѡ��
		clogin = new JComboBox<String>();
		clogin.addItem("����");
		clogin.addItem("����");
		clogin.setBounds(55, 140, 55, 20);
		// ��½��ť
		login = new JButton("��¼");
		// �����������ɫ������ָ��
		login.setFont(new Font("����", Font.PLAIN, 12));
		login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		login.setBounds(140, 180, 100, 25);
		// ����¼��ť���
		login.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				User user =new User();
				user.setUsername(ulName.getText());
				user.setPassword(ulPasswd.getText());
				Socket socket=connectServer(port, hostIp, user);
				if(socket!=null){
					try {
						BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
						games= new ArrayList<GameInfo>();
						String[] inf = null;
						while(true){
							inf = reader.readLine().split("@");
							if(inf[0].equals("GameInfo")){//GameInfo@gameId@blackName@whiteName@winerName@step@date
								GameInfo gameInfo = new GameInfo();
								
							}else{
								break;
							}
						}
						if(inf[0].equals("success")){
							user.setPlayerName(playerName);
							user.setName(inf[1]);
							String[] allplayer = reader.readLine().split("@");
							JOptionPane.showMessageDialog(null, "��¼�ɹ�", "��ϲ", JOptionPane.DEFAULT_OPTION);
							new MainFrame(user,socket,allplayer,isFirst);
							getFrame().dispose();
						}else{
							JOptionPane.showMessageDialog(null, "�û��������벻ƥ��", "����", JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		// �������������װ��
		j1.add(j2);
		j1.add(j3);
		j1.add(clogin);
		j1.add(login);
		container.add(j1);
		container.add(ulName);
		container.add(ulPasswd);
	}

	public Socket connectServer(int port, String hostIp, User user) {
		// ���ӷ�����
		try {
			System.out.println("hostIp="+hostIp+"  port"+port);
			Socket socket = new Socket(hostIp, port);// ���ݶ˿ںźͷ�����ip��������
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
 
			// ���Ϳͻ����û�������Ϣ(�û�����ip��ַ)
			writer.println(user.getUsername() +"@"+user.getPassword()+ "@" + socket.getLocalAddress().toString());
			writer.flush();
			// ����������Ϣ���߳�
			return socket;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "��˿ں�Ϊ��" + port + "    IP��ַΪ��" + hostIp + "   �ķ���������ʧ��!" + "\r\n", "����", JOptionPane.ERROR_MESSAGE);
			e.printStackTrace();
			return null;
		}
	}

	public JFrame getFrame() {
		return this;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostIp() {
		return hostIp;
	}

	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}

}