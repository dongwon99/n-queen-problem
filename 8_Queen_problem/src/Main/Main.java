package Main;

import java.awt.*;
import java.awt.event.*;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.*;
import Data.Coordinate;
import Data.Stack;
import Data.LabelStack;
import javax.swing.SwingWorker;


class Menu extends JFrame
{
	private Container cp;
	private JPanel menuPanel;
	private JPanel mainPanel;
	private JPanel expPanel;
	private LabelStack queenLabel; // ������ �ذ�Ǵ� ������ ���������� �����ֱ� ���� �� �̹����� ����
	
	private int time = 250;
	private int N,size,result=0;
	private boolean pause = false; 
	//�Ͻ����� ����� �ִ� �������� �����带 ���߷��� �� �� ȭ���� ���ߴ� ������ ���� -> �ڹ��� ������ Thread ������� ����
	// ==> SwingWorker ���� ��׶��忡�� ������ ���� -> GUI â�� ������ ����
	private SwingWorker<Void, Integer> worker;
	private SwingWorker<Void, Integer> stopper;
	
	public Menu()
	{
		setTitle("N-Queens Problem");
		cp = getContentPane();
		menuPanel = new JPanel(null);
		mainPanel = new JPanel(null);
		expPanel = new JPanel(null);
		setMenu(); // �޴� ����
	
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);  //â �߰��� ����
		setResizable(false);  //â ũ�� ���� �Ұ� ����
	}
	
	public void DisposeMain()
	{
		dispose();
	}
	//�޴� ���
	public void setMenu()
	{ 
		setSize(600,400);
		
		JButton startBtn = new JButton("start");
		JButton ruleBtn = new JButton("exp");
		JButton exitBtn = new JButton("exit");
		JLabel title = new JLabel("<html>N-Queens<br>Problem</html>");
		ImageIcon title_imageicon = new ImageIcon("menu_title_image.jpg");
		JLabel title_image = new JLabel(title_imageicon);
		
		Font btnFont = new Font("", Font.BOLD, 20);
		Font titleFont = new Font("", Font.BOLD, 30);
		
		title.setBounds(50, 30, 150, 70); title.setFont(titleFont);
		title_image.setBounds(130,50,500,230);
		
		startBtn.setBounds(60, 130, 100, 30); startBtn.setFont(btnFont);
		ruleBtn.setBounds(60, 180, 100, 30); ruleBtn.setFont(btnFont);
		exitBtn.setBounds(60, 230, 100, 30); exitBtn.setFont(btnFont);
		
		startBtn.addActionListener(new menuBtnActionListener());
		ruleBtn.addActionListener(new menuBtnActionListener());
		exitBtn.addActionListener(new menuBtnActionListener());
		
		menuPanel.add(title_image);
		menuPanel.add(title);
		menuPanel.add(startBtn);
		menuPanel.add(ruleBtn);
		menuPanel.add(exitBtn);
		menuPanel.setVisible(true);
		cp.add(menuPanel);
	}
	//�ùķ����� ���
	public void setMain()
	{
		menuPanel.setVisible(false);
		setSize(800,600);
		
		size = setSize(); // ü�������� ��ĭ ũ��
		setChessBoard();  // ü������ ����
		setStack();		  // ����� ���� �̹��� ����
		setBtn();		  // �ǳʶٱ�, �Ͻ����� ��ư ����
		queenLabel = new LabelStack(N, size, mainPanel); //gui ��¿� ���� ����
		
		
		cp.add(mainPanel);
		mainPanel.setVisible(true);
		
		initializeWorker();  // �ùķ����� 
		worker.execute();  // ����
	}
	// ���� ����(exp ��ư)
	public void setExp()
	{
		menuPanel.setVisible(false);
		
		Font titleFont = new Font("", Font.BOLD, 30);
		Font textFont = new Font("", Font.PLAIN, 17);
		
		JLabel expTitle = new JLabel("����");
		JLabel expText = new JLabel("<html>"
				+ "1. 4~14 ������ ���� N�� �Է��մϴ�.<br>"
				+ "2. �Է��� �Ϸ��ϸ� �ùķ��̼��� ���۵˴ϴ�.<br>"
				+ "3. �ùķ��̼��� ������ ������ ������ �ǳʶٱ� ��ư�� ������,<br> "
				+ " &nbsp; &nbsp; �ùķ��̼��� ���� ������ ��ٸ��ϴ�.<br>"
				+ "4. �ùķ��̼��� �Ϸ�Ǹ� Result ��� �޸��忡 �ذ� ��µ˴ϴ�.</html>");
		
		JButton menuBtn = new JButton("���ư���");
		menuBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				expPanel.setVisible(false);
				menuPanel.setVisible(true);
			}});
		
		expTitle.setFont(titleFont);
		expText.setFont(textFont);
		expTitle.setBounds(20,20,100,40);
		expText.setBounds(10,70,530,200);
		menuBtn.setBounds(225, 300, 120, 30);
		
		expPanel.add(expTitle);
		expPanel.add(expText);
		expPanel.add(menuBtn);
		expPanel.setVisible(true);
		
		cp.add(expPanel);
	}
	// ü������ ��ĭ�� ũ�� ����
	public int setSize()
	{
		int size = 0;
		switch(N)
		{
		case 4:
			size = 100;
			break;
		case 5:
			size = 80;
			break;
		case 6:
			size = 67;
			break;
		case 7:
			size = 57; 
			break;
		case 8:
			size = 50;
			break;
		case 9:
			size = 44;
			break;
		case 10:
			size = 40;
			break;
		case 11:
			size = 36;
			break;
		case 12:
			size = 33;
			break;
		case 13:
			size = 31;
			break;
		case 14:
			size = 28;
			break;
		}
		return size;
	}
	// ü������ ���(����)
	public void setChessBoard()
	{
		JLabel chessBoard[][] = new JLabel[N+1][N];
		
		Color c1 = new Color(255,204,153);
		Color c2 = new Color(204,153,102);
		
		chessBoard[0] = new JLabel[N];
		//�� �� ���� ������ ù��° �� ����(����ȭ ó��) �� ���� ����� ü���� ���
		for(int i = 0; i < N; i++)
		{ 
			chessBoard[0][i] = new JLabel("");
			chessBoard[0][i].setBounds(50+size*i, 40 ,size,10);
			mainPanel.add(chessBoard[0][i]);
		}
		//ü���� ���� �� ���
		for(int i = 1; i < N+1; i++)
		{
			chessBoard[i] = new JLabel[N];
			for(int j = 0; j < N; j++)
			{
				chessBoard[i][j] = new JLabel("");
				chessBoard[i][j].setOpaque(true);
				if(i == j || Math.abs(i-j) % 2 == 0)
				{
					chessBoard[i][j].setBackground(c1);
				}
				else
				{
					chessBoard[i][j].setBackground(c2);
				}
				chessBoard[i][j].setBounds((50+j*size),(50+(i-1)*size),size,size);
				if(i == 0 && j == 0)
				{
					continue;
				}
				mainPanel.add(chessBoard[i][j], 1.1);
			}
		}
	}
	public void setStack()
	{
		// ���� �̹��� ũ�� ����
		JLabel stack_img = new JLabel(new ImageIcon("stack_img3.png"));
		stack_img.setBounds(600,35,152,412);
		mainPanel.add(stack_img, 1.1);	
	}
	public void setBtn()
	{
		JButton skipBtn = new JButton("�ǳʶٱ�"); 
		JButton pauseBtn = new JButton("�Ͻ�����");
		skipBtn.setBounds(290, 500, 100, 20);
		pauseBtn.setBounds(400, 500, 100, 20);
		skipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �ǳʶٱ� ��ư�� ������� �������� sleep() �Լ��� ���ڸ� 0����
				time = 0;
			}});
		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// �Ͻ����� ��ư�� ���� ���
				pause = !pause;
				initializeStopper(); // stop ������ ����
				
				if(pause == false) // �Ͻ����� ����
				{
					stopper.execute();
					pauseBtn.setText("�Ͻ�����");
				}
				else  // �Ͻ�����
				{
					stopper.cancel(true);
					pauseBtn.setText(" ���� ");
				}
					
			}});
		mainPanel.add(skipBtn);
		mainPanel.add(pauseBtn);
	}
	//Result Ŭ������ �����Ҷ��� �Ű�����
	public Menu getMenu()
	{
		return this;
	}
	//���α׷�(����)�� ������ �ٽ� �޴�â���� �� �� ���, Main �г� Ŭ����
	public void clearMainPanel()
	{
		mainPanel.removeAll();
		mainPanel.setVisible(false);
	}
	//���α׷�(����)�� ������ �ٽ� �����Ҷ�(�����)
	public void setDefault()
	{
		time = 250;
		result = 0;
		pause = false;
	}
	
	//�޴���ư ����, ����, ���� 3���� ��ư�� ���� ActionListener
	class menuBtnActionListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e) 
		{
			JButton btn = (JButton) e.getSource();
			
			if(btn.getText().equals("start"))
			{
				new Input();
			}
			else if(btn.getText().equals("exp"))
			{
				menuPanel.setVisible(false);
				setExp();
			}
			else
			{
				dispose();
			}
		}
	}
	
	//�޴����� ���۹�ư�� ������ �� N���� �Է¹ޱ����� JFrame
	class Input extends JFrame
	{
		public Input()
		{
			setTitle("N �Է�");
			setSize(350,185);
			
			Container inputCp = getContentPane();
			JPanel inputPanel = new JPanel(null);
			
			JLabel titleLabel = new JLabel("4~14 ������ ���� �Է����ּ���");
			JTextField inputTextField = new JTextField();
			JLabel inputLabel = new JLabel("�Է�");
			JButton okBtn = new JButton("Ȯ��");
			JButton cancelBtn = new JButton("���");
			
			titleLabel.setBounds(75,20,200,20);
			inputTextField.setBounds(50,50,250,30);
			inputLabel.setBounds(20,42,50,50);
			okBtn.setBounds(100,100,60,30);
			cancelBtn.setBounds(170,100,60,30);
			
			okBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boolean miss = false;
					try
					{
						//textField �� �Է��� �޵�, �߸� �Է��� ��� ����ó�� if ���� catch
						N = Integer.parseInt(inputTextField.getText());
						if(N < 4 || N > 14) {
							JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�. 4���� 14������ ������ �Է����ּ���.", "ERROR", JOptionPane.WARNING_MESSAGE);
							miss = true;
							return;
						}
					}
					catch(NumberFormatException exp)
					{
						 JOptionPane.showMessageDialog(null, "�߸��� �Է��Դϴ�. 4���� 14������ ������ �Է����ּ���.", "ERROR", JOptionPane.WARNING_MESSAGE);
						 miss = true;
						 return;
					}
					finally {
						if(!miss) {
							dispose();
							setMain();
						}
					}
				}});
			cancelBtn.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					dispose();
				}});
			
			inputPanel.add(titleLabel);
			inputPanel.add(inputTextField);
			inputPanel.add(inputLabel);
			inputPanel.add(okBtn);
			inputPanel.add(cancelBtn);
			
			inputCp.add(inputPanel);
			
			setVisible(true);
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setLocationRelativeTo(null);  //â �߰��� ����
			setResizable(false);  //â ũ�� ���� �Ұ� ����
		}
	}
	//�˰��� �ذ� ������
	private void initializeWorker()
	{
		worker = new SwingWorker<Void,Integer>()
		{
			@Override
			protected Void doInBackground() throws IOException
			{
				// ���� �˰���
				Stack stack = new Stack(N); // ���� ����
				FileWriter fout = new FileWriter("Result.txt"); // ���� ���
				int x = 1, y = 1;
				try
				{
					while(true)
					{
						for(; y <= N; y++) // ���� x,y ���� for�� ����((4,3)���� �ظ� ã�Ҵٸ� (4,4)���� Ž�� ����)
						{
							Thread.sleep(time); // �����ð�
							Coordinate temp = stack.find(x, y);  // x,y ���� y�࿡ ���� ���� �� �ִ� �ڸ��� �ִ��� Ž��
							if(temp == null)  // ���ٸ�
							{
								x = stack.pop().x+1;  // ������ pop
								y -= 2;   			// ���� ������ ���ư���(for���̱� ������ -1�� �ѹ� �� ����)
								
								if(y < 0)	// y=1 �� �� ã�� ���ߴٸ� �� �̻� �ذ� ���ٴ� ���̹Ƿ� break 
									break;
								
								queenLabel.pop(); // ���� �̹��� ���� pop
								continue;
							}
							else		// �ִٸ�
							{
								stack.push(temp);  // ��ǥ�� ���� �̹����� push
								queenLabel.push(temp);
								x = 1;      // ù��° ��, ���� ����� �ٽ� Ž��
							}
							if(pause == true)     // �Ͻ����� ��ư Ŭ�� ����
							{
								while(pause == true)
								{
									if(pause == false)  // �ٽ� ���ȴٸ� ����������
										break;
								}
							}
						}
						if(y < 0)	// y = 1���� �� �̻� ���� ���������� break �� ��� while�� ���� Ż��
							return null;
						
						Thread.sleep(time*4);   // �ظ� ��� ã������ �Ͻ������ϰ� ��� �����ֱ�
					
						Coordinate lastIndex = stack.pop();  // ������ �� pop
						queenLabel.pop();
						x = lastIndex.x+1; // ���� ������ Ž���� �����ϱ� ���� ���� ����
						y = lastIndex.y;
												
						stack.saveResult(fout); // ���� ���
						result++;	// ���� ���� +1
					}
				}
				catch(InterruptedException e1)
				{
					return null;
				}
				finally { // while���� �����ٸ�, ���� ��� ��Ʈ���� �ݰ�, ���â Ŭ���� ����
						fout.close();
						new Result(result, getMenu());
				}
			}
		};
	}
	
	//�Ͻ����� ������
	private void initializeStopper() {
		stopper = new SwingWorker<Void,Integer>(){
		@Override
		protected Void doInBackground() throws Exception 
		{
			//�Ͻ����� ��ư�� ������ �� ���ѹݺ��� ���� if�� �ٽ� ��ư�� ���ȴ��� ��� �˻�
			if(pause == true)
			{
				while(pause == true)
				{
					if(pause == false)
						break;
				}
			}
			return null;
		}};
	}
}



public class Main 
{
	public static void main (String[] args)
	{
		new Menu();
	}
}
