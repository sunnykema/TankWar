package kema.tank;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;

public class MyHome {
	private static int x ;
	private static int y;
	private TankClient tc;
	
	private static boolean isLive = true;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// 老家
	private static Image myHome = tk.getImage(MyHome.class.getClassLoader().getResource("images/symbol.gif"));
	
	private boolean init = false;
	
	public MyHome(int x ,int y ,TankClient tc){
		this.x =x ;
		this.y = y;
		this.tc = tc;
	}
	/**
	 * 画出老家
	 * @param g
	 */
	public void draw(Graphics g){
		//还没初始化，，给初始化
		if(!init){
			init = true;
			g.drawImage(myHome, -100, -100, null);
		}
		if(!isLive){
			System.out.println("You Lost!");
			
			return ;
		}
		
		g.drawImage(myHome, x, y-50, null);
	}
	
		public Rectangle getRect() {
			return new Rectangle(x,y-50,myHome.getWidth(null),myHome.getHeight(null));
		}

		public static boolean isLive() {
			return isLive;
		}

		public static void setLive(boolean isLive) {
			MyHome.isLive = isLive;
		}
		//键盘的操作
		public void KyePressed(KeyEvent e){
			int key = e.getKeyCode();
				switch(key){
				//按下F2，如果老家，，复活
				case KeyEvent.VK_F3:
					if(!this.isLive){
						/*System.out.println("!!!!!!!!!!!");*/
						this.isLive = true;
						tc.setOver(false);
						
						tc.getWallFive().setLive(true);
						tc.getWallFour().setLive(true);
						tc.getWallThree().setLive(true);
					}
					break;
				
			}
			
		}
	
}
