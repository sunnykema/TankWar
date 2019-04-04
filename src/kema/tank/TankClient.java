package kema.tank;
import java.awt.*;
import java.awt.*;
import java.util.List;
import java.util.ArrayList;
import java.awt.event.*;

import javax.xml.stream.events.StartDocument;

import com.zhangzg.MenuComponent.MenuBarList;

import www.kema.GameAI.GameAI;
/**
 * ̹����Ϸ������
 * @author kema
 *
 */
public class TankClient extends Frame{
	//���崰�ڴ�С
	public static final int GAME_WIDTH = 800;
	public static final int GAME_HEIGHT = 600;
	
	//��Ϸ�ոտ�ʼ���������õ�
	private static boolean isStart = false;
	
	//������Ϸ�Ŀ�����
	private static boolean isLoding = false;
	
	//������Ϸ�Ŀ�����
	private static boolean isLodingF = false;
	
	//�ط�̹�˵Ĵ���
	private static int generation =0;
	
	//��Ϸ�Ƿ������
	private static boolean isOver = false;

	// �ϼ�
	MyHome myHome = new MyHome(GAME_WIDTH/2,GAME_HEIGHT,this);
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	//��Ϸ������ͼ
	private static Image gameOver = tk.getImage(Tank.class.getClassLoader().getResource("images/over.gif"));
	
	//�ҷ�̹��
	Tank myTank = new Tank(GAME_WIDTH / 2 -100 ,GAME_HEIGHT - 50,true,Direction.STOP,this);
	
	//Ѫ��
	Blood blood = new  Blood(this);
	
	//��ʱ��
	int times = 0;
	
	public Tank getMyTank() {
		return myTank;
	}
	
	public List<Missile> getMissiles() {
		return missiles;
	}


	//ǽ
	Wall wallOne = new Wall(100,150,200,20,false,this);
	Wall wallTwo = new Wall(300,450,20,300,false,this);
	Wall wallThree = new Wall(375,550,50,20,true,this);
		
	Wall wallFour = new Wall(375,520,20,91,true,this);
		
	Wall wallFive = new Wall(465,550,50,20,true,this);

	//��ը
	List<Explode> explodes = new ArrayList<Explode>();
	//�ӵ�
	List<Missile> missiles = new ArrayList<Missile>();
	//�з�̹��
	List<Tank> tanks = new ArrayList<Tank>();
	
	
	//�ӵ�
	Missile m = null;
	//��ǰ��Ļ
	Image offScreenImage = null;
	
	
	
	public static int getGameWidth() {
		return GAME_WIDTH;
	}

	public static int getGameHeight() {
		return GAME_HEIGHT;
	}

		
	public Wall getWallOne() {
		return wallOne;
	}



	public Wall getWallTwo() {
		return wallTwo;
	}

	public static boolean isStart() {
		return isStart;
	}

	public static void setStart(boolean isStart) {
		TankClient.isStart = isStart;
	}

	public static boolean isLoding() {
		return isLoding;
	}

	public static void setLoding(boolean isLoding) {
		TankClient.isLoding = isLoding;
	}
	
	public Wall getWallThree() {
		return wallThree;
	}

	public void setWallThree(Wall wallThree) {
		this.wallThree = wallThree;
	}

	public Wall getWallFour() {
		return wallFour;
	}

	public void setWallFour(Wall wallFour) {
		this.wallFour = wallFour;
	}

	public Wall getWallFive() {
		return wallFive;
	}

	public void setWallFive(Wall wallFive) {
		this.wallFive = wallFive;
	}

	public void setWallOne(Wall wallOne) {
		this.wallOne = wallOne;
	}

	public void setWallTwo(Wall wallTwo) {
		this.wallTwo = wallTwo;
	}

	/**
	 * ����������  ����windows�е�paint
	 */
		public void paint(Graphics g){
			if(!isLodingF){
				if(isLoding){
					//AePlayer aePlayer = new AePlayer("start.wav");
					if(isStart){
					PlaySound playSound = new PlaySound("start.wav");
					playSound.play();
					isStart = false;
					}
					while(times < 50)
					{
						g.setColor(Color.BLACK);
						g.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
						//��ʾ��Ϣ
						if(times%2==0)
						{
							g.setColor(Color.yellow);
							//������Ϣ������
							Font myFont=new Font("������κ",Font.BOLD,30);
							g.setFont(myFont);
							g.drawString("loading...",GAME_WIDTH / 2 - 50,GAME_HEIGHT / 2);
						}
						//����
						try {
							Thread.sleep(100);
							times++;
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						this.repaint();
					}
					isLoding = false; // ��ʾ���ڼ��ؽ���Ŀ��ƿ����Ѿ��ر�
					isLodingF = true; // ��ʾ��ҳ�Ѿ��������
					//System.out.println("!!!!!!");
				}else{
					int size = 50;
					Color c  = g.getColor();
					g.setColor(Color.GREEN);
					g.setFont(new Font("����", Font.LAYOUT_LEFT_TO_RIGHT, size));
					g.drawString("TankWar", GAME_WIDTH / 2 - (size * 3 / 2), GAME_HEIGHT / 2);
					new Tank(50+ 300,50,false,Direction.D,this);
				}
			}else{
				//��ǰ��Ļ�����ӵ�������
				Color c  = g.getColor();
				g.setColor(Color.GREEN);
				g.drawString("Missiles count:" + missiles.size(), 5, 40);
				//��ǰ��Ļ�ı�ը��
				g.drawString("explodes count:"+explodes.size(),5,60);
				//�з�̹�˵���Ŀ
				g.drawString("Tanks    count:"+tanks.size(), 5, 80);
				//�ҷ�̹�˵�����ֵ
				g.drawString("Tank      life:" + myTank.getLife(), 5, 100);
				
				g.drawString("Tank generation :"+generation, 5, 120);
				g.setColor(c);
				//����ط�̹��ȫ�������� �������ɳ�̹��
				if(tanks.size() <= 0){
				/*	new Thread(new tankThread()).start();*/
					generation += 200;
					for(int i=0;i<3;i++){
						tanks.add(new Tank(50+ 300*i,50,false,Direction.D,this));
						tanks.add(new Tank(100+ 400*i,50,false,Direction.D,this));
					}
					/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
				}
				//����ӵ�����̹�˵����������ײ��⡣���е��ӵ�
				for(int i=0;i<missiles.size();i++){
					Missile m = missiles.get(i);
					//���ез�̹��
					m.hitTanks(tanks);
					//�ҷ�̹�˱�����
					m.hitTank(myTank);
					//�ӵ�����ǽ
					m.hitWall(wallOne);
					m.hitWall(wallTwo);
					m.hitWall(wallThree);
					m.hitWall(wallFour);
					m.hitWall(wallFive);
					//����myhome
					boolean ans = false;
					ans = m.hitMyHome(myHome);
					if(ans){
						isOver = true;
					}
					m.draw(g);
				}
				//����ը
				for(int i = 0;i < explodes.size(); i++){
					Explode e = explodes.get(i);
					e.draw(g);
				}
				//�з�̹�ˣ���������
				for(int i = 0;i < tanks.size();i++){
					//���ÿһ��̹��
					Tank t = tanks.get(i);
					//����ײǽ�¼�
					t.collidesWall(wallOne);
					t.collidesWall(wallTwo);
					t.collidesWall(wallThree);
					t.collidesWall(wallFour);
					t.collidesWall(wallFive);
					//̹�˼���ײ�¼�
					t.collidesWithTanks(tanks);
					//��myhome��ײ
					t.collidesWithMyHome(myHome);
					t.draw(g);
				}
				
				//�����ҷ�̹��
				myTank.draw(g);
				//��Ѫ��
				myTank.eat(blood);
				//����ǽ
				wallOne.draw(g);
				wallTwo.draw(g);
				wallThree.drawNR(g);
				wallFour.drawNR(g);
				wallFive.drawNR(g);
				// ����Ѫ��
				blood.draw(g);
				//�����ϼ�
				myHome.draw(g);
			}
		}
		/**
		 * ����  ����container �е� update
		 */
		public void update(Graphics g){
			//���Ϊ�գ�������������
			if(offScreenImage == null){
				offScreenImage = this.createImage(GAME_WIDTH,GAME_HEIGHT);
			}
			if(!isOver){
				//���ñ���
				Graphics gOffScreen = offScreenImage.getGraphics();
				Color c = gOffScreen.getColor();
				gOffScreen.setColor(Color.BLACK);
				gOffScreen.fillRect(0,0,GAME_WIDTH,GAME_HEIGHT);
				gOffScreen.setColor(c);
				
				paint(gOffScreen);
				g.drawImage(offScreenImage,0,0,null);
			}
			else{
				/*System.out.println("********");*/
				g.drawImage(gameOver, GAME_WIDTH/2, GAME_HEIGHT/2, null);
			
				
			}
		}
		/**
		 * ������������Ķ�������
		 */
		public void lauchFrame(){
			//�з�̹��
			//new Thread(new tankThread()).start();
			//this.setLocation(400, 300);
			for(int i=0;i<3;i++){
				
				tanks.add(new Tank(50+ 300*i,50,false,Direction.D,this));
				tanks.add(new Tank(100+ 400*i,50,false,Direction.D,this));
			}
			/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
			//������Ϣ����С��������
			this.setSize(GAME_WIDTH, GAME_HEIGHT);
			this.setTitle("TankWar");
			//�������������������ڹر�
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
					
				}	
			});
			this.setMenuBar(new MenuBarList(this).getMb());
			//����Ϊfalse��������Ϸ���ڲ�������
			this.setResizable(false);
			//����
			this.setBackground(Color.BLACK);
			
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
			//���̼�����
			this.addKeyListener(new KeyMonitor());
			//�ɼ�
			setVisible(true);
			//�����̡߳���
			new Thread(new PaintThread()).start();
		}
		
		/**
		 * ������������Ķ�������
		 */
		public void lauchFrameStart(){
			//�з�̹��
			//new Thread(new tankThread()).start();
			//this.setLocation(400, 300);
			/*tanks.add(new Tank(50+ 300,50,false,Direction.D,this));*/
			//������Ϣ����С��������
			this.setSize(GAME_WIDTH, GAME_HEIGHT);
			this.setTitle("TankWar");
			//�������������������ڹر�
			this.addWindowListener(new WindowAdapter(){
				public void windowClosing(WindowEvent e){
					System.exit(0);
					
				}	
			});
			this.setMenuBar(new MenuBarList(this).getMb());
			//����Ϊfalse��������Ϸ���ڲ�������
			this.setResizable(false);
			//����
			this.setBackground(Color.BLACK);
			
			this.setIconImage(Toolkit.getDefaultToolkit().getImage(Tank.class.getClassLoader().getResource("images/tank.jpg")));
			//���̼�����
			this.addKeyListener(new KeyMonitor());
			//�ɼ�
			setVisible(true);
		}
		
		/**
		 * ������
		 * @param args
		 */
	public static void main(String[] args) {
		//��ʼ������tankclient
		TankClient tc = new TankClient();
		//����lanchframe
		tc.lauchFrameStart();

		tc.dispose();
		TankClient.setStart(true);
		TankClient.setLoding(true);
		new TankClient().lauchFrame();
	}
	
	/**
	 *����̹�˵��߳�,,
	 *��ϧЧ��������
	 * @author �Դ�
	 *
	 */
/*	private class tankThread implements Runnable  {
		
		public void  run(){
			
				tanks.add(new Tank(50+ 300,50,false,Direction.D,TankClient.this));
				tanks.add(new Tank(100+ 400,50,false,Direction.D,TankClient.this));
			
		}
	}*/
	/**
	 * paintThread �߳� 
	 * @author �Դ�
	 *
	 */
	private class PaintThread implements Runnable{
		public void run() {
			//��ѭ��
			while(true){
				//�ػ�
				repaint();
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * ���̼���
	 * @author �Դ�
	 *
	 */
	private class KeyMonitor extends KeyAdapter{
		//��������̧��
		public void keyReleased(KeyEvent e) {
			myTank.KeyReleased(e);
		}
		//����������ѹ
		public void keyPressed(KeyEvent e) {
				myTank.KyePressed(e);
				myHome.KyePressed(e);
			}
		}
		public static boolean isOver() {
			return isOver;
		}
		public static void setOver(boolean isOver) {
			TankClient.isOver = isOver;
		}
	}
