package kema.tank;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * �ӵ�
 * @author �Դ�
 *
 */
public class Missile {
	//�ӵ����ٶ�
	public static final int XSPEED = 10;
	public static final int YSPEED = 10;
	
	//�ӵ��Ĵ�С
	public static final int WIDTH = 10;
	public static final int HEIGHT = 10;
	
	//λ��
	int x, y;
private static Map<String,Image> imgs = new HashMap<String ,Image>();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// ̹��
	private static Image[] missileImgs = null;
	
	private static Image gameOver = null;
	/**
	 * �����˸�������ӵ����������Ҽ���Map��ֵ��
	 */
	static {
		missileImgs= new Image[]{
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileL.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileR.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileRD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileU.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/missileLD.gif")),
				tk.getImage(Missile.class.getClassLoader().getResource("images/over.gif"))
			};
		//�ӵ�����ͼƬP����
		imgs.put("L",missileImgs[0]);
		imgs.put("LU",missileImgs[1]);
		imgs.put("U",missileImgs[2]);
		imgs.put("RU",missileImgs[3]);
		imgs.put("R",missileImgs[4]);
		imgs.put("RD",missileImgs[5]);
		imgs.put("D",missileImgs[6]);
		imgs.put("LD",missileImgs[7]);
	}
	
	//����
	Direction dir;
	
	//�Ƿ�Ϊ�ҷ�̹��
	private boolean good;
	//�ӵ�������,��ʼֵΪtrue
	private boolean live = true;
	//
	private TankClient tc;
	
	public boolean isLive() {
		return live;
	}
	//���캯��
	public Missile(int x, int y, Direction dir) {
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	public Missile(int x, int y,boolean good, Direction dir,TankClient tc) {
		this(x,y,dir);
		this.good = good;
		this.tc = tc;
		
	}
	//���ӵ�
	public void draw(Graphics g){
		//�ж��Ƕ����ߣ����ӵ����ˣ�ȥ����
		if(!live){
			tc.missiles.remove(this);
			return;
		}
		//�����ӵ�
		switch(dir) {
		case L:
			g.drawImage(imgs.get("L"), x, y, null);
			break;
		case LU:
			g.drawImage(imgs.get("LU"), x, y, null);
			break;
		case U:
			g.drawImage(imgs.get("U"), x, y, null);
			break;
		case RU:
			g.drawImage(imgs.get("RU"), x, y, null);
			break;
		case R:
			g.drawImage(imgs.get("R"), x, y, null);
			break;
		case RD:
			g.drawImage(imgs.get("RD"), x, y, null);
			break;
		case D:
			g.drawImage(imgs.get("D"), x, y, null);
			break;
		case LD:
			g.drawImage(imgs.get("LD"), x, y, null);
			break;
		}
		//�ƶ��ӵ�
		move();
	}
	//���ӵ���
  	private void move(){
		//�ж��ĸ����򣬣�Ȼ���������ƶ�
		switch(dir){
		case L:
			x -= XSPEED;
			break;
		case LU:
			x -=XSPEED;
			y -=YSPEED;
			break;
		case U:
			y -=YSPEED;
			break;
		case RU:
			x +=XSPEED;
			y -=YSPEED;
			break;
		case R:
			x +=XSPEED;
			break;
		case RD:
			x +=XSPEED;
			y +=YSPEED;
			break;
		case D:
			y +=YSPEED;
			break;
		case LD:
			x -=XSPEED;
			y +=YSPEED; 
			break;
		}
		//�ӵ��ɳ����壬���Ͱ��ӵ�������ֵ��Ϊfalse
		if(x < 0|| y < 0||x > TankClient.GAME_WIDTH||y>TankClient.GAME_HEIGHT){
			live = false;
		}
	}
	//�����ӵ�
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	//�������̹�˵��¼�
	public boolean hitTank(Tank t){
		//��ײ���   ���Ҳ����Լ���
		if(this.live && this.getRect().intersects(t.getRect()) && t.isLive() &&this.good != t.isGood()){
			PlaySound playSound = new PlaySound("blast.wav");
			playSound.play();
			//���е�̹��,������ֵ�������ԣ����Կ����������ˣ�������ֵ����20,,
			
				t.setLife(t.getLife() - 20);
				if(t.getLife() <= 0){
					t.setLive(false);
				}
			
			//ͬʱ�ӵ�������Ҳû��
			this.live = false;
			//���ű�ը����
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
			
		return false;
	}
	//�Ƿ����̹�� ���з���true��
	public boolean hitTanks(List<Tank> tanks){
		for(int i=0;i < tanks.size();i++){
			if(hitTank(tanks.get(i))){
				return true;
			}
		}
		return false;
	}
	//�ж��Ƿ�ײǽ��ײǽ�Ż�true
	public boolean hitWall (Wall w){
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			PlaySound playSound = new PlaySound("hit.wav");
			playSound.play();
			this.live =false;
			//����ǽ������ʹ����ʧ
			if(w.isTu() && !this.good){
				w.setLive(false);
			}
			
			return true;
		}
		return false;
	}
	/**
	 * �ж��Ƿ����myhome
	 * @param mh
	 * @return
	 */
	public boolean hitMyHome(MyHome mh){
		if(this.live &&  mh.isLive() && this.getRect().intersects(mh.getRect()) && !good){
			MyHome.setLive(false);
			this.live = false;
			PlaySound playSound = new PlaySound("blast.wav");
			playSound.play();
			//���ű�ը����
			Explode e = new Explode(x, y, tc);
			tc.explodes.add(e);
			return true;
		}
		
		return false;
	}
	public boolean isGood() {
		return good;
	}
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	public Direction getDir() {
		return dir;
	}
	
	
}

