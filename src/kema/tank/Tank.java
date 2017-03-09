package kema.tank;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.util.*;

import www.kema.Astar.Astar;
import www.kema.GameAI.GameAI;
import www.kema.TankState.TankState;
/**
 * ̹����
 * @author �Դ�
 *
 */
public class Tank {
	//̹�˵�ǰ���ڵ�λ��
	private int x,y;
	
	public static final int FINDHOMESTATE = 1;  //���ϼ�
	public static final int FINDEDHOME= 2;    //�ҵ��ϼ�
	public static final int FINDMYTANK = 3;	//�����ҷ�̹�ˣ�����ǹ
	public static final int AVOIDMISSILE1 = 4; // �����߅�����ұ߁���ӵ�
	public static final int AVOIDMISSILE2 = 5; // ������ϻ���������ӵ�
	public static final int AVOIDMISSILE3 = 6; // �����������±ߵ��ӵ�
	public static final int AVOIDMISSILE4 = 7; // ����������µ��ӵ�
	
	
	
/*	//�ϼ����ڵ�
	private static int ENDX = 119;
	private static int ENDY = 80;*/
	
	//̹�˵��ٶ�
	public static final int XSPEED = 5;
	public static final int YSPEED = 5;
	
	//̹�˵Ĵ�С
	public static final int WIDTH = 30;
	public static final int HEIGHT = 30;
	//�Ƿ����
	private boolean live = true;
	
	TankClient tc;
	//Ѫ����
	bloodBar bb = new bloodBar();

	//�ж��Ƿ�Ϊ�ҷ�̹�ˣ������ǣ���good == true;
	private boolean good;
	//̹����һʱ�����ڵ�λ��
	private int oldX, oldY;
	//����ֵ
	private int life = 100;
	
	private static Map<String,Image> imgs = new HashMap<String ,Image>();
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// ̹��
	private static Image[] tankImgs = null;
	
	private List<Integer> list = new ArrayList<Integer>();
	
	TankState tankState ;
	
	private GameAI gameAI  ;
	/**
	 * �����˸������̹�ˣ��������Ҽ���Map��ֵ��
	 */
	static {
		tankImgs= new Image[]{
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankL.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRU.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankR.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankRD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankD.gif")),
				tk.getImage(Tank.class.getClassLoader().getResource("images/tankLD.gif"))
			};
		
		imgs.put("L",tankImgs[0]);
		imgs.put("LU",tankImgs[1]);
		imgs.put("U",tankImgs[2]);
		imgs.put("RU",tankImgs[3]);
		imgs.put("R",tankImgs[4]);
		imgs.put("RD",tankImgs[5]);
		imgs.put("D",tankImgs[6]);
		imgs.put("LD",tankImgs[7]);
		
		
		
	}
	
	//�����,,��ʾ�÷������ߵĲ���
	private static Random r =new Random();
	//�ĸ���λ
	private boolean bL = false ,bU = false, bR =false,bD = false;
	
	//׷��̹��ʱ�ķ��򼯺�
	private List<Integer> closeDir = new ArrayList<Integer>();
	//�ж��Ƿ���׷�������
	private boolean isChase = false;
	
	//׷��Ĳ���:
	private int chaseStep = 0;
	
	//�����ϼң������ǵķ��򼯺�
	private List<Integer> findHomeDir = new ArrayList<Integer>();
	//�Ƿ��ѷ���
	private boolean isfind = false;
	
	//���ϼҵĲ���
	private int findHomeStep =0;
		
	
	//���ó�ʼ״̬��stop
	private Direction dir = Direction.STOP;
	
	private Direction ptdir = Direction.U;
	
	//������Ĳ���
	private int step = 0;
	
	private int path = r.nextInt(12)+3;
	
	public int pathState = r.nextInt(12)+3;
	
	private int pathLenth = 0;
	//���캯��
	public Tank(int x, int y,boolean good) {
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
		
		 gameAI = new GameAI(x,y);
		 list = gameAI.getAns();
		 this.pathLenth = list.size();
		/* System.out.println("list_size:"+pathLenth);*/
		 /*for(int lis : list){
			 System.out.print(lis);
		 }
		 System.out.println();*/
	}
	
	/*public void init(){
		// list = GameAI.GA();
		
	}*/
	//getAndSet ����
		public int getLife() {
			return life;
		}

		public void setLife(int life) {
			this.life = life;
		}

		public boolean isGood() {
			return good;
		}
	//ֹͣ״̬
	public void stay(){
		x = oldX;
		y = oldY;
	}
	//���캯��
	public Tank(int x,int y,boolean good,Direction dir,TankClient tc){
		//��������Ĺ��캯��
		 this(x,y,good);
		 this.dir = dir;
		 this.tc=tc;
	}
	//��tank
	public void draw(Graphics g){
		//���̹�����ˡ���������
		if(!live){
			if(!good){
				tc.tanks.remove(this);
			}
			return;
		}
/*		//�õ���ɫ
		Color c = g.getColor();
		//������ҷ�̹����ɫΪ��ɫ,����Ϊ��ɫ
		if(good){
			g.setColor(Color.RED);
			}
		else{
			g.setColor(Color.YELLOW);
		}
		//����̹��
		g.fillOval(x, y, WIDTH,HEIGHT);
		//�ٰ���ɫ����ȥ
		g.setColor(c);*/
		//��̹�˻�Ѫ��
		
			bb.draw(g);
		
		//�ж�ʲô�����Լ�����
			switch(ptdir){
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
		move();
	}
	//�ƶ��ľ������
	void move(){
		
		this.oldX = x;
		this.oldY = y;
		
		switch(dir){
		case L:
			x -=XSPEED;
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
		case STOP:
			break;
		}
		if(this.dir != Direction.STOP){
			this.ptdir = this.dir;
		}
		//�߽紦��
		if(x < 0) x = 0;
		if(y < 20) y = 20;
		//�߽紦��
		if(x + Tank.WIDTH > TankClient.GAME_WIDTH) x = TankClient.GAME_WIDTH - Tank.WIDTH;
		if(y + Tank.HEIGHT > TankClient.GAME_HEIGHT) y = TankClient.GAME_HEIGHT - Tank.WIDTH;
		//�з�̹��
		if(!good){
			Direction[] dirs = Direction.values();
			
			tankState = new TankState(FINDHOMESTATE,this,tc);
			
			
			int currenState = tankState.update();
			/*System.out.println("current state =" +currenState);*/
			switch(currenState){
				
				case FINDHOMESTATE:		//��step == 0 ��ʱ�򣬣�˵��AI�����Ѿ������ˣ������������������һ������
				/*	System.out.println("finding home");*/
					/*if(pathLenth -1 == step){
						this.fire();
						step = r.nextInt(12) + 3;
						//��������Ǹ������ƶ�
						int rn = r.nextInt(dirs.length);
						dir = dirs[rn];
						
						step --;
					}
					else{*/
					//���������ˣ�����������
					if(pathLenth -1 == step){
						
						step = 0;
					}
						if(0 == path){
							this.fire();
							path = r.nextInt(12)+3;
							dir = dirs[list.get(step)];
							step++;
						}
						
						path -- ;
					
					
					break;
				case FINDEDHOME:
					System.out.println("fire your home");
					//��ȡ��ǰ̹�˵�������
					int currentX = y / XSPEED;
					int currentY = x / YSPEED;
					/*//̹�����ϼҵ����
					if(currentY <(tc.getGameWidth()-50) / YSPEED){
						System.out.println("in left,turn right");
						if(this.collidesWithTanks(tc.tanks)){
							break;
						}
						//��wall2���棬��˵���ӽ��ϼ�
						if(currentY > tc.getWallTwo().getY() / YSPEED){
							this.setDir(Direction.R);
							x += XSPEED;
						}else if(currentX < (tc.getWallTwo().getX() - 5)/YSPEED){
							this.setDir(Direction.RD);
							x +=XSPEED;
							y +=YSPEED;
						}else{
							this.setDir(Direction.L);
							x -=XSPEED;
						}
						
						if(pathState == 0){
							pathState = r.nextInt(12)+3;
						}
						pathState --;
						if(r.nextInt(40) > 30){
							this.fire(Direction.RD);
						}
					
						break;
					}
					//̹�����ϼ��ұ�
					if(currentY > (tc.getGameWidth()-50) / YSPEED){
						
						System.out.println("in right,turn left");
						if(this.collidesWithTanks(tc.tanks));
						
						if(currentY > tc.getWallTwo().getY() / YSPEED){
							this.setDir(Direction.L);
							x -= XSPEED;
						}else if(currentX > (tc.getWallTwo().getX() + tc.getWallTwo().getWidth() + 5)/YSPEED){
							this.setDir(Direction.LD);
							x -=XSPEED;
							y +=YSPEED;
						}else{
							this.setDir(Direction.R);
							x+=XSPEED;
						}
						
						this.setDir(Direction.LD);
						if(pathState == 0){
							pathState = r.nextInt(12)+3;
						}
						pathState --;
						if(r.nextInt(40) > 30){
							this.fire(Direction.LD);
						}
						
						break;
					}*/
					if(collidesWall(tc.getWallTwo())){
						System.out.println("ײǽ��");
						break;
					}
					else{
					if(!isfind || findHomeDir.size() ==0){
						fireMyhome();
						isfind = true;
						findHomeStep = 0;
					}
					if(findHomeDir.size() >0){
						if(findHomeStep >= findHomeDir.size()){
							break;
						}
						int findDir = findHomeDir.get(findHomeStep);
						
						this.dir = dirs[findDir];
						
						findHomeStep ++;
					}
					int chaseX = r.nextInt(40);
					if(chaseX > 35){
						this.fire();
					}
					if(chaseStep >= findHomeDir.size()){
						isfind = false;
						findHomeStep = 0;
					}
					break;
					}
				
				case FINDMYTANK:
					System.out.println("chase myTank");
					if(!isChase || closeDir.size() == 0){
						chaseMyTank();
						isChase = true;
						chaseStep = 0;
						 
					}
					if(closeDir.size() > 0){
						int chaseDir = closeDir.get(chaseStep);
						
						this.dir = dirs[chaseDir];
						
						chaseStep++;
					}
					int chaseX = r.nextInt(40);
					if(chaseX > 35){
						this.fire();
					}
					if(chaseStep >= closeDir.size()){
						isChase = false;
						chaseStep = 0;
					}
					break;
				case AVOIDMISSILE1:
					System.out.println("�����߻�����߅�����ӵ�");
					this.setDir(Direction.U);
					if(this.collidesWall(tc.wallOne) || this.collidesWall(tc.wallTwo) 
							|| this.x < 10 || this.y < 10 ||x + Tank.WIDTH >= TankClient.GAME_WIDTH-10
							|| y + Tank.HEIGHT >= TankClient.GAME_HEIGHT -10){
						System.out.println("change dir");
						this.setDir(Direction.L);
					}
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					y -=YSPEED;
					break;
				case AVOIDMISSILE2:
					System.out.println("������ϻ���������ӵ�");
					this.setDir(Direction.LD);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					y -=YSPEED;
					break;
				case AVOIDMISSILE3:
					System.out.println("�����������±ߵ��ӵ�");
					this.setDir(Direction.L);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					x -=XSPEED;
					break;
				case AVOIDMISSILE4:
					System.out.println("����������µ��ӵ�");
					this.setDir(Direction.RD);
					if(pathState == 0){
						pathState = r.nextInt(12)+3;
					}
					pathState --;
					x +=XSPEED;
					break;
				default:
					break;
			}
		/*	//��step == pathLenth ��ʱ�򣬣�˵����AI�����Ѿ������ˣ������������������һ������
			if(pathLenth -1 == step){
				this.fire();
				step = r.nextInt(12) + 3;
				//��������Ǹ������ƶ�
				int rn = r.nextInt(dirs.length);
				dir = dirs[rn];
				
				step --;
			}
			else{
				
				if(0 == path){
					this.fire();
					path = r.nextInt(12)+3;
					dir = dirs[list.get(step)];
					step++;
				}
				
				path -- ;
			}
			*/
			
			//�����������40���ڣ�����38�������ӵ�
			if(r.nextInt(40) >38){
				this.fire();
			}	
		}
	}
	/**
	 * �ҵ��ϼҺ�ִ�е�ASTAR�㷨
	 */
	private void fireMyhome() {
		int myHomeX = tc.getGameHeight() / XSPEED;
		int myHomeY = (tc.getGameWidth()/2) / XSPEED;
		Astar astar = new Astar(y/XSPEED, x/YSPEED, myHomeX, myHomeY, tc);
		astar.init();
		
		findHomeDir = astar.getCloseDir();
	}
	/**
	 * �����ҵ�̹�˺󣬣�ִ�е�ASTAR�㷨
	 */
	private void chaseMyTank() {
		int mytankX = tc.myTank.getY() / XSPEED;
		int mytankY = tc.myTank.getX() / YSPEED;
		
		Astar astar = new Astar(y/XSPEED, x/YSPEED, mytankX, mytankY, tc);
		/*Astar astar = new Astar(5, 60, 5, 50, tc);*/
		astar.init();
		
		closeDir = astar.getCloseDir();
		System.out.println("closeDir size in chaseMyTank:"+closeDir.size());
	}

	//���̵Ĳ���
	public void KyePressed(KeyEvent e){
		int key = e.getKeyCode();
			switch(key){
			//����F2������ҷ�̹�˹��ˣ�������
			case KeyEvent.VK_F2:
				if(!this.live){
					this.live = true;
					this.life =100;
				}
				break;
			//̹�˵��ƶ�
			case KeyEvent.VK_LEFT:
			 bL = true;
				break;
			case KeyEvent.VK_UP:
				bU = true;
				break;
			case KeyEvent.VK_RIGHT:
				bR = true;
				break;
			case KeyEvent.VK_DOWN:
				bD = true;
				break; 
		}
		locateDirection();
	}
	//ִ�еĲ���
	void locateDirection(){
		if(bL && !bU && !bR &&!bD) dir= Direction.L;
		else if(bL && bU && !bR &&!bD) dir= Direction.LU;
		else if(!bL && bU && !bR &&!bD) dir= Direction.U;
		else if(!bL && bU && bR &&!bD) dir= Direction.RU;
		else if(!bL && !bU && bR &&!bD) dir= Direction.R;
		else if(!bL && !bU && bR &&bD) dir= Direction.RD;
		else if(!bL && !bU && !bR &&bD) dir= Direction.D;
		else if(bL && !bU && !bR &&bD) dir= Direction.LD;
		else if(!bL && !bU && !bR &&!bD) dir= Direction.STOP;
	}
	//�����ͷŵĲ���,�����
	public void KeyReleased(KeyEvent e) {
		int key = e.getKeyCode();
		switch(key){
		//���ӵ�
		case KeyEvent.VK_X:
			fire();
			break;
		case KeyEvent.VK_LEFT:
		 bL = false;
			break;
		case KeyEvent.VK_UP:
			bU = false;
			break;
		case KeyEvent.VK_RIGHT:
			bR = false;
			break;
		case KeyEvent.VK_DOWN:
			bD = false;
			break; 
		//��ң����˸�������ӵ�
		case KeyEvent.VK_A:
			superFire();
			break;
	}
	locateDirection();
	}
	/**
	 * ���ӵ�
	 * @return
	 */
	public Missile fire(){
		//����Ѿ����ˣ����Ͳ��ܷ��ӵ�
		if(!live)
			return null;
		int x = this.x + Tank.WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + Tank.HEIGHT/2 -Missile.HEIGHT/2;
		Missile m =new Missile(x,y,good,ptdir,this.tc);
		PlaySound PlaySound = new PlaySound("fire.wav");
		PlaySound.play();
		tc.missiles.add(m);
		return m;
	}
	public Missile fire(Direction dir){
		if(!live)
			return null;
		int x = this.x + WIDTH/2 - Missile.WIDTH/2;
		int y = this.y + HEIGHT/2 -Missile.HEIGHT/2;
		Missile m =new Missile(x,y,good,dir,this.tc);
		PlaySound PlaySound = new PlaySound("fire.wav");
		PlaySound.play();
		tc.missiles.add(m);
		return m;
	}
	//�������
	public void superFire(){
		Direction [] dir = Direction.values(); 
		for(int i = 0 ; i < 8 ;i++){
			fire(dir[i]);
		}
	}
	
	public Rectangle getRect(){
		return new Rectangle(x,y,WIDTH,HEIGHT);
	}
	public boolean isLive() {
		return live;
	}
	public void setLive(boolean live) {
		this.live = live;
	}
	//ײǽ��
	public boolean collidesWall(Wall w){
		//��ײ���
		if(this.live && this.getRect().intersects(w.getRect()) && w.isLive()){
			this.stay();
			return true;
		}
		return false;
	}
	//̹�˼�ײ����
	public boolean collidesWithTanks(java.util.List<Tank> tanks){
		for(int i = 0; i < tanks.size(); i++){
			Tank t = tanks.get(i);
			//������̹��
			if(this != t){
				//��ײ���
				if(this.live && t.isLive() && this.getRect().intersects(t.getRect())){
					this.stay();
					t.stay();
					return true;
				}
			}
		}
		return false;
	}
	
	//��Myhomeײ����
	public boolean collidesWithMyHome(MyHome myHome){
			//��ײ���
			if(this.live && myHome.isLive() && this.getRect().intersects(myHome.getRect())){
				this.stay();
				return true;
			}
			return false;
		}
	//Ѫ��
	private class bloodBar {
		public void draw(Graphics g){
			Color c = g.getColor();
			//���ҷ�̹��Ѫ�����ɫ��Ϊ��ɫ
			if(good){
				g.setColor(Color.RED);
				g.drawRect(x, y-10,WIDTH, 10);
				//���ӻ�
				int w = WIDTH * life / 100 ;
				g.fillRect(x, y - 10, w, 10);
				g.setColor(c);
			}else{
				g.setColor(Color.MAGENTA);
				g.drawRect(x, y-10,WIDTH, 10);
				//���ӻ�
				int w = WIDTH * life / 100 ;
				g.fillRect(x, y - 10, w, 10);
				g.setColor(c);
			}
		}
	}
	//��Ѫ��
	public boolean eat(Blood b){
		//����ҷ�̹�˻����ţ�����Ѫ�黹�ڣ�����ײ����
		if(this.live && b.isLive() && this.getRect().intersects(b.getRect())){
			//��Ѫ����
			this.life = 100 ;
			//�����Ժ󣬣���Ѫ����ʧ��
			b.setLive(false);
			return true;
		}
		return false;
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

	public void setDir(Direction dir) {
		this.dir = dir;
	}

	public GameAI getGameAI() {
		return gameAI;
	}

	
	
}

