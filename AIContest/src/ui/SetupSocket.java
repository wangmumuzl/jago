/**
 * 
 */
package ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JRootPane;
import javax.swing.JTextField;

import entity.User;

/**
 * @author 朱琳
 *
 */
public class SetupSocket extends JFrame {

	private static final long serialVersionUID = 4545179570573907458L;
	// 地址
	private JTextField addr;
	// 端口号
	private JTextField port;
	// 小容器
	private JLabel addrL;
	private JLabel portL;
	private JLabel title;
	// 小按钮
	private JButton cancel;
	private JButton confirm;
	// 复选框
	// 列表框
	private JComboBox<String> cb1;

	private Login login;

	public SetupSocket(JFrame jf) {
		login=(Login) jf;
		// 设置登录窗口标题
		this.setTitle("设置");
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
		// 窗体大小不能改变
		this.setResizable(false);
		// 居中显示
		this.setLocationRelativeTo(null);
		// 窗体显示
		this.setVisible(true);
		//不获取焦点
		this.setFocusable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}

	/**
	 * 窗体组件初始化
	 */
	public void init() {
		Container container = this.getContentPane();
		
		title = new JLabel();
		title.setBounds(55, 30, 80, 30);
		title.setText("服务器设置");
		addrL = new JLabel();
		addrL.setBounds(55, 80, 50, 30);
		addrL.setText("地址：");
		portL = new JLabel();
		portL.setBounds(55, 130, 50, 30);
		portL.setText("端口：");
		// 地址输入框
		addr = new JTextField();
		addr.setText("127.0.0.1");
		addr.setForeground(new Color(156, 156, 156));
		addr.setBounds(120, 80, 150, 30);
		addr.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (addr.getText().equals("")) {
					addr.setText("127.0.0.1");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				addr.setForeground(new Color(0, 0, 0));
			}
		});

		// 端口号输入框
		port = new JTextField();
		port.setText("6666");
		port.setForeground(new Color(156, 156, 156));
		port.setBounds(120, 130, 150, 30);
		port.addFocusListener(new FocusListener() {

			@Override
			public void focusLost(FocusEvent e) {
				// TODO Auto-generated method stub
				if (port.getText().equals("")) {
					port.setText("6666");
				}
			}

			@Override
			public void focusGained(FocusEvent e) {
				// TODO Auto-generated method stub
				port.setForeground(new Color(0, 0, 0));
			}
		});
		
		confirm = new JButton("确认");
		// 设置字体和颜色和手形指针
		confirm.setFont(new Font("宋体", Font.PLAIN, 12));
		confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		confirm.setBounds(80, 190, 60, 25);
		// 给按钮添加
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.setPort(Integer.parseInt(port.getText().trim()));
				login.setHostIp(addr.getText());
				getFrame().dispose();
			}
		});
		
		cancel = new JButton("取消");
		// 设置字体和颜色和手形指针
		cancel.setFont(new Font("宋体", Font.PLAIN, 12));
		cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cancel.setBounds(200, 190, 60, 25);
		// 给按钮添加
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().dispose();
			}
		});
		// 所有组件用容器装载
		container.add(title);
		container.add(addrL);
		container.add(addr);
		container.add(portL);
		container.add(port);
		container.add(confirm);
		container.add(cancel);
	}
	
	public JFrame getFrame(){
		return this;
	}
}
