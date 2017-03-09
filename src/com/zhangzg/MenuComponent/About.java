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
	
private Label text = new Label("关于我们");
	
	private TextArea textArea = new TextArea("  我们的小组由赵聪、张智高、曾清益组成，坦克大战作为一款实用性很强的游戏，在我们的游戏中，增加了A*算法、遗传算法、有限状态机，使游戏的AI更具有智能状态。增加了碰撞检测、子弹的检测、友机检测，为玩家提供更真实的场景。在操作方面，只需要六个按键即可完成，简单易学。----From Tankwar Team.",200,70,TextArea.SCROLLBARS_NONE );
	
	public About(){
		this.setLayout(new FlowLayout(FlowLayout.CENTER, 50, 100));
		this.setSize(500, 400);
		this.setTitle("TankWar");
		text.setFont(new Font("楷体",Font.ITALIC,50));
		text.setSize(500, 50);
		textArea.setEditable(false);
		textArea.setSize(400, 300);
		textArea.setBounds(10, 10, 480, 380);
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
