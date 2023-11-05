package Data;


// ÁÂÇ¥ Å¬·¡½º
public class Coordinate 
{
	public int x,y;
	Coordinate(int x, int y)
	{
		this.x=x; this.y=y;
	}
	public boolean equals(Coordinate n)
	{
		if (x == n.x && y == n.y)
			return true;
		return false;
	}
}