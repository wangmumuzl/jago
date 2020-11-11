package ui;
import javax.swing.*;
import java.awt.*;
import javax.swing.table.*;
import javax.swing.event.*;
import java.awt.event.*;
import java.util.Vector;
class TableRowHeaderTest 
{
	public static void main(String[] args) 
	{
		new TableRowHeaderFrame();
	}
}
class TableRowHeaderFrame extends JFrame
{
	public TableRowHeaderFrame(){
		DefaultTableModel model = new DefaultTableModel(50,6);
		JTable table = new JTable(model);
		/*��table����JScrollPane*/
		JScrollPane scrollPane = new JScrollPane(table);
		/*��rowHeaderTable��Ϊrow header����JScrollPane��RowHeaderView����*/
		//-----��������--------------
		Vector bt = new Vector();
		bt.add("���");
		bt.add("����");
		bt.add("����");
		bt.add("�Ա�");
		//-----��������--------------
		Vector data1 = new Vector();
		data1.add("1");
		data1.add("10001");
		data1.add("С��");
		data1.add("��");
		Vector data2 = new Vector();
		data2.add("2");
		data2.add("10002");
		data2.add("С��");
		data2.add("Ů");
		Vector datas = new Vector();
		datas.add(data1);
		datas.add(data2);
		scrollPane.setRowHeaderView(new RowHeaderTable(table,40));
		this.getContentPane().add(scrollPane,BorderLayout.CENTER);
		this.setVisible(true);
		this.setSize(400,200);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}

/**
 * ������ʾRowHeader��JTable��ֻ��Ҫ�������JScrollPane��RowHeaderView����ΪJTable�����б���
 */
class RowHeaderTable extends JTable
{
	private JTable refTable;//��Ҫ���rowHeader��JTable
	/**
	 * ΪJTable���RowHeader��
	 * @param refTable ��Ҫ���rowHeader��JTable  
	 * @param columnWideth rowHeader�Ŀ��
	 */
	public RowHeaderTable(JTable refTable,int columnWidth){
		super(new RowHeaderTableModel(refTable.getRowCount()));
		this.refTable=refTable;
		this.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);//�����Ե����п�
		this.getColumnModel().getColumn(0).setPreferredWidth(columnWidth);
		this.setDefaultRenderer(Object.class,new RowHeaderRenderer(refTable,this));//������Ⱦ��
		this.setPreferredScrollableViewportSize (new Dimension(columnWidth,0));
	}
}
/**
 * ������ʾRowHeader��JTable����Ⱦ��������ʵ�ֶ�̬���ӣ�ɾ���У���Table�����ӡ�ɾ����ʱRowHeader
 * һ��仯����ѡ��ĳ��ʱ��������ɫ�ᷢ���仯
 */
class RowHeaderRenderer extends JLabel implements TableCellRenderer,ListSelectionListener
{
    JTable reftable;//��Ҫ���rowHeader��JTable
	JTable tableShow;//������ʾrowHeader��JTable
    public RowHeaderRenderer(JTable reftable,JTable tableShow)
    {
        this.reftable = reftable;
		this.tableShow=tableShow;
		//���Ӽ�������ʵ�ֵ���reftable��ѡ����ʱ��RowHeader�ᷢ����ɫ�仯
		ListSelectionModel listModel=reftable.getSelectionModel();
		listModel.addListSelectionListener(this); 
    }
    public Component getTableCellRendererComponent(JTable table,Object obj,
		boolean isSelected,boolean hasFocus,int row, int col)
    {
		((RowHeaderTableModel)table.getModel()).setRowCount(reftable.getRowCount());
        JTableHeader header = reftable.getTableHeader();
        this.setOpaque(true);
        setBorder(UIManager.getBorder("TableHeader.cellBorder"));//����ΪTableHeader�ı߿�����
        setHorizontalAlignment(CENTER);//��text������ʾ
        setBackground(header.getBackground());//���ñ���ɫΪTableHeader�ı���ɫ  
        if ( isSelect(row) )    //��ѡȡ��Ԫ��ʱ,��row header�����ó�ѡȡ��ɫ 
        {
            setForeground(Color.white);
            setBackground(Color.lightGray);
        }
        else
        {
            setForeground(header.getForeground());   
        }
		setFont(header.getFont());
        setText(String.valueOf(row+1));
        return this;
    }
	public void valueChanged(ListSelectionEvent e){
		this.tableShow.repaint();
	}
    private boolean isSelect(int row)
    {
        int[] sel = reftable.getSelectedRows();
        for ( int i=0; i<sel.length; i++ )
            if (sel[i] == row ) 
				return true;
        return false;
    }
}

/**
 * ������ʾ��ͷRowHeader��JTable��TableModel����ʵ�ʴ洢����
 */
class RowHeaderTableModel extends AbstractTableModel
{
	private int rowCount;//��ǰJTable������������Ҫ��RowHeader��TableModelͬ��
	public RowHeaderTableModel(int rowCount){
		this.rowCount=rowCount;
	}
	public void setRowCount(int rowCount){
		this.rowCount=rowCount;
	}
	public int getRowCount(){
		return rowCount;
	}
	public int getColumnCount(){
		return 1;
	}
	public Object getValueAt(int row, int column){
		return row;
	}
}