package www.kema.GameAI;


public class Chromsome implements Comparable<Chromsome> {
	String gene ;
	double fitness ;
	double p ;
	public Chromsome(){
		this.gene = "";
		this.fitness = 0.0;
		this.p = 0.0;
	}
	@Override
	public int compareTo(Chromsome o) {

		return (int) (-this.fitness + o.fitness);
	}
	
}
