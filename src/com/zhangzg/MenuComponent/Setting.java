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
	
	private Label text = new Label("更多功能，敬请期待！");
	
	

	public Setting(){
		this.setLayout(new FlowLayout());
		this.setSize(300, 200);
		this.setTitle("TankWar");

		this.add(text);
		//监听器，，，监听窗口关闭
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				e.getWindow().dispose();
			}	
		});
		//这置为false，，让游戏窗口不能缩放
		this.setResizable(false);
		//背景
		this.setBackground(Color.WHITE);
		
		this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
		//可见
		this.setVisible(true);
	}
	
}
