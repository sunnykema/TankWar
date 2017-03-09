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
	
	private Label text = new Label("玩法简介");
	
	private TextArea textArea = new TextArea("  利用键盘控制\n"
			+ "  a键是放外挂技能，x键是放小炮。\n"
			+ "  ↑操作为向上运动，↓操作为向下运动，←操作为向左运动，→操作为向右运动\n"
			+ "  游戏中玩家具有一定的生命值，游戏中也会出现随机的生命点可以补充生命。\n"
			+ "  当子弹打中坦克5次之后，坦克会因此死亡。\n"
			+ "  如果想继续玩下去，那么按下键盘键F2即可复活。\n"
			+ "  游戏过程中，如果敌方打到我方老家，按F3可以复活老家。",200,70,TextArea.SCROLLBARS_NONE );
	
	public Method(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));
		this.setSize(500, 400);
		this.setTitle("TankWar");
		text.setFont(new Font("楷体",Font.ITALIC,50));
		text.setSize(500, 50);
		textArea.setEditable(false);
		textArea.setSize(400, 300);
		this.add(text);
		this.add(textArea);
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
