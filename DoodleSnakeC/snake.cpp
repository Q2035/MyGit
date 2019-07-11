#include<stdio.h>
#include<time.h>
#include<stdlib.h>
#include<windows.h>
#include<conio.h>

#define HEIGHT 25
#define WIDTH 75


void gridLoader();
void foodRandomCreate();
void snakeStart();
void getKeyInput();
void locationChange();
void isEatFood();
void addHead();
void isEatItself();

//size��Ϊ�ߵĳߴ磬ͬʱ�ֿ����ڼ���������
int size =4;
//��ʷ��߷�
int bestScore =0;

//��ʶ ʳ���Ƿ񱻳�
//��ʾ�ߵ�����״̬
int isEaten =0;
int isAlive =1;

//��ά�����������λ��

int snake[HEIGHT][WIDTH];

//ʳ��λ��
int food[2] = {1,1};

//����ķ����Լ���һ�εķ������ڱ�֤�߲���ͻȻ���෴�ķ����ƶ�
char direction =' ';
char lastDirection =' ';


//��ͷ��β���� �����ƶ�
int headX;
int headY;

int tailX;
int tailY;

//���峣���ƶ��ٶ�
const int speed =1;

int main(void){

	char tryAgain ='y';
	//�������ʳ��
Again:
	direction =' ';
 	lastDirection =' ';
	foodRandomCreate();
	//�����ʼ��
	snakeStart();
	do{
		if(isAlive){
			//ʳ�ﱻ�� ���ٴ�����

			if(isEaten){
				foodRandomCreate();
			}
			//��ȡ������������
			getKeyInput();
			//�ı��ߵ�����
			locationChange();

			
			//�ж��Ƿ�Ե�ʳ�� ���� ���������峤��
			isEatFood();
			//�߽硢�ߡ�ʳ������
			gridLoader();
			//����
			system("CLS");
			//ÿ��25֡
			Sleep(40);
		}else{
			//�Ƿ����¿�ʼ
			printf("Try again? y/n\n");
			scanf(" %c",&tryAgain);
			if(tryAgain =='y'){
				isAlive =1;
				//�����ʼ��
				goto Again;
			}else{
				break;
			}
			
		}

	}while(1);

	//��Ϸ����
	if(bestScore <size -4){
		bestScore =size -4;
	}
	//����ɼ��Լ���ѳɼ�
	printf("GAME OVER! \n");
	printf("YOUR SCORE: %d\n", size -4);
	printf("BESTSCORE:%d\n", bestScore);
	
	return 0;
}

//����ߡ�ʳ���ͼ��
void gridLoader(){
	int i,j;

	for (i = 0; i < HEIGHT;i++)
	{
		printf("|");
		//��������Ҫ���
		if(i==0 || i==HEIGHT - 1){
			for (j = 0; j < WIDTH; j++)
			{
				printf("-");
			}
		}else{
			for (j = 0; j < WIDTH; j++)
			{
				
				//�����������������ӡ����
				//ͬ�������������������ӡʳ��
				if(snake[i][j]){
					//������
					printf("*");
				}else if(i==food[0] && j == food[1]){
					// ����ʳ��
					printf("#");
				}else{
					printf(" ");
				}

			}
		}
		//�ڱ߿��Ҳ���ʾ��Ϸ����
		if(i==0){
			printf("|��Ϸ����:\n");
		}else if(i ==1){
			printf("|1.�ߵ��ƶ�����w a s d�ֱ������ �� �� ��\n");
		}else if(i ==2){
			printf("|2.�������ⰴ��Ϊ��ͣ\n");
		}else if(i ==3){
			printf("|3.��֮ͣ��ɰ������������Ϸ\n");
		}else if(i ==4){
			printf("|4.�����뷽���෴�ļ�����Ч\n");
		}else if(i ==6){
			printf("|����:%d\n", size -4);
		}else if(i ==5){
			printf("|5.��Ϸ�����󣬿�ѡ�����¿�ʼ\n");
		}else{
			printf("|\n");
		}
		

	}
}

void foodRandomCreate(){
	srand((unsigned)time(NULL));
	food[0] =rand()%(HEIGHT -3) +1;
	food[1] =rand()%(WIDTH -3) +1;
	isEaten = 0;
}

void snakeStart(){
	int i=0;
	int j=0;
	for (i = 0; i < HEIGHT; ++i)
	{
		for (j = 0; j < WIDTH; ++j)
		{
			snake[i][j] = 0;
		}
	}
	//Ϊ�˱���Ѱ����ͷ�Լ���β �ֶ��������ֽ��б����ʶ
	//���Խ��ж�ÿһ��������� ��ͷ������һ��Ϊ1-size
	snake[2][3]=4;
	snake[2][4]=3;
	snake[2][5]=2;
	snake[2][6]=1;

	headX =2;
	headY =6;

	tailX =2;
	tailY =3;
}

void getKeyInput(){
	if(kbhit()){
		lastDirection =direction;
		direction =getch();
		//��������һ������ķ�����ͬ��ֵ��Ч
		if(direction == 'w' && lastDirection =='s'){
			direction ='s';
		}else if(direction =='s' && lastDirection =='w'){
			direction ='w';
		}else if(direction =='a' && lastDirection =='d'){
			direction ='d';
		}else if(direction == 'd' && lastDirection =='a'){
			direction ='a';
		}
	}
}

//�ı��ߵ�λ������
void locationChange(){

	int x,y;

	switch(direction){
		case 'w':
			headX -=speed;
			break;
		case 's':
			headX +=speed;
			break;
		case 'a':
			headY -=speed;
			break;
		case 'd':
			headY +=speed;
			break;
		default:
			break;		
	}
	//�ж��Ƿ�Ե��Լ� ��ֻ�з��������Ч
	isEatItself();
	//�ж��Ƿ�Խ��
	if (headX <=1 || headX >=HEIGHT || headY<=1 ||headY >=WIDTH)
	{
		isAlive =0;
	}

	//��β����������Ϊ 0�� ������ʧ
	snake[tailX][tailY] =0;

	for (x = 0; x < HEIGHT; ++x)
	{
		for (y = 0; y < WIDTH; ++y)
		{
			if (snake[x][y] >0 && snake[x][y] <size)
			{
				//������β��Ϊ��β
				if (snake[x][y] ==size -1){

					//�����β������
					tailX =x;
					tailY =y;
					snake[x][y] =size;
				}else{
					//�������1 С��size
					//�������ߵ����壨��ȥͷ��β��
					//Ҫʵ�����ǵ��ƶ� ��Ҫ��������� 
					snake[x][y] ++;
				}
			}
		}
	}
	// ��������ͷ����
	snake[headX][headY] =1;
}

//�ж��Ƿ�Ե�ʳ��
void isEatFood(){
	if(headX ==food[0] && headY ==food[1]){
		isEaten =1;
		//����ͷ�� ���ӳ�����
		addHead();
	}
}


void addHead(){
	int x,y;

	switch(direction){
		case 'w':
			headX -=speed;
			break;
		case 's':
			headX +=speed;
			break;
		case 'a':
			headY -=speed;
			break;
		case 'd':
			headY +=speed;
			break;
		default:
			break;		
	}

	isEatItself();
	// �ж��Ƿ�Խ��
	if (headX <=1 || headX >=HEIGHT || headY<=1 ||headY >=WIDTH)
	{
		isAlive =0;
	}

	for (x = 0; x < HEIGHT; ++x)
	{
		for (y = 0; y < WIDTH; ++y)
		{
			if (snake[x][y] >0 && snake[x][y] <=size)
			{
				//������β��Ϊ��β
					snake[x][y] ++;
			}
		}
	}


	snake[headX][headY] =1;
	size ++;
}

//�ж��Ƿ�Ե��Լ�
void isEatItself(){
	int x,y;

	for (x = 0; x < HEIGHT; ++x)
	{
		for (y = 0; y < WIDTH; ++y)
		{
			if (snake[x][y] >1 && snake[x][y] <=size)
			{
				//��������ͷ������һ�� ��Ϊ����
				if(headX ==x && headY ==y){
					isAlive =0;
				}
			}
		}
	}
}