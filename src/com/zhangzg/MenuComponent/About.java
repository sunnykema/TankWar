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

public class About extends Frame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
private Label text = new Label("��������");
	
	private TextArea textArea = new TextArea("  ���ǵ�С�����Դϡ����Ǹߡ���������ɣ�̹�˴�ս��Ϊһ��ʵ���Ժ�ǿ����Ϸ�������ǵ���Ϸ�У�������A*�㷨���Ŵ��㷨������״̬����ʹ��Ϸ��AI����������״̬����������ײ��⡢�ӵ��ļ�⡢�ѻ���⣬Ϊ����ṩ����ʵ�ĳ������ڲ������棬ֻ��Ҫ��������������ɣ�����ѧ��----From Tankwar Team.",200,70,TextArea.SCROLLBARS_NONE );
	
	public About(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));
		this.setSize(500, 400);
		this.setTitle("TankWar");
		text.setFont(new Font("����",Font.ITALIC,50));
		text.setSize(500, 50);
		textArea.setEditable(false);
		textArea.setSize(400, 300);
		textArea.setBounds(10, 10, 480, 380);
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
