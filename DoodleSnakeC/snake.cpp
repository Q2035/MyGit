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

//size作为蛇的尺寸，同时又可用于计算最后分数
int size =4;
//历史最高分
int bestScore =0;

//标识 食物是否被吃
//表示蛇的生存状态
int isEaten =0;
int isAlive =1;

//二维数组代表蛇身位置

int snake[HEIGHT][WIDTH];

//食物位置
int food[2] = {1,1};

//键入的方向，以及上一次的方向，用于保证蛇不会突然往相反的方向移动
char direction =' ';
char lastDirection =' ';


//蛇头蛇尾坐标 方便移动
int headX;
int headY;

int tailX;
int tailY;

//定义常量移动速度
const int speed =1;

int main(void){

	char tryAgain ='y';
	//随机生成食物
Again:
	direction =' ';
 	lastDirection =' ';
	foodRandomCreate();
	//蛇身初始化
	snakeStart();
	do{
		if(isAlive){
			//食物被吃 则再次生成

			if(isEaten){
				foodRandomCreate();
			}
			//获取键盘输入数据
			getKeyInput();
			//改变蛇的坐标
			locationChange();

			
			//判断是否吃到食物 若是 则增加身体长度
			isEatFood();
			//边界、蛇、食物的输出
			gridLoader();
			//清屏
			system("CLS");
			//每秒25帧
			Sleep(40);
		}else{
			//是否重新开始
			printf("Try again? y/n\n");
			scanf(" %c",&tryAgain);
			if(tryAgain =='y'){
				isAlive =1;
				//蛇身初始化
				goto Again;
			}else{
				break;
			}
			
		}

	}while(1);

	//游戏结束
	if(bestScore <size -4){
		bestScore =size -4;
	}
	//输出成绩以及最佳成绩
	printf("GAME OVER! \n");
	printf("YOUR SCORE: %d\n", size -4);
	printf("BESTSCORE:%d\n", bestScore);
	
	return 0;
}

//输出蛇、食物的图形
void gridLoader(){
	int i,j;

	for (i = 0; i < HEIGHT;i++)
	{
		printf("|");
		//勾画出主要框架
		if(i==0 || i==HEIGHT - 1){
			for (j = 0; j < WIDTH; j++)
			{
				printf("-");
			}
		}else{
			for (j = 0; j < WIDTH; j++)
			{
				
				//若蛇满足条件，则打印蛇身
				//同样，若满足条件，则打印食物
				if(snake[i][j]){
					//代表蛇
					printf("*");
				}else if(i==food[0] && j == food[1]){
					// 代表食物
					printf("#");
				}else{
					printf(" ");
				}

			}
		}
		//在边框右侧显示游戏规则
		if(i==0){
			printf("|游戏规则:\n");
		}else if(i ==1){
			printf("|1.蛇的移动依靠w a s d分别代表上 左 下 右\n");
		}else if(i ==2){
			printf("|2.其他任意按键为暂停\n");
		}else if(i ==3){
			printf("|3.暂停之后可按方向键继续游戏\n");
		}else if(i ==4){
			printf("|4.按下与方向相反的键，无效\n");
		}else if(i ==6){
			printf("|分数:%d\n", size -4);
		}else if(i ==5){
			printf("|5.游戏结束后，可选择重新开始\n");
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
	//为了便于寻找蛇头以及蛇尾 现对其蛇身部分进行编码标识
	//尝试进行对每一节蛇身编码 蛇头至蛇身一次为1-size
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
		//输入与上一次输入的方向相同的值无效
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

//改变蛇的位置坐标
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
	//判断是否吃到自己 ，只有放在这儿有效
	isEatItself();
	//判断是否越界
	if (headX <=1 || headX >=HEIGHT || headY<=1 ||headY >=WIDTH)
	{
		isAlive =0;
	}

	//将尾部坐标设置为 0即 代表消失
	snake[tailX][tailY] =0;

	for (x = 0; x < HEIGHT; ++x)
	{
		for (y = 0; y < WIDTH; ++y)
		{
			if (snake[x][y] >0 && snake[x][y] <size)
			{
				//将次蛇尾变为蛇尾
				if (snake[x][y] ==size -1){

					//变更蛇尾的坐标
					tailX =x;
					tailY =y;
					snake[x][y] =size;
				}else{
					//坐标大于1 小于size
					//即代表蛇的身体（除去头和尾）
					//要实现他们的移动 需要身体的序列 
					snake[x][y] ++;
				}
			}
		}
	}
	// 设置新蛇头坐标
	snake[headX][headY] =1;
}

//判断是否吃到食物
void isEatFood(){
	if(headX ==food[0] && headY ==food[1]){
		isEaten =1;
		//增加头部 即加长蛇身
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
	// 判读是否越界
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
				//将次蛇尾变为蛇尾
					snake[x][y] ++;
			}
		}
	}


	snake[headX][headY] =1;
	size ++;
}

//判断是否吃到自己
void isEatItself(){
	int x,y;

	for (x = 0; x < HEIGHT; ++x)
	{
		for (y = 0; y < WIDTH; ++y)
		{
			if (snake[x][y] >1 && snake[x][y] <=size)
			{
				//即坐标与头部坐标一致 则为死亡
				if(headX ==x && headY ==y){
					isAlive =0;
				}
			}
		}
	}
}