package com.A18CS.bcArrays;

import java.awt.Image;
import java.awt.event.KeyEvent;
import java.util.Iterator;
import java.util.LinkedList;


public class Snake {
	int speed = 10;
	int lastDirection = 0;//������һ�εķ��� 1 2 3 4 �ֱ�Ϊ��������
	int tempDirection =0;//�����ζ�ȡ�ķ���
	boolean isAlive = true;
	boolean left =false;
	boolean right = false;
	boolean up = false;
	boolean down = false;
	int stop = -1;//���Ա�ʾ���¿ո����ͣ��״̬
	int straightLength =0;//ֱ��������
	int score = 0;
	boolean isFirstStart = true;
	
	//ʳ������
	Node food =new Node();
	//������ͷ��λ������
	Image headImage = GameUtil.getImage("images/queen.png");
	Image background = GameUtil.getImage("images/background.png");
	Image rules = GameUtil.getImage("images/rules.png");
	LinkedList<Node> body = new LinkedList<Node>();
	//�ӳ�����
	//��Ҫ��β��Ϊ����һ�򷽱����
	public void bodyAdd(Node lastNode) {
		int x =60;
		int y =85;
		if(left) {
			x = lastNode.getX() -speed;
		}
		if(right) {
			x = lastNode.getY() +speed;
		}
		if(up) {
			y = lastNode.getY() - speed;
		}
		if(down) {
			y = lastNode.getY() +speed;
		}
		Node node = new Node(x,y);
		body.add(node);
	}
	//������İ��� ����������������Ϊfalse
	//ʹ����һ�ķ�������
	public void addDirection(KeyEvent e) {
		lastDirection = tempDirection;
		isFirstStart = false;
		switch(e.getKeyCode()) {
		case KeyEvent.VK_LEFT:
			if(lastDirection == 4) {
				tempDirection = lastDirection;
			}else {
				tempDirection = 3;
			}
			left = true;
			right = false;
			up = false;
			down = false;
			
			break;
		case KeyEvent.VK_RIGHT:
			if(lastDirection == 3) {
				tempDirection = lastDirection;
			}else {
				tempDirection = 4;
			}
			left = false;
			right = true;
			up = false;
			down = false;
			
			break;
		case KeyEvent.VK_UP:
			if(lastDirection == 2) {
				tempDirection = lastDirection;
			}else {
				tempDirection = 1;
			}
			left = false;
			right = false;
			up = true;
			down = false;
				
			break;
		case KeyEvent.VK_DOWN:
			if(lastDirection == 1) {
				
				
				tempDirection = lastDirection;
			}else {
				tempDirection = 2;
			}
			left = false;
			right = false;
			up = false;
			down = true;
			break;
		case KeyEvent.VK_SPACE:
			stop *= -1;
		
		case KeyEvent.VK_R:
			if(!isAlive) {
				isAlive = true;
				new MyGameFrame().launchFrame();
			}
			break;
		case KeyEvent.VK_ENTER:
			if(!isAlive) {
				System.exit(0);
			}
	}
}
	//�ı�̰���ߵķ��򣬽���β�ƶ�����ͷ��ɾ����β
	public void drawMyself() {
		if(left) {
//			body.getFirst().setX(body.getFirst().getX() - 3);
//			Iterator<Node> ite = body.iterator();
//			Node head = ite.next();
			//��������෴�İ����������ϴη�������ƶ�
			//if else��Ϊ�˷�ֹ���ְ���������ͬ�İ������߻ᰴ���෴�ķ�������ƶ���
			if(lastDirection ==4) {
				Node temp = body.getLast();
				body.removeLast();
				temp.setX(body.getFirst().getX() +speed);
				temp.setY(body.getFirst().getY());
				body.addFirst(temp);
			}else {
				Node temp = body.getLast();
				body.removeLast();
				temp.setX(body.getFirst().getX() -speed);
				temp.setY(body.getFirst().getY());
				body.addFirst(temp);
			}
			
//			while(ite.hasNext()) {
//				Node i = ite.next();
//				i.setX(head.getX() - 3);
//				head = i;
//			}
		}
		//�ƶ��ߵķ���
		//���ұ�֤�߲������෴�ķ����ƶ�
		if(right) {
			if(lastDirection ==3) {
				Node temp = body.getLast();
				body.removeLast();
				temp.setX(body.getFirst().getX() -speed);
				temp.setY(body.getFirst().getY());
				body.addFirst(temp);
			}else {
				body.getFirst().setX(body.getFirst().getX() + 3);
				Node temp = body.getLast();
				body.removeLast();
				temp.setX(body.getFirst().getX() +speed);
				temp.setY(body.getFirst().getY());
				body.addFirst(temp);
	//			System.out.println("ִ������");
			}
//			
		}
		if(up ) {
			if(lastDirection ==2) {
				Node temp = body.getLast();
				body.removeLast();
				temp.setY(body.getFirst().getY() +speed);
				temp.setX(body.getFirst().getX());
				body.addFirst(temp);
			}else {
				body.getFirst().setY(body.getFirst().getY() - 3);
				Node temp = body.getLast();
				body.removeLast();
				temp.setY(body.getFirst().getY() -speed);
				temp.setX(body.getFirst().getX()); //��Ҫ����x������ͬ
				body.addFirst(temp);
				System.out.println("��");
			}
//			
		}
		if(down ) {
			if(lastDirection ==1) {
				body.getFirst().setY(body.getFirst().getY() - 3);
				Node temp = body.getLast();
				body.removeLast();
				temp.setY(body.getFirst().getY() -speed);
				temp.setX(body.getFirst().getX()); //��Ҫ����x������ͬ
				body.addFirst(temp);
				System.out.println("��");
			}else {
				body.getFirst().setY(body.getFirst().getY() + 3);
				Node temp = body.getLast();
				body.removeLast();
				temp.setY(body.getFirst().getY() +speed);
				temp.setX(body.getFirst().getX());
				body.addFirst(temp);
			}
//			
		}
//		g.drawImage(headImage,head.getX(), head.getY() , null);
	}
	
	
	//�ж��Ƿ�Խ��
	public void ifOverBound(Node head) {
		if(head.getX() < 5 || head.getX() >730) {
			isAlive = false;
		}
		if(head.getY() <30 || head.getY() > 495) {
			isAlive =false;
		}
	}
	
	//�ж��Ƿ�Ե�ʳ��
	public boolean isAte() {
		if(Math.abs(food.getX() - body.getFirst().getX()) < 20  && Math.abs(food.getY() - body.getFirst().getY()) < 20) {
			return true;
		}
		return false;
	}
	//�ж��Ƿ�ײ���Լ�������
	public void ifEatMyself() {
		Iterator<Node> itr = body.iterator();
		Node temp =itr.next();
		int i=1;
		System.out.println("##" + getStraightLengh());
		while(i<getStraightLengh() +1 && itr.hasNext()) {
			temp = itr.next();
			i++;
			System.out.println("wokk");
		}
		Node head = body.getFirst();
		if(up || down) {
			while(itr.hasNext()) {
				if((temp.getY() == head.getY())) {
							if(Math.abs(temp.getX() - head.getX()) <4) {
								isAlive= false;
								System.out.println("���й���2");
							}
						}
					head = temp;
					temp = itr.next();
					}
			}

		if(left || right) {
			while(itr.hasNext()) {
				System.out.println("!!!");
				if((temp.getX() == head.getX())) {
							if(Math.abs(temp.getY() - head.getY()) <4) {
								isAlive= false;
								System.out.println("���й���1");
							}
						}
					head = temp;
					temp = itr.next();
					}
			}
	}
	//���ֱ��������
	public int getStraightLengh() {
		Iterator<Node> itr = body.iterator();
		int i =1;
		int tempCount =0;
		Node head =itr.next();
		Node temp = null;
		if(itr.hasNext()) {
			//���head���������� ��temp��ʾΪhead���������
			i++;
			temp =itr.next();
		}
		if(up || down) {
			while(itr.hasNext() && head.getY() == temp.getY() ) {
				tempCount = 1;
				head = temp;
				temp = itr.next();
				i++;
			}
			i += tempCount;
//			straightLength =i;
			return i;
		}
		if(left || right) {
			while(itr.hasNext() && head.getX() == temp.getX() ) {
				tempCount = 1;
				head = temp;
				temp = itr.next();
				i++;
			}
			i += tempCount;
//			straightLength =i;
			return i;
		}
		return 1;
	}
	//�������ʳ��
	public void randomCreateFood() {
		int x = 0;
		int y = 0;
		while(x<=5) {
			x = (int)(Math.random()* 730);
		}
		while( y<30) {
			y = (int)(Math.random()* 495);
		}
		food.setX(x);
		food.setY(y);
	}
	
}