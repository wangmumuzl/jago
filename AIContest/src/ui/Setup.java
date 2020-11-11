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
 * @author ����
 * 1.������ʾ��¼�����ֵ���������
 * 2.ѡ�񱾵ػ�Զ�����ֽ�����
 *   ��1��ѡ�񱾵����֣���Ҫ��������һ���ֵ���������
 *   ��2��ѡ���������֣������ӷ�����
 */
public class Setup extends JFrame{
		// �ҷ�����
		private JTextField weClass;
		//�Է�����
		private JTextField otherClass;
		// С����
		private JLabel we;
		private JLabel other;
		private JLabel title;
		private JLabel example;
		// С��ť
		private JButton cancel;
		private JButton confirm;
		// ��ѡ��
		// �б��
		private JComboBox<String> cb1;
		
		private JRadioButton localButton;
	    private JRadioButton serverButton;
	    
	    private JRadioButton first ;
	    private JRadioButton later ;
	    private boolean isFirst;
	    
		public Setup() {
			// ���õ�¼���ڱ���
			this.setTitle("��������");
			// ȥ�����ڵ�װ��(�߿�)
			// this.setUndecorated(true);
			// ����ָ���Ĵ���װ�η��
			this.getRootPane().setWindowDecorationStyle(JRootPane.NONE);
			// ���������ʼ��
			init();
			this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// ���ò���Ϊ���Զ�λ
			this.setLayout(null);
			this.setSize(355, 340);
			int screen_width = Toolkit.getDefaultToolkit().getScreenSize().width;
			int screen_height = Toolkit.getDefaultToolkit().getScreenSize().height;
			this.setLocation((screen_width - this.getWidth()) / 2, (screen_height - this.getHeight()) / 2);
			// �����С���ܸı�
			this.setResizable(false);
			// ������ʾ
			this.setLocationRelativeTo(null);
			// ������ʾ
			this.setVisible(true);
			//����ȡ����
			this.setFocusable(false);
			this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		}

		/**
		 * ���������ʼ��
		 */
		public void init() {
			Container container = this.getContentPane();
			
			we = new JLabel();
			we.setBounds(55, 30, 80, 30);
			we.setText("�ҷ����֣�");
			
			// �ҷ���������� 
			weClass = new JTextField();
			weClass.setBounds(120, 30, 150, 30);
			
			first = new JRadioButton("����");
			first.setBounds(120, 70, 70, 30);
			later = new JRadioButton("����");
			later.setBounds(200, 70, 70, 30);
			//ѡ�����ֺ���
			ButtonGroup group1 = new ButtonGroup();
			group1.add(first);
			group1.add(later);
			first.setSelected(true);
			
			other = new JLabel();
			other.setBounds(55, 120, 80, 30);
			other.setText("�Է����֣�");
			
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
			// �Է��������������   ���ļ��ж�ȡ
			otherClass = new JTextField();
			otherClass.setBounds(120, 160, 150, 30);
			
			example = new JLabel("(�����ʽ�磺baseline.player.AI)");
			example.setBounds(55, 195, 300, 30);
			example.setForeground(new Color(178,48,96));
			
			confirm = new JButton("ȷ��");
			// �����������ɫ������ָ��
			confirm.setFont(new Font("����", Font.PLAIN, 12));
			confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			confirm.setBounds(80, 250, 60, 25);
			// ����ť���
			confirm.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
//					
					if(weClass.getText().equals("")){
						example.setText("��������������baseline.player.AI");
						weClass.setBackground(new Color(250,128,114));
					}else{
						try {
							weClass.setBackground(new Color(255,255,255));
							Class clazz1 = Class.forName(weClass.getText().trim());
							if(localButton.isSelected()){
								if(otherClass.getText().equals("")){
									example.setText("��������������baseline.player.AI");
									otherClass.setBackground(new Color(250,128,114));
								}else{
									otherClass.setBackground(new Color(255,255,255));
									Class clazz2 = Class.forName(otherClass.getText());
								
									//�ҷ�����
									Player we = (Player) clazz1.newInstance();
									//�з�����
									Player other = (Player) clazz2.newInstance();
									
									int timeLimit = 30;
									
									//�ҷ��ļ�ʱ��
									GameTimer weTimer = new GameTimer(timeLimit);	
									we.setTimer(weTimer);
									
									//�з��ļ�ʱ��
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
							example.setText("������ȷ���ɶ�Ӧ����");
						}
						
					}
				}
			});
			
			cancel = new JButton("ȡ��");
			// �����������ɫ������ָ��
			cancel.setFont(new Font("����", Font.PLAIN, 12));
			cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
			cancel.setBounds(200, 250, 60, 25);
			// ����ť���
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					getFrame().dispose();
				}
			});
			
			// �������������װ��
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
		 * ���ļ��ж�ȡ����
		 */
		private void fileRead() {
			// TODO Auto-generated method stub
			BufferedReader reader = null ;
			//���������������ļ��ж�ȡ
			try {
				reader = new BufferedReader(new FileReader(new File("player.txt")));
				String order=reader.readLine(); //���ֺ���
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
		 * ��ס���������
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
