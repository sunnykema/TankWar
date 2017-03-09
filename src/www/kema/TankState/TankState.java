package www.kema.TankState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import kema.tank.Direction;
import kema.tank.Missile;
import kema.tank.Tank;
import kema.tank.TankClient;

public class TankState {
	public static final int FINDHOMESTATE = 1; // ���ϼ�
	public static final int FINDEDHOME = 2; // �ҵ��ϼ�
	public static final int FINDMYTANK = 3; // �����ҷ�̹�ˣ�����ǹ
	public static final int AVOIDMISSILE1 = 4; // �����߅�����ұ߁���ӵ�
	public static final int AVOIDMISSILE2 = 5; // ������ϻ���������ӵ�
	public static final int AVOIDMISSILE3 = 6; // �����������±ߵ��ӵ�
	public static final int AVOIDMISSILE4 = 7; // ����������µ��ӵ�

	private final int FINDHOMESCOPE = 30; // �����ϼҵķ�Χ
	private final int FINDMISSILESCOPE = 20;// �����ӵ��ķ�Χ
	private final int FINDMYTANKSCOPE = 30;

	private final int tankSpeed = 5; // �ӵ����ٶ�
	private static int ENDX = 119;
	private static int ENDY = 80;

	private int currentState; // ��ǰ��״̬

	private Tank tank;
	private TankClient tc;

	/**
	 * ���캯��
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
	 * �������������findhomeState ���ϼ�
	 */
	public int update() {
		// �������ȼ���ִ��,,�����Ǳ������������ϼң�������׷ɱ�ҷ�̹��
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
	 * �Д����Ƿ��ҵ���
	 * 
	 * @return
	 */
	public boolean findedHome() {
		// ��ȡ��ǰ̹�˵�������
		int currentX = tank.getY() / tankSpeed;
		int currentY = tank.getX() / tankSpeed;

		/*
		 * System.out.println("currentx int findhome = "+ currentX);
		 * System.out.println("currentY int findhome = "+ currentY);
		 */

		/*
		 * System.out.println("endx ="+ENDX); System.out.println("endY ="+ENDY);
		 */
		// �ҵ�
		if (Math.abs(currentX - ENDX) + Math.abs(currentY - ENDY) < FINDHOMESCOPE) {
			System.out.println("find home!!!");
			this.currentState = FINDEDHOME;
			return true;
		}
		return false;

	}

	/**
	 * �ж��Ƿ����ӵ�
	 * 
	 * @return
	 */
	public boolean avoidMisslie() {
		List<Missile> list = new ArrayList<Missile>();
		// �ҳ����е��ӵ�����
		list = tc.getMissiles();
		// ����
		for (Missile mis : list) {
			// ���ҷ�̹�˷������ӵ�������ִ��һ�²���
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
				// �ӵ��ɵ��˵�̹�˵��Ա�
				if (Math.abs(missileY - tankY) + Math.abs(missileX - tankX) < FINDMISSILESCOPE) {
					// ��ȡ�ӵ����еķ���
					Direction dir = mis.getDir();
					System.out.println("avoid missile");
					int x = Math.abs(tankX - missileX);
					int y = Math.abs(tankY - missileY);
					// �ӵ���̹�˵����
					if (tankY > missileY) {

						if (dir == Direction.R || dir == Direction.RU
								|| dir == Direction.RD || dir == Direction.U
								|| dir == Direction.D) {
							if (x * 1.0 / y > 1.71) {
								System.out.println("��߅");
								currentState = AVOIDMISSILE1;
								return true;
							}
							if (tankX > missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("����");
									currentState = AVOIDMISSILE4;
									return true;
								}
							} else if (tankX < missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("����");
									currentState = AVOIDMISSILE2;
									return true;
								}
							}
							if (y * 1.0 / x >= 1.71) {
								System.out.println("��߅����߅");
								currentState = AVOIDMISSILE3;
								return true;
							}
							currentState = AVOIDMISSILE1;
							return true;
						}
					}
					// �ӵ���̹�˵��ұ�
					if (tankY < missileY) {
						if (dir == Direction.L || dir == Direction.LD
								|| dir == Direction.LU || dir == Direction.D
								|| dir == Direction.U) {
							if (x * 1.0 / y > 1.71) {
								System.out.println("��߅");
								currentState = AVOIDMISSILE1;
								return true;
							}
							if (tankX > missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("����");
									currentState = AVOIDMISSILE2;
									return true;
								}
							} else if (tankX < missileX) {
								if (x * 1.0 / y < 1.71 && y * 1.0 / x < 1.71) {
									System.out.println("����");
									currentState = AVOIDMISSILE4;
									return true;
								}
							}
							if (y * 1.0 / x >= 1.71) {
								System.out.println("��߅�����ϻ���");
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
	 * �ж��Ƿ������ҷ�̹��
	 * 
	 * @return
	 */
	public boolean findMyTank() {
		// ��ȡ��ǰ̹�˵�������,,����������ϵ�Ƿ���
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
		// �ҵ�
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
	 * System.out.println("in method fireYourHome fire your home"); //��ȡ��ǰ̹�˵�������
	 * int currentX = tank.getX() / tankSpeed; int currentY = tank.getY() /
	 * tankSpeed; //̹�����ϼҵ���� if(currentY < ENDY){ tank.setDir(Direction.R);
	 * Random rand = new Random(); int step = rand.nextInt(12) + 3; while(step
	 * >= 0){ if(rand.nextInt(40) > 30){ tank.fire(Direction.R); } } return ; }
	 * //̹�����ϼ��ұ� if(currentY > ENDY){ tank.setDir(Direction.L); Random rand =
	 * new Random(); int step = rand.nextInt(12) + 3; while(step >= 0){
	 * if(rand.nextInt(40) > 30){ tank.fire(Direction.L); } } return ; } }
	 */

}
