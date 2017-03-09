package www.kema.Astar;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import kema.tank.TankClient;

public class Astar {
	private static final int RANGE = 10;
	 private int x ; 
	 
	 private int y;  
	 
	 private int  targetX;
	 
	 private int  targetY;
	 private TankClient tc;
	    
	 private  Queue<Node> open;
	    
	 private  List<Node> close = new ArrayList<Node>();
	 
	 private List<Integer> closeDir = new ArrayList<Integer>();
	 
	 private int  dir[][] = {{0,-1},{-1,-1},{-1,0},{-1,1},{0,1},{1,1},{1,0},{1,-1}};
	 
	    public Astar(int  x, int y,int targetX,int targetY,TankClient tc)  
	    {  
	        this.x = x;  
	        this.y = y;  
	        this.tc = tc;
	   
	        this.targetX = targetX;
	        this.targetY =targetY;
	    }  
	    
	    public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		
	    public void init()  
	    {  
	        Comparator<Node> OrderIsdn =  new Comparator<Node>(){  
	            public int compare(Node o1, Node o2) {  
	                /*  myTankX = tc.getMyTank().getY() /tc.getMyTank().XSPEED;
	                  myTankY = tc.getMyTank().getX() / tc.getMyTank().XSPEED;*/
	                
	                
	                int dis1 = Math.abs(o1.getX() - targetX) +Math.abs(o1.getY() - targetY);
	                int dis2 = Math.abs(o2.getX() - targetX) + Math.abs(o2.getY() - targetY);
	             /*  System.out.println("dis1="+dis1+",o1.x"+o1.getX()+",o1.y"+o1.getY()+",target.x"+targetX+",target.y"+targetY);
	               System.out.println("dis2="+dis2+",o2.x"+o2.getX() +",o2.y"+o2.getY());*/
	               if(dis1 > dis2)  
	                {  
	                    return 1;  
	                }  
	                else if(dis1<dis2)  
	                {  
	                    return -1;  
	                }  
	                else  
	                {  
	                    return 0;  
	                }  
	              
	            }

	           

			
	        };  
	        open =  new PriorityQueue<Node>(100,OrderIsdn);  
	         bfs();
	         pointTodir();
	         System.out.println("closeDir size in Astart :"+close.size());
	      /*  Node node1 = new Node(5, 58);
			Node node = new Node(5,59);
		
			Node node2 = new Node(5,57);
			Node node3 = new Node(5, 56);
			open.add(node);
			
			open.add(node1);
			
			open.add(node2);
			
			open.add(node3);
			for(int i =0 ; i < open.size();i++){
				System.out.println("X"+open.poll().getX()+"Y"+open.poll().getY());
			}
			System.out.println(open.size());
			System.out.println(open.poll().getY());
			System.out.println(open.size());
			System.out.println(open.poll().getY());
			System.out.println(open.size());
			System.out.println(open.poll().getY());
			System.out.println(open.size());
			System.out.println(open.poll().getY());*/
	    }  
	    
	    
	    private void pointTodir() {
	    	System.out.println("close size in pointTodir="+close.size());
	    	int cnt = 0;
	    	int oldx = 0,oldY = 0;
	    	int currentX,currentY;
			for(Node node : close){
				if(0 != cnt){
					currentX = node.getX();
					currentY = node.getY();
					
					int tmpX = currentX - oldx;
					int tmpY = currentY - oldY;
					//根据direction 的方向转
					if(tmpX < 0){
						if(tmpY <0){
							closeDir.add(1);
						}else if(tmpY > 0){
							closeDir.add(3);
						}else if(tmpY == 0){
							closeDir.add(2);
						}
					}else if(tmpX > 0){
						if(tmpY <0){
							closeDir.add(7);
						}else if(tmpY > 0){
							closeDir.add(5);
						}else if(tmpY == 0){
							closeDir.add(6);
						}
					}else if(tmpX == 0){
						if(tmpY <0){
							closeDir.add(0);
						}else if(tmpY > 0){
							closeDir.add(4);
						}
					}
					
				}
				oldx = node.getX();
				oldY = node.getY();
				cnt++;
			}
			
		}

		public void bfs(){
	    
	    	open.clear();
	    	close.clear();
	    	Node node = new Node(x,y);
	    	open.add(node);
	    	int count = 0;
	    	while(!open.isEmpty()){
	    		Node firNode = new Node();
	    		firNode = open.poll();
	    		
	    		if(checkMeet(firNode)){
	    			return;
	    		}
	    		
	    		close.add(firNode);
	    		//步数大于10000步，，返回,,这里有BUG
	    		if(close.size() > 10000){
	    			/*for(){
	    				
	    			}*/
	    			System.out.println("size over 10000");
	    			
	    			return ;
	    		}
	    		System.out.println("targetX ="+targetX);
				System.out.println("targetY ="+targetY);
				System.out.println("currentX ="+node.getX());
				System.out.println("currentY = "+node.getY());
	    		/*System.out.println("count ="+count);
    			count++;*/
	    		for(int i = 0; i < 8; i++){
	    			Node no = new Node();
	    			no.setX(firNode.getX());
	    			no.setY(firNode.getY());
	    			
	    			if(check(no.getX() + dir[i][0],no.getY() + dir[i][1])){
	    				
	    				no.setX(no.getX() + dir[i][0]);
	    				no.setY(no.getY() + dir[i][1]);
	    				if(checkMeet(no)){
	    					close.add(no);
	    					return ;
	    				}
	    				//System.out.println("open size="+open.size());
	    				open.add(no);
	    		/*		System.out.println("open size:"+open.size());*/
	    				/*System.out.println("targetX ="+targetX);
	    				System.out.println("targetY ="+targetY);
	    				System.out.println("currentX ="+node.getX());
	    				System.out.println("currentY = "+node.getY());*/
	    				/*node.setX(node.getX() - dir[i][0]);
	    				node.setY(node.getY() - dir[i][1]);*/
	    			}
	    		}
	    	}
	    }
	    
	    private boolean check(int x, int y) {
			if(x < 0 || y < 0 || x > tc.getGameHeight() / 5  || y > tc.getGameWidth() /5){
				return false;
			}
	    	return true;
		}

		public boolean checkMeet(Node node){
	    	if(Math.abs(node.getX() - targetX) + Math.abs(node.getY() - targetY) < 10){
    			return true;
    		}
	    	return false;
	    }

		public List<Integer> getCloseDir() {
			return closeDir;
		}

	

		
}
