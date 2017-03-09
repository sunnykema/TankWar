package kema.tank;
import java.awt.*;
/**
 * 爆炸
 * @author 赵聪
 *
 */
public class Explode {
	//爆炸的位置
	int x,y;
	//爆炸的生命，初始值为true
	private boolean live = true;
	private TankClient tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// 爆炸的画面
	private static Image[] imgs={
		tk.getImage(Explode.class.getClassLoader().getResource("images/0.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/1.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/2.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/3.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/4.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/5.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/6.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/7.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/8.gif")),
		tk.getImage(Explode.class.getClassLoader().getResource("images/9.gif"))
	};
	int step = 0;
	//判断爆炸是否初始化。。。
	private boolean init = false;
	/**
	 * 构造函数
	 * @param x
	 * @param y
	 * @param tc
	 */
	public Explode(int x, int y, TankClient tc) {
		this.x = x;
		this.y = y;
		this.tc = tc;
	}
	/**
	 * 画出爆炸画面
	 * @param g
	 */
	public void draw(Graphics g){
		//还没初始化的时候，，先把imgs创建出来，不然第一个爆炸效果不会产生
		if(!init){
			init = true;
			for (int j = 0; j < imgs.length; j++) {
				g.drawImage(imgs[j], -100, -100, null);
			}
		}
		//如果死了,,去掉
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		// 爆炸播放完了
		if(step == imgs.length){
			live = false;
			step = 0;
			return ;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
}
