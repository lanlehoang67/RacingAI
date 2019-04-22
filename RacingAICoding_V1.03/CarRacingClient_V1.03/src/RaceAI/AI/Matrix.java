package RaceAI.AI;
import java.awt.Point;

public class Matrix {
	char map= ' ';				//	luu map: tuong, kieu duong di
	float percent= 0;			//	xac xuat: co the den duoc 
	int pass= 0;				//	dem so lan da di qua cho nay
	int dir= 4;					//	dem so nga~
	boolean guest= false;		//	doan duong do chac chan khong can di cung biet :D
	int rightWay= -1;			//	huong di dung
	Point back;					//	Point truoc do
	
	public Matrix(char map, float percent, int pass)
	{
		this.map= map;
		this.percent= percent;
		this.pass= pass;
	}
	
	
	
	
	
	
	
	
	
	
	
	
//	Update Info -------------------------------------------
	public void upMap(char map)
	{
		this.map= map;
	}
	public void upPercent(float percent)
	{
		if( this.percent != 1 )	this.percent= percent;
	}
	public void upPass()
	{
		this.pass++;
	}
	public void upDir(int dir)
	{
		if( this.percent != 1 )	this.dir= dir;
	}
	public void upGuest()
	{
		if( this.percent == 1 )
			this.guest= true;
		else
			this.guest= !(this.guest);
	}
	public void upWay(int way)
	{
		this.rightWay= way;
	}
	public void upBack(Point last)
	{
		this.back= last;
	}

//	Get Info ----------------------------------------------
	public char map()			//	getMap
	{
		return this.map;
	}
	public float percent()
	{
		return this.percent;
	}
	public int pass()
	{
		return this.pass;
	}
	public int dir()
	{
		return this.dir;
	}
	public boolean guest()
	{
		return this.guest;
	}
	public int way()
	{
		return this.rightWay;
	}
	public Point back()
	{
		return back;
	}
}