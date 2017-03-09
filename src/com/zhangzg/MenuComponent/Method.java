package com.zhangzg.MenuComponent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import kema.tank.Tank;

public class Method extends Frame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Label text = new Label("�淨���");
	
	private TextArea textArea = new TextArea("  ���ü��̿���\n"
			+ "  a���Ƿ���Ҽ��ܣ�x���Ƿ�С�ڡ�\n"
			+ "  ������Ϊ�����˶���������Ϊ�����˶���������Ϊ�����˶���������Ϊ�����˶�\n"
			+ "  ��Ϸ����Ҿ���һ��������ֵ����Ϸ��Ҳ������������������Բ���������\n"
			+ "  ���ӵ�����̹��5��֮��̹�˻����������\n"
			+ "  ������������ȥ����ô���¼��̼�F2���ɸ��\n"
			+ "  ��Ϸ�����У�����з����ҷ��ϼң���F3���Ը����ϼҡ�",200,70,TextArea.SCROLLBARS_NONE );
	
	public Method(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));
		this.setSize(500, 400);
		this.setTitle("TankWar");
		text.setFont(new Font("����",Font.ITALIC,50));
		text.setSize(500, 50);
		textArea.setEditable(false);
		textArea.setSize(400, 300);
		this.add(text);
		this.add(textArea);
		//�������������������ڹر�
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				e.getWindow().dispose();
			}	
		});
		//����Ϊfalse��������Ϸ���ڲ�������
		this.setResizable(false);
		//����
		this.setBackground(Color.WHITE);
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
		//�ɼ�
		this.setVisible(true);
	}
}
