package Data;

import java.io.FileWriter;
import java.io.IOException;

public class Stack 
{
	private int top = -1;
	private Coordinate stack[];
	int length;
	
	// ���� �⺻ �Լ� pop, push, seek
	public Stack(int length)
	{
		this.length = length;
		stack = new Coordinate[length];
	}	
	public Coordinate push(Coordinate data)
	{
		if(top == length-1)
		{
			return new Coordinate(-1,-1);
		}
		stack[++top] = data;
		return data;
	}
	public Coordinate pop()
	{
		if(top == -1)
		{
			return new Coordinate(-1,-1);
		}
		return stack[top--];
	}
	public Coordinate seek(int index)
	{
		return stack[index];
	}
	
	// data ��� ��ǥ�� ���� ���� �� �ִ��� �˻�, �̹� ���� ���� ����.
	public boolean comparison(Coordinate data)
	{
		for(int i = 0; i <= top; i++)
		{
			if(data.x == stack[i].x || data.y == stack[i].y)
			{
				return true;
			}
			else if(data.x + data.y == stack[i].x + stack[i].y)
			{
				return true;
			}
			else if(data.x - data.y == stack[i].x - stack[i].y)
			{
				return true;
			}
		}
		return false;
	}
	//x,y ��ǥ�������� y�࿡ ���� ���� �� �ִ� ��ǥ�� �ִ��� �˻�
	public Coordinate find(int x, int y)
	{
		for (; x <= length; x++)
		{
			Coordinate temp = new Coordinate(x,y);
			if(!comparison(temp))
			{
				return temp;
			}
		}
		return null;
	}
	//�ϳ��� �ظ� ã���� ���Ͽ� ����(write)
	public void saveResult(FileWriter fout) throws IOException
	{
		for (int i = 0; i < length; i++)
		{
			String result = "(" + Integer.toString(stack[i].y) + ", " + Integer.toString(stack[i].x) + ") ";
			fout.write(result);
		}
		fout.write("\n");
	}
}