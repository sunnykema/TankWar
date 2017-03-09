package kema.tank;
import java.awt.*;
import java.util.Random;
/**
 * 血块
 * @author 赵聪
 *
 */
public class Blood {
	//血块的位置，，宽度，高度
	int x, y, w, h;
	TankClient tc;
	//血块的生命 初始化为true
	private boolean live = true;
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	
	private static  Image bloodImage = tk.getImage(Wall.class.getClassLoader().getResource("images/star.gif"));
	
	private int step = 0;
	private Random rand = new Random();
	//血块跳动的点
/*	int [][] pos = {
			{300,300}, {350,250}, {400,300} ,{350,350} ,{300,300},{250,250},{200,300},{350,250}
				};*/
	//构造函数
	public Blood(TankClient tc){
		this.tc = tc;
		x = rand.nextInt(tc.getGameWidth() - 100);
		y = rand.nextInt(tc.getGameHeight() - 100);
		w = 10;
		h = 10;
	}
	//画出血块
	public void draw(Graphics g){
		
		//血块的生命值为false，
		if(!live){
			if(rand.nextInt(400) > 398){
				live = true;
				x = rand.nextInt(tc.getGameWidth() -100);
				y = rand.nextInt(tc.getGameHeight() - 100);
			}
			else{
				return ;
			}
		}
	/*	Color c =g.getColor();
		//设置血块的颜色
		g.setColor(Color.MAGENTA);
		//画出血块
		g.fillOval(x,y,w,h);
		g.setColor(c);*/
		
		g.drawImage(bloodImage, x, y, null);
		//移动血块
	//	move();
	}
/*	//血块的移动
	private void move() {
		step ++;
		//重复移动
		if(step >= pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}*/
	//返回血块
	public Rectangle getRect() {
		return  new Rectangle(x, y, bloodImage.getWidth(null), bloodImage.getHeight(null)); 
	}
	
}
