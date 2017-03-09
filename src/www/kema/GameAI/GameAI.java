package www.kema.GameAI;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Vector;

public class GameAI {
	private static int[][] map = new int[120][160];

	private static int PS = 150; // Ⱥ���ģ
	private static int GS = 3000; // �����ģ
	private static int DS = 10;
	private static double PC = 0.75; // ����ĸ���
	private static double PM = 0.09; // ����ĸ���
	private static int DEEP = 200; // �����Ŵ�����
	private static int CMAXLEN = 100; // ��������
	private static int MMAXLEN = 50; // �������󳤶�
	private static double P_PS = 1.0 / PS;



	private static boolean isFirst = true;

	private static int ENDX = 119;
	private static int ENDY = 80;

	private int STARTX;
	private int STARTY;

	private int dep = 0;
	private int findAns = 0;
	private int ansId, ansLen;
	private int now, next;

	private double psum[] = new double[2];

	private static List<Chromsome> populationU = new ArrayList<Chromsome>(); // �ϴ���Ⱥ
	private static List<Chromsome> populationD = new ArrayList<Chromsome>(); // �´���Ⱥ

	private static List<List<Chromsome>> population = new ArrayList<List<Chromsome>>(); // ���´���Ⱥ

	private List<Integer> ans = new ArrayList<Integer>(); // ������

	/**
	 * @param args
	 */
	/*
	 * public static void main(String[] args) {
	 * 
	 * // ��map��ʼ�� for (int i = 0; i < 120; i++) { for (int j = 0; j < 160; j++)
	 * { map[i][j] = 0; }
	 * 
	 * } // ǽ for (int i = 50; i < 100; i++) { map[50][i] = 1; }
	 * 
	 * for (int i = 30; i < 50; i++) { map[30][i] = 1; } GA();
	 * 
	 * }
	 */

	public GameAI(int x, int y) {
		this.STARTX = x;
		this.STARTY = y;
		this.ans = GA();
	}

	public List<Integer> getAns() {
		return ans;
	}

	public void setAns(List<Integer> ans) {
		this.ans = ans;
	}

	/**
	 * �Ŵ��㷨
	 * 
	 * @return
	 */
	public List<Integer> GA() {
		if (isFirst) {
			init();
			isFirst = false;
			System.out.println("init function");
		}
		int nextn;
		int x, y;
		ansId = -1;
		ansLen = GS;
		dep = 0;
		findAns = 0;
		/*
		 * for(int i= 0; i < PS;i++){
		 * System.out.println("gene = "+population.get(now).get(i).gene); }
		 */
			System.out.println("in GA!");
			while (dep != DEEP) {
				/*
				 * for(int i= 0; i < PS;i++){ System.out.println("dep ="+dep);
				 * System
				 * .out.println("gene = "+population.get(now).get(i).gene); }
				 */
				calc();

				if (1 == findAns) {
					break;
				}
				nextn = 0;
				Chromsome sa = new Chromsome();
				// ��һ��ǰʮ�Ŵ�����
				for (int i = 0; i < DS; i++) {
					sa = population.get(now).get(i);
					next = now ^ 1;
					population.get(next).remove(nextn);
					population.get(next).add(nextn, sa);

					/*
					 * System.out.println("DS next lenth= "+population.get(next).
					 * get(nextn).gene);
					 */

					nextn++;
				}

				int count = 0;
				// ��������������Ⱥ
				while (nextn < PS) {
					Chromsome tmp = new Chromsome();

					int fla = 0;
					// �������ӽ�����
					x = Selection();
					y = Selection();

					if (x == y) {
						/* System.out.println("x == y = "+x); */
						// Random rand = new Random();
						y = (x + 1) % (PS);
						/* count ++; */
					}
					/* System.out.println("count = "+count); */
				/*	if (nextn >= PS - 1) {
						break;
					}*/
					/*System.out.println("x =" + x);*/
					next = now ^ 1;
					population.get(next).remove(nextn);
					population.get(next).add(nextn, population.get(now).get(x));

					/*
					 * System.out.println("now lenth ="+
					 * population.get(now).get(x).gene);
					 * System.out.println("next lenth= "
					 * +population.get(next).get(nextn).gene);
					 */

					nextn++;
				/*	if (nextn >= PS - 1) {
						break;
					}*/
					next = now ^ 1;
					population.get(next).remove(nextn);
				/*	System.out.println("next =" + next);
					System.out.println("now = "+now);*/
					population.get(next).add(nextn, population.get(now).get(y));

					/*
					 * System.out.println("now1 lenth ="+
					 * population.get(now).get(y).gene);
					 * System.out.println("next1 lenth= "
					 * +population.get(next).get(nextn).gene);
					 */
					nextn++;
					Random rand = new Random();
					double xx = (double) rand.nextInt(100) * 1.0 / 100.0;

					if (xx < PC) {
						crossOver(nextn - 1, nextn - 2);
					}

					// ����
					xx = (double) rand.nextInt(100) * 1.0 / 100.0;

					if (xx < PM) {
						mutation(nextn - 1);
					}

					xx = (double) rand.nextInt(100) * 1.0 / 100.0;

					if (xx < PM) {
						mutation(nextn - 2);
					}
				}
				next = now;
				now ^= 1;
				dep++;

			}

			if (1 == findAns) {
				/*
				 * System.out.println("Find it");
				 * System.out.println("generation :"+ansId);
				 */
				outPut(population.get(now).get(ansId).gene, ansLen);
				return ans;
				/* System.out.println("ans1 = "+ans); */
			} else {
				outPut(population.get(next).get(0).gene, GS);
				return ans;
				/* System.out.println("ans2 = "+ans); */
			}
	}

	public List<Integer> outPut(String gene, int ansLen2) {
		ans.clear();
		char[] s = gene.toCharArray();
		for (int i = 0; i < ansLen2; i += 3) {
			int x = (s[i] - '0') * 4 + (s[i + 1] - '0') * 2 + (s[i + 2] - '0');
			int size = ans.size() - 1;
			/*if (size > 0 && ans.get(size) != x && ans.get(size) % 4 == x % 4) {
				ans.remove(size);
			}*/

			ans.add(x);
		}
		return ans;
	}

	/**
	 * ����
	 * 
	 * @param x
	 */
	public void mutation(int x) {
		Random rand = new Random();
		int low = rand.nextInt(GS) % GS;

		int len = rand.nextInt(CMAXLEN) % CMAXLEN;
		len = Math.min(len, GS - low - 1);
		char[] sa = population.get(next).get(x).gene.toCharArray();
		/*
		 * if(sa.length < 2999){ System.out.println("char lenth ="+sa); }
		 */
		for (int i = x; i < low + len; i++) {
			sa[i] ^= 1;
		}
		population.get(now).get(x).gene = String.valueOf(sa);
	}

	/**
	 * ����
	 * 
	 * @param x
	 * @param y
	 */
	public void crossOver(int x, int y) {
		Random rand = new Random();
		int low = rand.nextInt(GS) % GS;

		int len = rand.nextInt(CMAXLEN) % CMAXLEN;

		len = Math.min(len, GS - low - 1);

		char[] sa = population.get(next).get(x).gene.toCharArray();
		char[] sb = population.get(next).get(y).gene.toCharArray();
		/*
		 * System.out.println("sa = " +String.valueOf(sa));
		 * System.out.println("sb =" +String.valueOf(sb));
		 */

		for (int i = low; i <= low + len; i++) {
			char tmp;
			tmp = sa[i];
			sa[i] = sb[i];
			sb[i] = tmp;
		}
		/*
		 * System.out.println("sa1 = " +String.valueOf(sa));
		 * System.out.println("sb1 =" +String.valueOf(sb));
		 */
		population.get(now).get(x).gene = String.valueOf(sa);
		population.get(now).get(y).gene = String.valueOf(sb);

	}

	/**
	 * ѡ���Ǹ���������죬�����̶�
	 * 
	 * @return
	 */
	public int Selection() {
		double nn = 0;
		Random rand = new Random();
		double mm = rand.nextInt(10000) * 1.0 / 0x7fff;

		Chromsome sa = new Chromsome();
		for (int i = 0; i < PS; i++) {
			sa = population.get(now).get(i);
			nn += sa.p;
			if (nn >= mm) {
				if (sa.p < P_PS)
					population.get(now).get(i).p = 0;
				else
					population.get(now).get(i).p -= P_PS;
				return i;
			}
		}
		/* System.out.println("Selection nn ="+nn); */
		return PS - rand.nextInt(PS - 1) - 1;
	}

	/**
	 * ������Ӧ��
	 */
	public void calc() {
		Chromsome sa = new Chromsome();
		psum[now] = 0;
		for (int i = 0; i < PS; i++) {
			/* System.out.println("sasa"+population.get(now)); */
			sa = population.get(now).get(i);
			/*
			 * System.out.println("i="+i);
			 * System.out.println("gene ="+population.get(now).get(i).gene);
			 */
			/* System.out.println("Startx="+STARTX /5 + "starty = "+STARTY/5); */
			if (STARTX >= 120) {
				STARTX = 50;
			}
			if (STARTY >= 160) {
				STARTY = 100;
			}
			sa.fitness = getFitnessValue(i, ENDX, ENDY, STARTX / 5, STARTY / 5);
			if (1 == findAns) {
				return;
			}
			psum[now] += sa.fitness;
			population.get(now).get(i).fitness = sa.fitness;

			/*
			 * System.out.println("fitness = "+population.get(now).get(i).fitness
			 * );
			 */

		}

		Collections.sort(population.get(now));

		/* System.out.println("psum[now] ="+psum[now]); */
		for (int i = 0; i < PS; i++) {
			double p = 0.0;
			p = (1.0 * population.get(now).get(i).fitness) / psum[now];
			population.get(now).get(i).p = p;
			/*
			 * System.out.println("population.get(now).get(i).p ="+population.get
			 * (now).get(i).p);
			 */
		}
		/* System.out.println("111"); */

	}

	/**
	 * ������Ӧ��
	 * 
	 * @param id
	 * @param endx
	 * @param endy
	 * @param startx
	 * @param starty
	 * @return
	 */
	public double getFitnessValue(int id, int endx, int endy, int startx,
			int starty) {
		// nextr �����꣬nextc ������
		int nextr = startx, nextc = starty;
		String s = null;

		s = population.get(now).get(id).gene;
		/*
		 * System.out.println("Id ="+id); System.out.println("s = "+s);
		 */
		char[] arr = s.toCharArray();

		int oldX, oldY;
		for (int i = 0; i < GS; i += 3) {
			int x = (arr[i] - '0') * 4 + (arr[i + 1] - '0') * 2
					+ (arr[i + 2] - '0');
			/* System.out.println("x = "+x); */
			oldX = nextr;
			oldY = nextc;
			switch (x) {
			case 0:
				nextc--;
				break;
			case 1:
				nextc--;
				nextr--;
				break;
			case 2:
				nextr--;
				break;
			case 3:
				nextc--;
				nextr++;
				break;
			case 4:
				nextc++;
				break;
			case 5:
				nextc++;
				nextr++;
				break;
			case 6:
				nextr++;
				break;
			case 7:
				nextc++;
				nextr--;
				break;

			default:
				break;
			}

			if (nextr >= 0 && nextr < 120 && nextc >= 0 && nextc < 180) {

				if (map[nextr][nextc] == 1) {
					nextr = oldX;
					nextc = oldY;
				}

				else if (Math.abs(nextr - endx) + Math.abs(nextc - endy) < 20) {
					findAns = 1;
					ansId = id;
					ansLen = i + 3;
					return 1000;
				}

			} else {
				if (nextc < 0) {
					nextc = 0;
				}
				if (nextc >= 180) {
					nextc = 179;
				}
				if (nextr < 0) {
					nextr = 0;
				}
				if (nextr >= 120) {
					nextr = 119;
				}
			}
		}
		/*
		 * System.out.println("nextr="+nextr);
		 * System.out.println("nextc="+nextc);
		 */
		int ret = 0;
		ret = 1000 - Math.abs(endx - nextr) - Math.abs(endy - nextc);
		return ret;
	}

	/**
	 * ��ʼ��
	 */
	public void init() {
		try {
			population.add(0, populationU);
			population.add(1, populationD);
			now = 0;
			next = now ^ 1;
			population.get(now).clear();
			population.get(next).clear();
			Random rand = new Random();
			int count = 0;
			for (int i = 0; i < PS; i++) {
				Chromsome ss = new Chromsome();
				population.get(now).add(ss);
				population.get(next).add(ss);
				// ȡ����i����Ⱥ������ֵ
				Chromsome sa = new Chromsome();
				/* System.out.println(sa); */
				sa = population.get(now).get(i);
				// ���Ϊ�գ���new һ��
				if (sa == null) {
					sa = new Chromsome();
				}
				/* System.out.println(sa); */
				for (int j = 0; j < GS; j++) {
					sa.gene += rand.nextInt(2);
				}
				// ȡ����ǰ��ֵ
				population.get(now).get(i).gene = sa.gene;
				/* System.out.println("!!!!!!"); */

				/*
				 * System.out.println("lenth = "+population.get(now).get(i).gene)
				 * ; count ++;
				 */

			}
			/*
			 * System.out.println("last = "+population.get(now).get(PS
			 * -1).gene);
			 */
			/* System.out.println("count ="+count); */
		} catch (Exception e) {

			e.printStackTrace();
		}

	}


}
