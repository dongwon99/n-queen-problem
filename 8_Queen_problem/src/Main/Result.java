package Main;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import Data.Coordinate;
import Data.Stack;
import Data.LabelStack;

public class Result extends JFrame 
{
	private int result; 
	private Container cp;
	private JPanel jp;
	private Menu menu;
	
	public Result(int result, Menu menu)
	{
		this.result = result;
		this.menu = menu;
		setTitle("N-Queen Problem Result");
		setSize(300,200);
		cp = getContentPane();
		jp = new JPanel();
		jp.setLayout(new BorderLayout());
		
		setResult(); // 결과창 세팅
		
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);  //창 중간에 띄우기
		setResizable(false);  //창 크기 변경 불가 설정
	}	
	public void setResult()
	{
		JLabel endTitle = new JLabel("시뮬레이션 종료");
		JLabel result1 = new JLabel("해 : ");
		JLabel result2 = new JLabel(Integer.toString(result)+" 개");
		JButton reBtn = new JButton("메인 메뉴");
		JButton exitBtn = new JButton("종료");
		
		endTitle.setFont(new Font("", Font.BOLD, 23));
		result1.setFont(new Font("", Font.PLAIN, 18));
		result2.setFont(new Font("", Font.PLAIN, 18));
		reBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // 다시하기 버튼 클릭시
				menu.clearMainPanel();
				menu.setDefault();
				menu.setMenu();
			}});
		exitBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose(); // 종료하기 버튼 클릭시
				menu.dispose();
			}});
		
		Box northBox = Box.createHorizontalBox();
		northBox.add(Box.createHorizontalStrut(50));
		northBox.add(endTitle);
		
		Box centerBox = Box.createHorizontalBox();
		centerBox.add(Box.createHorizontalStrut(80));
		centerBox.add(result1);
		centerBox.add(Box.createHorizontalStrut(10));
		centerBox.add(result2);
		
		Box southBox = Box.createHorizontalBox();
		southBox.add(Box.createHorizontalStrut(70));
		southBox.add(reBtn);
		southBox.add(exitBtn);
		
		jp.add(northBox,BorderLayout.NORTH);
		jp.add(centerBox,BorderLayout.CENTER);
		jp.add(southBox,BorderLayout.SOUTH);
		cp.add(jp);
	}
}
