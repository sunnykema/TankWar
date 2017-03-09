package com.zhangzg.MenuComponent;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kema.tank.TankClient;

/**
 * 抽象出菜单选项
 * @author zhangzg
 *
 */
public class MenuBarList implements ActionListener {

	private MenuBar  mb;
	private Menu game;
	private Menu set;
	private Menu help;
	private MenuItem start;
	private MenuItem exit;
	private MenuItem setting;
	private MenuItem way;
	private MenuItem about;
	private TankClient tk;
	
	
	
	public MenuBarList(TankClient tk) {
		//初始化各个菜单模块
		this.mb      = new MenuBar();
		this.game    = new Menu("游戏");
		this.help    = new Menu("帮助");
		this.set     = new Menu("设置");
		this.start   = new MenuItem("开始游戏");
		this.exit    = new MenuItem("退出");
		this.setting = new MenuItem("游戏设置");
		this.way     = new MenuItem("玩法");
		this.about   = new MenuItem("关于我们");
		this.tk      = tk;
		
		//将菜单项添加到menubar中
		game.add(start);
		game.add(exit);
		set.add(setting);
		help.add(way);
		help.add(about);
		mb.add(game);
		mb.add(set);
		mb.add(help);
		
		start.addActionListener(this);
		exit.addActionListener(this);
		setting.addActionListener(this);
		way.addActionListener(this);
		about.addActionListener(this);
	}

	public MenuBar getMb() {
		return mb;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "开始游戏":
			tk.dispose();
			TankClient.setStart(true);
			TankClient.setLoding(true);
			new TankClient().lauchFrame();
			break;

		case "退出":
			System.exit(0);
			break;
		
		case "游戏设置":
			new Setting();
			break;
			
		case "玩法":
			new Method();
			break;
			
		case "关于我们":
			new About();
			break;
		
		}
		
	}
}
