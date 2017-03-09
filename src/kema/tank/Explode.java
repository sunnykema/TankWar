package kema.tank;
import java.awt.*;
/**
 * ��ը
 * @author �Դ�
 *
 */
public class Explode {
	//��ը��λ��
	int x,y;
	//��ը����������ʼֵΪtrue
	private boolean live = true;
	private TankClient tc;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	// ��ը�Ļ���
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
	//�жϱ�ը�Ƿ��ʼ��������
	private boolean init = false;
	/**
	 * ���캯��
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
	 * ������ը����
	 * @param g
	 */
	public void draw(Graphics g){
		//��û��ʼ����ʱ�򣬣��Ȱ�imgs������������Ȼ��һ����ըЧ���������
		if(!init){
			init = true;
			for (int j = 0; j < imgs.length; j++) {
				g.drawImage(imgs[j], -100, -100, null);
			}
		}
		//�������,,ȥ��
		if(!live){
			tc.explodes.remove(this);
			return;
		}
		// ��ը��������
		if(step == imgs.length){
			live = false;
			step = 0;
			return ;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
}
