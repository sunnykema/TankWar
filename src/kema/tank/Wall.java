package kema.tank;
import java.awt.*;
/**
 * 墙
 * @author 赵聪
 *
 */
public class Wall {
	//起始点的位置
	private int x, y;
	//请的长度，宽度
	private int height, width;
	
	private boolean isLive =true;
	TankClient tc;
	
	private boolean isTu ;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static  Image wallImage =	tk.getImage(Wall.class.getClassLoader().getResource("images/steel.gif"));
	//构造函数
	public Wall(int x, int y) {
		this.x = x;
		this.y = y;
	}
	public Wall(int x, int y, int height, int width,boolean isTu, TankClient tc) {
		this(x,y);
		this.height = height;
		this.width = width;
		this.isTu = isTu;
		this.tc = tc;
	}
	//画出墙
	//画出墙
		public void draw(Graphics g){
			Toolkit img = Toolkit.getDefaultToolkit();
			Image image = img.getImage(Wall.class.getClassLoader().getResource("images/tugai.net.20101117134209.gif"));
			for(int row = x;row < x + width;){
				for(int col = y;col < y + height;col = col + 30){
					g.drawImage(image, row, col, 30, 30, null);
				}
				row = row + 30;
			}
		}
		
		public void drawNR(Graphics g){
			Toolkit img = Toolkit.getDefaultToolkit();
			Image image = img.getImage(Wall.class.getClassLoader().getResource("images/wall.gif"));
			if(!isLive){
				return ;
			}
			for(int row = x;row < x + width;){
				for(int col = y;col < y + height;col = col + 30){
					g.drawImage(image, row, col, 30, 30, null);
				}
				row = row + 30;
			}
		}
	//得到墙
	public Rectangle getRect(){
		return new Rectangle(x, y, width,height);
	}
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getHeight() {
		return height;
	}
	
	public int getWidth() {
		return width;
	}
	public boolean isTu() {
		return isTu;
	}
	public void setTu(boolean isTu) {
		this.isTu = isTu;
	}
	public boolean isLive() {
		return isLive;
	}
	public void setLive(boolean isLive) {
		this.isLive = isLive;
	}

	
	
}
