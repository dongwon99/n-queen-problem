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
	private LabelStack queenLabel; // 문제가 해결되는 과정을 순차적으로 보여주기 위한 퀸 이미지의 스택
	
	private int time = 250;
	private int N,size,result=0;
	private boolean pause = false; 
	//일시정지 기능을 넣는 과정에서 쓰레드를 멈추려고 할 때 화면이 멈추는 현상이 생김 -> 자바의 스윙이 Thread 기반으로 실행
	// ==> SwingWorker 사용시 백그라운드에서 스레드 실행 -> GUI 창이 멈추지 않음
	private SwingWorker<Void, Integer> worker;
	private SwingWorker<Void, Integer> stopper;
	
	public Menu()
	{
		setTitle("N-Queens Problem");
		cp = getContentPane();
		menuPanel = new JPanel(null);
		mainPanel = new JPanel(null);
		expPanel = new JPanel(null);
		setMenu(); // 메뉴 세팅
	
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);  //창 중간에 띄우기
		setResizable(false);  //창 크기 변경 불가 설정
	}
	
	public void DisposeMain()
	{
		dispose();
	}
	//메뉴 출력
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
	//시뮬레이터 출력
	public void setMain()
	{
		menuPanel.setVisible(false);
		setSize(800,600);
		
		size = setSize(); // 체스보드의 한칸 크기
		setChessBoard();  // 체스보드 세팅
		setStack();		  // 출력할 스택 이미지 세팅
		setBtn();		  // 건너뛰기, 일시정지 버튼 세팅
		queenLabel = new LabelStack(N, size, mainPanel); //gui 출력용 스택 생성
		
		
		cp.add(mainPanel);
		mainPanel.setVisible(true);
		
		initializeWorker();  // 시뮬레이터 
		worker.execute();  // 동작
	}
	// 설명서 세팅(exp 버튼)
	public void setExp()
	{
		menuPanel.setVisible(false);
		
		Font titleFont = new Font("", Font.BOLD, 30);
		Font textFont = new Font("", Font.PLAIN, 17);
		
		JLabel expTitle = new JLabel("설명서");
		JLabel expText = new JLabel("<html>"
				+ "1. 4~14 사이의 정수 N을 입력합니다.<br>"
				+ "2. 입력을 완료하면 시뮬레이션이 시작됩니다.<br>"
				+ "3. 시뮬레이션을 빠르게 끝내고 싶으면 건너뛰기 버튼을 누르고,<br> "
				+ " &nbsp; &nbsp; 시뮬레이션이 끝날 때까지 기다립니다.<br>"
				+ "4. 시뮬레이션이 완료되면 Result 라는 메모장에 해가 출력됩니다.</html>");
		
		JButton menuBtn = new JButton("돌아가기");
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
	// 체스보드 한칸의 크기 세팅
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
	// 체스보드 출력(세팅)
	public void setChessBoard()
	{
		JLabel chessBoard[][] = new JLabel[N+1][N];
		
		Color c1 = new Color(255,204,153);
		Color c2 = new Color(204,153,102);
		
		chessBoard[0] = new JLabel[N];
		//알 수 없는 오류로 첫번째 라벨 띄우고(투명화 처리) 이 다음 행부터 체스판 출력
		for(int i = 0; i < N; i++)
		{ 
			chessBoard[0][i] = new JLabel("");
			chessBoard[0][i].setBounds(50+size*i, 40 ,size,10);
			mainPanel.add(chessBoard[0][i]);
		}
		//체스판 세팅 및 출력
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
		// 스택 이미지 크기 세팅
		JLabel stack_img = new JLabel(new ImageIcon("stack_img3.png"));
		stack_img.setBounds(600,35,152,412);
		mainPanel.add(stack_img, 1.1);	
	}
	public void setBtn()
	{
		JButton skipBtn = new JButton("건너뛰기"); 
		JButton pauseBtn = new JButton("일시정지");
		skipBtn.setBounds(290, 500, 100, 20);
		pauseBtn.setBounds(400, 500, 100, 20);
		skipBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 건너뛰기 버튼을 누른경우 스레드의 sleep() 함수의 인자를 0으로
				time = 0;
			}});
		pauseBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 일시정지 버튼을 누른 경우
				pause = !pause;
				initializeStopper(); // stop 스레드 생성
				
				if(pause == false) // 일시정지 해제
				{
					stopper.execute();
					pauseBtn.setText("일시정지");
				}
				else  // 일시정지
				{
					stopper.cancel(true);
					pauseBtn.setText(" 시작 ");
				}
					
			}});
		mainPanel.add(skipBtn);
		mainPanel.add(pauseBtn);
	}
	//Result 클래스를 생성할때의 매개인자
	public Menu getMenu()
	{
		return this;
	}
	//프로그램(메인)이 끝나고 다시 메뉴창으로 갈 때 사용, Main 패널 클리어
	public void clearMainPanel()
	{
		mainPanel.removeAll();
		mainPanel.setVisible(false);
	}
	//프로그램(메인)이 끝나고 다시 실행할때(재실험)
	public void setDefault()
	{
		time = 250;
		result = 0;
		pause = false;
	}
	
	//메뉴버튼 시작, 설명, 종료 3가지 버튼에 대한 ActionListener
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
	
	//메뉴에서 시작버튼을 눌렀을 때 N값을 입력받기위한 JFrame
	class Input extends JFrame
	{
		public Input()
		{
			setTitle("N 입력");
			setSize(350,185);
			
			Container inputCp = getContentPane();
			JPanel inputPanel = new JPanel(null);
			
			JLabel titleLabel = new JLabel("4~14 사이의 수를 입력해주세요");
			JTextField inputTextField = new JTextField();
			JLabel inputLabel = new JLabel("입력");
			JButton okBtn = new JButton("확인");
			JButton cancelBtn = new JButton("취소");
			
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
						//textField 로 입력을 받되, 잘못 입력한 경우 에러처리 if 문과 catch
						N = Integer.parseInt(inputTextField.getText());
						if(N < 4 || N > 14) {
							JOptionPane.showMessageDialog(null, "잘못된 입력입니다. 4에서 14까지의 정수를 입력해주세요.", "ERROR", JOptionPane.WARNING_MESSAGE);
							miss = true;
							return;
						}
					}
					catch(NumberFormatException exp)
					{
						 JOptionPane.showMessageDialog(null, "잘못된 입력입니다. 4에서 14까지의 정수를 입력해주세요.", "ERROR", JOptionPane.WARNING_MESSAGE);
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
			setLocationRelativeTo(null);  //창 중간에 띄우기
			setResizable(false);  //창 크기 변경 불가 설정
		}
	}
	//알고리즘 해결 스레드
	private void initializeWorker()
	{
		worker = new SwingWorker<Void,Integer>()
		{
			@Override
			protected Void doInBackground() throws IOException
			{
				// 메인 알고리즘
				Stack stack = new Stack(N); // 스택 생성
				FileWriter fout = new FileWriter("Result.txt"); // 파일 출력
				int x = 1, y = 1;
				try
				{
					while(true)
					{
						for(; y <= N; y++) // 현재 x,y 부터 for문 시작((4,3)에서 해를 찾았다면 (4,4)부터 탐색 시작)
						{
							Thread.sleep(time); // 지연시간
							Coordinate temp = stack.find(x, y);  // x,y 부터 y행에 퀸을 놓을 수 있는 자리가 있는지 탐색
							if(temp == null)  // 없다면
							{
								x = stack.pop().x+1;  // 이전값 pop
								y -= 2;   			// 이전 행으로 돌아가기(for문이기 때문에 -1을 한번 더 해줌)
								
								if(y < 0)	// y=1 일 때 찾지 못했다면 더 이상 해가 없다는 뜻이므로 break 
									break;
								
								queenLabel.pop(); // 퀸의 이미지 또한 pop
								continue;
							}
							else		// 있다면
							{
								stack.push(temp);  // 좌표와 퀸의 이미지를 push
								queenLabel.push(temp);
								x = 1;      // 첫번째 열, 다음 행부터 다시 탐색
							}
							if(pause == true)     // 일시정지 버튼 클릭 유무
							{
								while(pause == true)
								{
									if(pause == false)  // 다시 눌렸다면 빠져나가기
										break;
								}
							}
						}
						if(y < 0)	// y = 1에서 더 이상 퀸을 놓을수없어 break 된 경우 while문 완전 탈출
							return null;
						
						Thread.sleep(time*4);   // 해를 모두 찾았으니 일시정지하고 결과 보여주기
					
						Coordinate lastIndex = stack.pop();  // 마지막 값 pop
						queenLabel.pop();
						x = lastIndex.x+1; // 다음 열부터 탐색을 시작하기 위한 변수 설정
						y = lastIndex.y;
												
						stack.saveResult(fout); // 파일 출력
						result++;	// 해의 개수 +1
					}
				}
				catch(InterruptedException e1)
				{
					return null;
				}
				finally { // while문이 끝났다면, 파일 출력 스트림을 닫고, 결과창 클래스 생성
						fout.close();
						new Result(result, getMenu());
				}
			}
		};
	}
	
	//일시정지 스레드
	private void initializeStopper() {
		stopper = new SwingWorker<Void,Integer>(){
		@Override
		protected Void doInBackground() throws Exception 
		{
			//일시정지 버튼을 눌렀을 때 무한반복문 생성 if로 다시 버튼이 눌렸는지 계속 검사
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
