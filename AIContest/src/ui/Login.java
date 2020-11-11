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
	// 用户名
	private JTextField ulName;
	// 密码
	private JPasswordField ulPasswd;
	// 小容器
	private JLabel j1;
	private JLabel j2;
	private JLabel j3;
	// 小按钮
	private JButton login;
	// 列表框
	private JComboBox<String> clogin;

	// 连接服务器
	// 地址
	private int port;
	private String hostIp;
	private boolean isFirst;
	private String playerName;
	private ArrayList<GameInfo> games;
	
	public Login(boolean isFirst,String playerName) {
		// 设置登录窗口标题
		this.setTitle("博弈开发平台");
		// 去掉窗口的装饰(边框)
		// this.setUndecorated(true);
		// 采用指定的窗口装饰风格
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		// 窗体组件初始化
		init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置布局为绝对定位
		this.setLayout(null);
		this.setSize(355, 265);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screen_width - this.getWidth()) / 2, (screen_height - this.getHeight()) / 2);
		// 设置窗体的图标
//		Image img0 = new ImageIcon("D:/logo.png").getImage();
//		this.setIconImage(img0);
		// 窗体大小不能改变
		this.setResizable(false);
		// 居中显示
		this.setLocationRelativeTo(null);
		// 窗体显示
		this.setVisible(true);
		// 设置连接服务器地址和端口默认值
		this.hostIp = "127.0.0.1";
		this.port = 6666;
		this.isFirst = isFirst;
		this.playerName = playerName;
	}

	/**
	 * 窗体组件初始化
	 */
	public void init() {
		// 创建一个容器,其中的图片大小和setBounds第三、四个参数要基本一致(需要自己计算裁剪)
		Container container = this.getContentPane();
		j1 = new JLabel();
		// 设置背景色
		Image img1 = new ImageIcon("./src/img/background.jpg").getImage();
		j1.setIcon(new ImageIcon(img1));
		j1.setBounds(0, 0, 355, 265);
		// qq头像设定
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
		// 用户名输入框
		ulName = new JTextField();
		ulName.setText("用户名");
		ulName.setForeground(new Color(156, 156, 156));
		ulName.setBounds(120, 80, 150, 30);
		ulName.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (ulName.getText().equals("")) {
					ulName.setText("用户名");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ulName.setText("");
			}
		});
		// 注册账号
		// 密码输入框
		ulPasswd = new JPasswordField();
		ulPasswd.setEchoChar((char) 0);
		ulPasswd.setText("密码");
		ulPasswd.setForeground(new Color(156, 156, 156));
		ulPasswd.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (ulPasswd.getText().equals("")) {
					ulPasswd.setText("密码");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				ulPasswd.setText("");
			}
		});
		ulPasswd.setBounds(120, 130, 150, 30);
		// 找回密码
		// 记住密码
		// 自动登陆
		// 用户登陆状态选择
		clogin = new JComboBox<String>();
		clogin.addItem("在线");
		clogin.addItem("隐身");
		clogin.setBounds(55, 140, 55, 20);
		// 登陆按钮
		login = new JButton("登录");
		// 设置字体和颜色和手形指针
		login.setFont(new Font("宋体", Font.PLAIN, 12));
		login.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		login.setBounds(140, 180, 100, 25);
		// 给登录按钮添加
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
							JOptionPane.showMessageDialog(null, "登录成功", "恭喜", JOptionPane.DEFAULT_OPTION);
							new MainFrame(user,socket,allplayer,isFirst);
							getFrame().dispose();
						}else{
							JOptionPane.showMessageDialog(null, "用户名与密码不匹配", "错误", JOptionPane.ERROR_MESSAGE);
						}
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
		});
		// 所有组件用容器装载
		j1.add(j2);
		j1.add(j3);
		j1.add(clogin);
		j1.add(login);
		container.add(j1);
		container.add(ulName);
		container.add(ulPasswd);
	}

	public Socket connectServer(int port, String hostIp, User user) {
		// 连接服务器
		try {
			System.out.println("hostIp="+hostIp+"  port"+port);
			Socket socket = new Socket(hostIp, port);// 根据端口号和服务器ip建立连接
			PrintWriter writer = new PrintWriter(socket.getOutputStream());
 
			// 发送客户端用户基本信息(用户名和ip地址)
			writer.println(user.getUsername() +"@"+user.getPassword()+ "@" + socket.getLocalAddress().toString());
			writer.flush();
			// 开启接收消息的线程
			return socket;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "与端口号为：" + port + "    IP地址为：" + hostIp + "   的服务器连接失败!" + "\r\n", "错误", JOptionPane.ERROR_MESSAGE);
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