package Data;

import java.awt.Font;
import java.awt.Image;

import javax.swing.*;

public class LabelStack 
{
	int top = -1;
	int length;
	private int size;
	private JPanel jp;
	
	JLabel stack[];
	JLabel displayStack[];
	
	public LabelStack(int length, int size, JPanel mainPanel)
	{
		this.length = length;
		this.size = size;
		jp = mainPanel;
		
		stack = new JLabel[length];
		displayStack = new JLabel[length];
		
		ImageIcon queenImgIcon = new ImageIcon("queen_img.jpg");
		Image img = queenImgIcon.getImage();
		Image changeImg = img.getScaledInstance(size,size, Image.SCALE_SMOOTH);
		queenImgIcon = new ImageIcon(changeImg);
		
		Font stackFont = new Font("", Font.BOLD, size/2);
		for(int i = 0; i < length; i++)
		{
			stack[i] = new JLabel(queenImgIcon);
			stack[i].setSize(size,size);
			stack[i].setVisible(false);
			jp.add(stack[i], 2);
			
			displayStack[i] = new JLabel();
			displayStack[i].setBounds(645,35+(length-i-1)*size,100,size);
			displayStack[i].setFont(stackFont);
			displayStack[i].setVisible(false);
			jp.add(displayStack[i], 2);
		}
	}
	
	public int push(Coordinate data)
	{
		if(top == length-1)
		{
			return -1;
		}
		stack[++top].setLocation((50+(data.x-1)*size),(50+(data.y-1)*size));
		stack[top].setVisible(true);
		
		displayStack[top].setText(Integer.toString(data.y) + ", " + Integer.toString(data.x));
		displayStack[top].setVisible(true);
		return 1;
	}
	public int pop()
	{
		if(top == -1)
		{
			return -1;
		}
		stack[top].setVisible(false);
		displayStack[top].setVisible(false);
		
		top--;
		return 1;
	}

	public JLabel seek(int index)
	{
		return stack[index];
	}
}
