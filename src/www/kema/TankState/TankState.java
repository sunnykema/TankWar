package www.kema.TankState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kema.tank.Direction;
import kema.tank.Missile;
import kema.tank.Tank;
import kema.tank.TankClient;

public class TankState {
	public static final int FINDHOMESTATE = 1; // 找老家
	public static final int FINDEDHOME = 2; // 找到老家
	public static final int FINDMYTANK = 3; // 发现我方坦克，，开枪
	public static final int AVOIDMISSILE1 = 4; // 躲避左邊或者右边來的子弹
	public static final int AVOIDMISSILE2 = 5; // 躲避左上或者右下來的子弹
	public static final int AVOIDMISSILE3 = 6; // 躲避上面或者下边的子弹
	public static final int AVOIDMISSILE4 = 7; // 躲避右上左下的子弹

	private final int FINDHOMESCOPE = 30; // 发现老家的范围
	private final int FINDMISSILESCOPE = 20;// 发现子弹的范围
	private final int FINDMYTANKSCOPE = 30;

	private final int tankSpeed = 5; // 子弹的速度
	private static int ENDX = 119;
	private static int ENDY = 80;

	private int currentState; // 当前的状态

	private Tank tank;
	private TankClient tc;

	/**
	 * 构造函数
	 * 
	 * @param currentState
	 * @param tank
	 * @param tc
	 */
	public TankState(int currentState, Tank tank, TankClient tc) {
		this.currentState = currentState;
		this.tc = tc;
		this.tank = tank;
	}

	/**
	 * 
	 * 最初传进来的是findhomeState 找老家
	 */
	public int update() {
		// 按照优先级来执行,,首先是保命，再是找老家，最后才是追杀我方坦克
		if (avoidMisslie()) {
		} else if (findedHome()) {
		} else if (findMyTank()) {
		}
		switch (currentState) {
		case FINDHOMESTATE:
			return FINDHOMESTATE;

		case FINDEDHOME:
			return FINDEDHOME;

		case FINDMYTANK:
			return FINDMYTANK;

		case AVOIDMISSILE1:
			return AVOIDMISSILE1;

		case AVOIDMISSILE2:
			return AVOIDMISSILE2;

		case AVOIDMISSILE3:
			return AVOIDMISSILE3;

		case AVOIDMISSILE4:
			return AVOIDMISSILE4;

		default:
			break;
		}
		return currentState;
	}

	/**
	 * 判斷是是否找到家
	 * 
	 * @return
	 */
	public boolean findedHome() {
		// 获取当前坦克的做坐标
		int currentX = tank.getY() / tankSpeed;
		int currentY = tank.getX() / tankSpeed;

		/*
		 * System.out.println("currentx int findhome = "+ currentX);
		 * System.out.println("currentY int findhome = "+ currentY);
		 */

		/*
		 * System.out.println("endx ="+ENDX); System.out.println("endY ="+ENDY);
		 */
		// 找到
		if (Math.abs(currentX - ENDX) + Math.abs(currentY - ENDY) < FINDHOMESCOPE) {
			System.out.println("find home!!!");
			this.currentState = FINDEDHOME;
			return true;
		}
		return false;

	}

	/**
	 * 判断是否躲避子弹
	 * 
	 * @return
	 */
	public boolean avoidMisslie() {
		List<Missile> list = new ArrayList<Missile>();
		// 找出所有的子弹集合
		list = tc.getMissiles();
		// 遍历
		for (Missile mis : list) {
			// 是我方坦克发出的子弹，，则执行一下操作
			if (mis.isGood()) {
				int missileX = mis.getY() / tankSpeed;
				int missileY = mis.getX() / tankSpeed;

				/*
				 * System.out.println("missilex = "+missileX);
				 * System.out.println("missiley = "+missileY);
				 */
				int tankY = tank.getX() / tankSpeed;
				int tankX = tank.getY() / tankSpeed;
				/*
				 * System.out.println("currentx int avoidMissile = "+ tankY);
				 * System.out.println("currentY int avoidMissile = "+ tankX);
				 */
				// 子弹飞到了敌坦克的旁边
				if (Math.abs(missileY - tankY) + Math.abs(missileX - tankX) < FINDMISSILESCOPE) {
					// 获取子弹飞行的方向
					Direction dir = mis.getDir();
					System.out.println("avoid missile");
					int x = Math.abs(tankX - missileX);
					int y = Math.abs(tankY - missileY);
					// 子弹在坦克的左边
					if (tankY > missileY) {

						if (dir == Direction.R || dir == Direction.RU
								|| dir == Direction.RD || dir == Direction.U
								|| dir == Direction.D) {
							if (x * 1.0 / y > 1.71) {
								System.out.println("左邊");
								currentState = AVOIDMISSILE1;
								return true;
							}
							if (tankX > missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("左下");
									currentState = AVOIDMISSILE4;
									return true;
								}
							} else if (tankX < missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("左上");
									currentState = AVOIDMISSILE2;
									return true;
								}
							}
							if (y * 1.0 / x >= 1.71) {
								System.out.println("上邊或下邊");
								currentState = AVOIDMISSILE3;
								return true;
							}
							currentState = AVOIDMISSILE1;
							return true;
						}
					}
					// 子弹在坦克的右边
					if (tankY < missileY) {
						if (dir == Direction.L || dir == Direction.LD
								|| dir == Direction.LU || dir == Direction.D
								|| dir == Direction.U) {
							if (x * 1.0 / y > 1.71) {
								System.out.println("右邊");
								currentState = AVOIDMISSILE1;
								return true;
							}
							if (tankX > missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("右下");
									currentState = AVOIDMISSILE2;
									return true;
								}
							} else if (tankX < missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("右上");
									currentState = AVOIDMISSILE4;
									return true;
								}
							}
							if (y * 1.0 / x >= 1.71) {
								System.out.println("右邊，，上或下");
								currentState = AVOIDMISSILE3;
								return true;
							}
							currentState = AVOIDMISSILE1;
							return true;
						}
					
					}
				}
			}
		}
		return false;

	}

	/**
	 * 判断是否遇见我方坦克
	 * 
	 * @return
	 */
	public boolean findMyTank() {
		// 获取当前坦克的做坐标,,两个做坐标系是反的
		int currentX = tank.getY() / tankSpeed;
		int currentY = tank.getX() / tankSpeed;
		if(!tc.getMyTank().isLive()){
			return false;
		}
		/*
		 * System.out.println("currentx int findmyTank = "+ currentX);
		 * System.out.println("currentY int findmyTank = "+ currentY);
		 */
		int myTankX = tc.getMyTank().getY() / tankSpeed;
		int myTankY = tc.getMyTank().getX() / tankSpeed;
		/*
		 * System.out.println("myTankX ="+ myTankX);
		 * System.out.println("myTankY ="+myTankY);
		 */
		// 找到
		if (Math.abs(currentX - myTankX) + Math.abs(currentY - myTankY) < FINDMYTANKSCOPE) {
			System.out.println("find myTank!!!");
			this.currentState = FINDMYTANK;
			return true;
		}
		return false;

	}

	/*
	 * private void avoidMissile(int aVOIDMISSILE) { Random rand = new Random();
	 * int step = rand.nextInt(5)+3; switch(aVOIDMISSILE){ case AVOIDMISSILE1:
	 * while(step >=0){
	 * 
	 * } break; case AVOIDMISSILE2:
	 * 
	 * break; case AVOIDMISSILE3:
	 * 
	 * break; case AVOIDMISSILE4:
	 * 
	 * break; default: break; } } private void chaseMyTank() {
	 * 
	 * } private void fireyourHome() {
	 * System.out.println("in method fireYourHome fire your home"); //获取当前坦克的做坐标
	 * int currentX = tank.getX() / tankSpeed; int currentY = tank.getY() /
	 * tankSpeed; //坦克在老家的左边 if(currentY < ENDY){ tank.setDir(Direction.R);
	 * Random rand = new Random(); int step = rand.nextInt(12) + 3; while(step
	 * >= 0){ if(rand.nextInt(40) > 30){ tank.fire(Direction.R); } } return ; }
	 * //坦克在老家右边 if(currentY > ENDY){ tank.setDir(Direction.L); Random rand =
	 * new Random(); int step = rand.nextInt(12) + 3; while(step >= 0){
	 * if(rand.nextInt(40) > 30){ tank.fire(Direction.L); } } return ; } }
	 */

}
