package RaceAI.AI;

import java.awt.Point;
import java.util.Random;
import java.util.Vector;

import RaceAI.RaceClient.Car;
import RaceAI.RaceClient.Race;
import RaceAI.AI.Matrix;
public class MainAI {
	Race race;
	Vector<Car> All_cars;
	Car Mycar;
	Matrix[][] matrix = new Matrix[1000][1000];
	public String key = "0000"; // Go-Back-Left-Right (Up - Down - Left - Right)
	int rowLength=0;
	int colLength=0;
	double hypotenuse=1;
	int numCar=1;
	int blockSize =5;
	Point now = new Point(1,1),finish,next=now,difNow = new Point(0,0);
	Point last = new Point(0,0);
	public MainAI(Race race, Vector<Car> cars, Car Mycar){
		this.race = race;
		this.Mycar = Mycar;
		this.All_cars = cars;
		rowLength= this.race.BlockRow() -2;
		colLength = this.race.BlockColumn() -2;
		finish = new Point(rowLength,colLength);
		hypotenuse = Math.sqrt(rowLength*rowLength+colLength*colLength);
		numCar = All_cars.size();
		blockSize = this.race.BlockSize();
	}
	boolean CheckSamePoint(Point a, Point b) {
		if(a.x == b.x && a.y == b.y) {
			return true;
		}
		return false;
	}
	boolean CheckSamePoint(Point a,int x,int y){
		if(a.x==x && a.y==y) {
			return true;
		}
		return false;
	}
	// matran
	float xacsuat(int x,int y) {
		float xA = finish.x - x;
		float yA = finish.y -y;
		return (1-(float)Math.sqrt(Math.pow(xA, 2)+Math.pow(yA, 2)));
	}
	void initMatrix(int x,int y) {
		int lenRow = this.race.BlockRow();
		int lenCol = this.race.BlockColumn();
		for(int i=0;i<lenRow;i++) {
			for(int j=0;j<lenCol;j++) {
				if(i>0 && j>0 && i<lenRow-1 && j<lenCol-1) {
					matrix[i][j] = new Matrix(' ',xacsuat(i,j),0);
				}
				else {
					matrix[i][j] = new Matrix('1',-1,0);
				}
			}
		}
		matrix[1][1].upMap('0');
	}
	int countWall(int x,int y) {
		int countwall=0;
		for(int i=0;i<4;i++) {
			if(matrix[x+ix[i]][y+iy[y]].map()=='1'||matrix[x+ix[i]][y+iy[i]].percent()==-1) {
				countwall++;
			}
		}
			return countwall;
	
	}
	void setXS(int x,int y) {
		for(int i=0;i<8;i++) {
			if(matrix[x+ix[i]][y+iy[i]].map()==' ') {
				matrix[x+ix[i]][y+iy[i]].upMap(this.race.BlockKind(x, y));
				if(matrix[x+ix[i]][y+iy[i]].map()=='1') {
					matrix[x+ix[i]][y+iy[i]].upPercent(-1);
				}
			}
		}
		setAgain(x,y);
	}
	void setAgain(int col,int row) {
		matrix[col][row].upDir(4-countWall(col,row));
		matrix[col][row].pass();
	}
	Random rand = new Random();
	int[] ix = {0, 1, 0, -1,-1,-1,1,1};
	int[] iy = {1, 0, -1, 0,-1,1,1,-1};
	int left=0,down=1,right=2,up=3;
	//last position
	double lx=0,ly=0;
	// last speed
	double speed = 0;
	// your AI
	public void AI(){
		// AI example
		//Block Index
		int x = (int) (this.Mycar.getx() / this.race.BlockSize());
		int y = (int) (this.Mycar.gety() / this.race.BlockSize());
		
		double speed_now = Math.sqrt((this.Mycar.getx()-lx)*(this.Mycar.getx()-lx)+(this.Mycar.gety()-ly)*(this.Mycar.gety()-ly));
		speed = (speed*2+speed_now)/3;
		lx=this.Mycar.getx();
		ly=this.Mycar.gety();
		System.out.println(speed+ ", "+this.race.BlockSize()*0.01);
		
		this.now = new Point(x,y);
		if (this.next==null) this.next = this.now;
		if (this.last==null) this.last = this.now;
		
		//Next Block Center Coordinate
		double block_center_x= (this.next.x + 0.5) * this.race.BlockSize();
		double block_center_y= (this.next.y + 0.5) * this.race.BlockSize();
		
		//Car's Direction
		double v_x = Math.cos(this.Mycar.getalpha() * Math.PI/180);
		double v_y = Math.sin(this.Mycar.getalpha() * Math.PI/180);
		
		//Vector to Next Block Center from Car's position
		double c_x = block_center_x - this.Mycar.getx();
		double c_y = block_center_y - this.Mycar.gety();
		double distance2center = Math.sqrt(c_x*c_x+c_y*c_y);
		if (distance2center!=0) {
			//vector normalization
			c_x/=distance2center;
			c_y/=distance2center;
		}
		
		
		if (distance2center<this.race.BlockSize()*0.2){
			this.key = "0000"; //stop
			// find new next block
			boolean find=false;
			{
				int temp;
				int i1=rand.nextInt(4);
				int i2=rand.nextInt(4);
				temp = ix[i1];
				ix[i1] = ix[i2];
				ix[i2] = temp;
				temp = iy[i1];
				iy[i1] = iy[i2];
				iy[i2] = temp;
				
			}
			int i;
			for (i=0;i<4;i++)
				if ((last.x!=x+ix[i] || last.y!=y+iy[i]) && this.race.BlockKind(x+ix[i], y+iy[i]) !='1'){
					find = true;
					break;
				}
			if (find) this.next = new Point(x+ix[i], y+iy[i]);
			else this.next=this.last;
			this.last=this.now;
		}
		else {
			// Go to next block center
			double inner = v_x*c_x + v_y*c_y;
			double outer = v_x*c_y - v_y*c_x;
			if (inner > 0.995){
					this.key = "1000"; //go
			} else {
				if (inner < 0){
					this.key = "0101"; //turn right
				}
				else {
					if (this.race.BlockKind(x, y)!='3')
						if (outer > 0) this.key = "0001"; //turn right
						else this.key = "0010"; //turn left
					else 
						if (outer > 0) this.key = "0010"; //turn right
						else this.key = "0001"; //turn left
				}
			}
		}
	}
}