package com.zhangzg.MenuComponent;

import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import kema.tank.TankClient;

/**
 * ������˵�ѡ��
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
		//��ʼ�������˵�ģ��
		this.mb      = new MenuBar();
		this.game    = new Menu("��Ϸ");
		this.help    = new Menu("����");
		this.set     = new Menu("����");
		this.start   = new MenuItem("��ʼ��Ϸ");
		this.exit    = new MenuItem("�˳�");
		this.setting = new MenuItem("��Ϸ����");
		this.way     = new MenuItem("�淨");
		this.about   = new MenuItem("��������");
		this.tk      = tk;
		
		//���˵�����ӵ�menubar��
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
		case "��ʼ��Ϸ":
			tk.dispose();
			TankClient.setStart(true);
			TankClient.setLoding(true);
			new TankClient().lauchFrame();
			break;

		case "�˳�":
			System.exit(0);
			break;
		
		case "��Ϸ����":
			new Setting();
			break;
			
		case "�淨":
			new Method();
			break;
			
		case "��������":
			new About();
			break;
		
		}
		
	}
}
