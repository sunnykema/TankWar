package kema.tank;
import java.awt.*;
import java.util.Random;
/**
 * Ѫ��
 * @author �Դ�
 *
 */
public class Blood {
	//Ѫ���λ�ã�����ȣ��߶�
	int x, y, w, h;
	TankClient tc;
	//Ѫ������� ��ʼ��Ϊtrue
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
	//Ѫ�������ĵ�
/*	int [][] pos = {
			{300,300}, {350,250}, {400,300} ,{350,350} ,{300,300},{250,250},{200,300},{350,250}
				};*/
	//���캯��
	public Blood(TankClient tc){
		this.tc = tc;
		x = rand.nextInt(tc.getGameWidth() - 100);
		y = rand.nextInt(tc.getGameHeight() - 100);
		w = 10;
		h = 10;
	}
	//����Ѫ��
	public void draw(Graphics g){
		
		//Ѫ�������ֵΪfalse��
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
		//����Ѫ�����ɫ
		g.setColor(Color.MAGENTA);
		//����Ѫ��
		g.fillOval(x,y,w,h);
		g.setColor(c);*/
		
		g.drawImage(bloodImage, x, y, null);
		//�ƶ�Ѫ��
	//	move();
	}
/*	//Ѫ����ƶ�
	private void move() {
		step ++;
		//�ظ��ƶ�
		if(step >= pos.length){
			step = 0;
		}
		x = pos[step][0];
		y = pos[step][1];
	}*/
	//����Ѫ��
	public Rectangle getRect() {
		return  new Rectangle(x, y, bloodImage.getWidth(null), bloodImage.getHeight(null)); 
	}
	
}
