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
 * @author ����
 *
 */
public class SetupSocket extends JFrame {

	private static final long serialVersionUID = 4545179570573907458L;
	// ��ַ
	private JTextField addr;
	// �˿ں�
	private JTextField port;
	// С����
	private JLabel addrL;
	private JLabel portL;
	private JLabel title;
	// С��ť
	private JButton cancel;
	private JButton confirm;
	// ��ѡ��
	// �б��
	private JComboBox<String> cb1;

	private Login login;

	public SetupSocket(JFrame jf) {
		login=(Login) jf;
		// ���õ�¼���ڱ���
		this.setTitle("����");
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
		
		title = new JLabel();
		title.setBounds(55, 30, 80, 30);
		title.setText("����������");
		addrL = new JLabel();
		addrL.setBounds(55, 80, 50, 30);
		addrL.setText("��ַ��");
		portL = new JLabel();
		portL.setBounds(55, 130, 50, 30);
		portL.setText("�˿ڣ�");
		// ��ַ�����
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

		// �˿ں������
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
		
		confirm = new JButton("ȷ��");
		// �����������ɫ������ָ��
		confirm.setFont(new Font("����", Font.PLAIN, 12));
		confirm.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		confirm.setBounds(80, 190, 60, 25);
		// ����ť���
		confirm.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				login.setPort(Integer.parseInt(port.getText().trim()));
				login.setHostIp(addr.getText());
				getFrame().dispose();
			}
		});
		
		cancel = new JButton("ȡ��");
		// �����������ɫ������ָ��
		cancel.setFont(new Font("����", Font.PLAIN, 12));
		cancel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		cancel.setBounds(200, 190, 60, 25);
		// ����ť���
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				getFrame().dispose();
			}
		});
		// �������������װ��
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
