package com.zhangzg.MenuComponent;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import kema.tank.Tank;

public class Setting extends Frame{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Label text = new Label("���๦�ܣ������ڴ���");
	
	

	public Setting(){
		this.setLayout(new FlowLayout());
		this.setSize(300, 200);
		this.setTitle("TankWar");

		this.add(text);
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
