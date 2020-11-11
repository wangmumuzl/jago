/**
 * 
 */
package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Observer;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import core.game.Game;
import core.player.Player;
import core.player.SocketDelegate;
import core.player.SocketPlayer;
import jagoclient.Global;
import jagoclient.board.GoFrame;
import jagoclient.board.LocalGoFrame;
import ui.MainFrame.MessageThread;

/**
 * @author 朱琳
 *
 */
public class ServerBattle extends JFrame {
	// 我方棋手
	private JTextField weClass;
	// 小容器
	private JLabel black;
	private JLabel white;
	private JComboBox<String> blackPlayer;
	private JComboBox<String> whitePlayer;
	// 小按钮
	private JButton cancel;
	private JButton confirm;

	private String[] serverPlayerList;
	private Socket socket;
	private PrintWriter writer;
	
	private MessageThread mt;
	
	public ServerBattle(String[] allPlayer,Socket socket,MessageThread mt) {
		this.serverPlayerList = allPlayer;
		this.socket = socket;
		this.mt = mt;
		// 设置登录窗口标题
		this.setTitle("服务端间棋手对战");
		// 去掉窗口的装饰(边框)
		// this.setUndecorated(true);
		// 采用指定的窗口装饰风格
		this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
		// 窗体组件初始化
		init();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// 设置布局为绝对定位
		this.setLayout(null);
		this.setSize(355, 340);
		int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
		int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
		this.setLocation((screen_width - this.getWidth()) / 2, (screen_height - this.getHeight()) / 2);
		// 窗体大小不能改变
		this.setResizable(false);
		// 居中显示
		this.setLocationRelativeTo(null);
		// 窗体显示
		this.setVisible(true);
		// 不获取焦点
		this.setFocusable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * 窗体组件初始化
	 */
	public void init() {
		Container container = this.getContentPane();

		black = new JLabel();
		black.setBounds(55, 30, 80, 30);
		black.setText("黑方棋手：");
		blackPlayer = new JComboBox<>(serverPlayerList);
		blackPlayer.setBounds(130, 30, 120, 30);
		// 黑方棋手选择框
		weClass = new JTextField();
		weClass.setBounds(120, 30, 150, 30);

		white = new JLabel();
		white.setBounds(55, 120, 80, 30);
		white.setText("对方棋手：");
		whitePlayer = new JComboBox<>(serverPlayerList);
		whitePlayer.setBounds(130, 120, 120, 30);
		// 白方棋手类名输入框 从文件中读取

		confirm = new JButton("确认");
		// 设置字体和颜色和手形指针
		confirm.setFont(new Font("宋体", Font.PLAIN, 12));
		confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		confirm.setBounds(80, 250, 60, 25);
		// 给按钮添加
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//将选中的棋手发送给服务端
				String blackPlayerName = (String) blackPlayer.getSelectedItem();
				String whitePlayerName = (String) whitePlayer.getSelectedItem();
				System.out.println(blackPlayerName+"@"+whitePlayerName);
				try {
					writer = new PrintWriter(socket.getOutputStream());
					// 向服务端发信号 开始下棋
					writer.println("SERVERGAME@" + blackPlayerName + "@" + whitePlayerName);
					writer.flush();
					
					// 开启服务端下棋监听
					SocketPlayer black = new SocketPlayer(blackPlayerName, socket,"black");
					SocketPlayer white = new SocketPlayer(whitePlayerName, socket,"white");
					mt.addObserver(black);
					mt.addObserver(white);
					
					int timeLimit = 30000;
					Game game = new Game(black, white,timeLimit);;
					game.start();
					getFrame().dispose();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
			}
		});

		cancel = new JButton("取消");
		// 设置字体和颜色和手形指针
		cancel.setFont(new Font("宋体", Font.PLAIN, 12));
		cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cancel.setBounds(200, 250, 60, 25);
		// 给按钮添加
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().dispose();
			}
		});

		// 所有组件用容器装载
		container.add(black);
		container.add(white);
		container.add(confirm);
		container.add(cancel);
		container.add(blackPlayer);
		container.add(whitePlayer);
	}

	public ServerBattle getFrame() {
		return this;
	}

//	public static void main(String[] args) {
//		String[] ss=new String[]{"hello","world"};
//		new ServerBattle(ss);
//	}
}
