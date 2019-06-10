package com.A18CS.bcArrays;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;


public class MyGameFrame extends Frame{
	Snake snake = new Snake();
	public void launchFrame() {
		setTitle("贪吃蛇");
		setVisible(true);
		setSize(800,550);
		setLocation(300,300);
		setBackground(Color.white);
		//蛇头初始位置：x 60 y 85
		snake.body.add(new Node(60,85));
		//随机生成食物
		snake.randomCreateFood();
		//设置关闭
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		addKeyListener(new KeyMonitor());//键盘监听器
		snake.bodyAdd(snake.body.getLast());
		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());
//		snake.bodyAdd(snake.body.getLast());//测试成功
		new PaintThread().start();
	}
	//双缓冲
	private Image offScreenImage = null;
	public void update(Graphics g) {
		offScreenImage = this.createImage(800,550);
		Graphics goff = offScreenImage.getGraphics();
		paint(goff);
		g.drawImage(offScreenImage, 0, 0, null);
	}
	@Override
	public void paint(Graphics g) {
		g.drawImage(snake.background,0,0,null);
		if(snake.isFirstStart) {
			g.drawImage(snake.rules,0,0,null);
		}
//		System.out.println("执行过");
		if(snake.isAlive && snake.stop < 0) {
			//判断是否吃到自己
//			snake.ifEatMyself();
			//判断是否吃到食物
			if(snake.isAte()) {
				snake.score ++;
				snake.bodyAdd(snake.body.getLast());
				snake.randomCreateFood();
			}else {
				g.drawImage(snake.headImage,snake.food.getX(),snake.food.getY(),null);
			}
			snake.drawMyself();
			Iterator<Node> ite = snake.body.iterator();
			while(ite.hasNext()) {
				Node i = ite.next();
				g.drawImage(snake.headImage,i.getX(), i.getY() , null);
				
			}//		g.drawImage(snake.headImage,snake.body.getFirst().getX(), snake.body.getFirst().getY() , null);
			System.out.println("X:"+snake.body.getFirst().getY() );
			System.out.println(snake.body.getFirst().getX() );
	//		snake.bodyAdd(snake.body.getLast());
			//判断是否越界
			snake.ifOverBound(snake.body.getFirst());
		}else {
			//g.drawChars("You are dead!".toCharArray(), 10, 14, 400, 225);
//			System.exit(0);
			//按下空格暂停后保留暂停前的画面
			g.drawImage(snake.headImage,snake.food.getX(),snake.food.getY(),null);
			Iterator<Node> ite = snake.body.iterator();
			while(ite.hasNext()) {
				Node i = ite.next();
				g.drawImage(snake.headImage,i.getX(), i.getY() , null);
				
			}
			if(!snake.isAlive)
//				g.drawImage(snake.gameOver,-200,0,null);
				g.drawString("游戏结束！", 400, 250);
				g.drawString("你的得分：" + snake.score, 400, 280);
		}
		
	}
	public static void main(String[] args) {
		MyGameFrame f =new MyGameFrame();
		f.launchFrame();
	}
	
	class PaintThread extends Thread{
		public void run() {
			while(true) {
				repaint();
				try {
					Thread.sleep(40);
				}catch(InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class KeyMonitor extends KeyAdapter{
		public void keyPressed(KeyEvent e) {
			snake.addDirection(e);
		}
	}
}