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
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
import core.game.timer.GameTimer;
import core.player.Player;
import jagoclient.Global;
import jagoclient.board.GoFrame;
import jagoclient.board.LocalGoFrame;
import baseline.player.*;

/**
 * @author 朱琳
 * 1.输入显示登录者棋手的完整类名
 * 2.选择本地或远程棋手进对弈
 *   （1）选择本地棋手，需要在输入另一棋手的完整类名
 *   （2）选择服务端棋手，则连接服务器
 */
public class Setup extends JFrame{
		// 我方棋手
		private JTextField weClass;
		//对方棋手
		private JTextField otherClass;
		// 小容器
		private JLabel we;
		private JLabel other;
		private JLabel title;
		private JLabel example;
		// 小按钮
		private JButton cancel;
		private JButton confirm;
		// 复选框
		// 列表框
		private JComboBox<String> cb1;
		
		private JRadioButton localButton;
	    private JRadioButton serverButton;
	    
	    private JRadioButton first ;
	    private JRadioButton later ;
	    private boolean isFirst;
	    
		public Setup() {
			// 设置登录窗口标题
			this.setTitle("棋手设置");
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
			//不获取焦点
			this.setFocusable(false);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

		/**
		 * 窗体组件初始化
		 */
		public void init() {
			Container container = this.getContentPane();
			
			we = new JLabel();
			we.setBounds(55, 30, 80, 30);
			we.setText("我方棋手：");
			
			// 我方类名输入框 
			weClass = new JTextField();
			weClass.setBounds(120, 30, 150, 30);
			
			first = new JRadioButton("先手");
			first.setBounds(120, 70, 70, 30);
			later = new JRadioButton("后手");
			later.setBounds(200, 70, 70, 30);
			//选择先手后手
			ButtonGroup group1 = new ButtonGroup();
			group1.add(first);
			group1.add(later);
			first.setSelected(true);
			
			other = new JLabel();
			other.setBounds(55, 120, 80, 30);
			other.setText("对方棋手：");
			
			localButton = new JRadioButton("Local");
			localButton.setBounds(120, 120, 70, 30);
			serverButton = new JRadioButton("Server");
			serverButton.setBounds(200, 120, 70, 30);
			
			ButtonGroup group2 = new ButtonGroup();
	        group2.add(localButton);
	        group2.add(serverButton);
	        localButton.setSelected(true);
	        
	        localButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					otherClass.enable();
					otherClass.setBackground(new Color(255,255,255));
				}
			});
	        serverButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					// TODO Auto-generated method stub
					otherClass.disable();
					otherClass.setBackground(new Color(205,201,201));
				}
			});
			// 对方棋手类名输入框   从文件中读取
			otherClass = new JTextField();
			otherClass.setBounds(120, 160, 150, 30);
			
			example = new JLabel("(输入格式如：baseline.player.AI)");
			example.setBounds(55, 195, 300, 30);
			example.setForeground(new Color(178,48,96));
			
			confirm = new JButton("确认");
			// 设置字体和颜色和手形指针
			confirm.setFont(new Font("宋体", Font.PLAIN, 12));
			confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			confirm.setBounds(80, 250, 60, 25);
			// 给按钮添加
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					
					if(weClass.getText().equals("")){
						example.setText("请输入类名，如baseline.player.AI");
						weClass.setBackground(new Color(250,128,114));
					}else{
						try {
							weClass.setBackground(new Color(255,255,255));
							Class clazz1 = Class.forName(weClass.getText().trim());
							if(localButton.isSelected()){
								if(otherClass.getText().equals("")){
									example.setText("请输入类名，如baseline.player.AI");
									otherClass.setBackground(new Color(250,128,114));
								}else{
									otherClass.setBackground(new Color(255,255,255));
									Class clazz2 = Class.forName(otherClass.getText());
								
									//我方棋手
									Player we = (Player) clazz1.newInstance();
									//敌方棋手
									Player other = (Player) clazz2.newInstance();
									
									int timeLimit = 30;
									
									//我方的计时器
									GameTimer weTimer = new GameTimer(timeLimit);	
									we.setTimer(weTimer);
									
									//敌方的计时器
									GameTimer otherTimer = new GameTimer(timeLimit);	
									other.setTimer(otherTimer);
									
									Game game = null;
									if(first.isSelected()){
										game = new Game(we, other,timeLimit);
									}else{
										game = new Game(other , we,timeLimit);
									}
									game.start();
									getFrame().fileWrite();
									getFrame().dispose();
								}
							}else{
								getFrame().fileWrite();
								new Login(isFirst,weClass.getText().trim());
								getFrame().dispose();
							}
						} catch (Exception e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
							example.setText("不能正确生成对应的类");
						}
						
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
			container.add(we);
			container.add(weClass);
			container.add(first);
			container.add(later);
			container.add(other);
			container.add(otherClass);
			container.add(confirm);
			container.add(cancel);
			container.add(localButton);
			container.add(serverButton);
			container.add(example);
			
			fileRead();
		}
		
		public Setup getFrame(){
			return this;
		}
		public static void main(String[] args) {
			new Setup();
		}
		/**
		 * 从文件中读取类名
		 */
		private void fileRead() {
			// TODO Auto-generated method stub
			BufferedReader reader = null ;
			//本地棋手类名从文件中读取
			try {
				reader = new BufferedReader(new FileReader(new File("player.txt")));
				String order=reader.readLine(); //先手后手
				if(order!=null){
					if(order.trim().equals("later")){
						later.setSelected(true);
					}
					String myPlayer = reader.readLine();
					if(myPlayer!=null){
						weClass.setText(myPlayer.trim());
						String otherPlayer = reader.readLine();
						if(otherPlayer!=null){
							otherClass.setText(otherPlayer.trim());
						}
					}
				}
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}finally {
				try {
					reader.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}
		/**
		 * 记住输入的类名
		 */
		private void fileWrite() {
			// TODO Auto-generated method stub
			BufferedWriter writer = null;
			try {
				writer=new BufferedWriter(new FileWriter(new File("player.txt")));
				if(first.isSelected()){
					this.isFirst=true;
					writer.write("first\t\n");
				}else{
					this.isFirst=false;
					writer.write("later\t\n");
				}
				writer.write(weClass.getText()+"\t\n"+otherClass.getText());
				writer.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
}
