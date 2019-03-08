package tankgames;

import java.awt.*;
import java.util.ArrayList;

//����̹����
public class Tank {
	int x,y,w,h;
	int speed;
	int direct;
	boolean isLive = true;
	int life = 3;
	Color c1,c2,c3;
	
	public Tank(){
		x=250;
		y=400;
		w=5;
		h=60;
		speed=5;
		direct=0;
	
	}

	public Tank(int x, int y, int w, int direct) {
		super();
		this.x = x;
		this.y = y;
		this.w = w;
		this.h =this.w*12;
		this.speed = 5;
		this.direct = direct;
	}
}


//�����ҵ�̹������
class MyTank extends Tank{
	//�ҵ�̹������
	public int size = 3;
	//�Д��ҵ�̹���Ƿ����޵�״̬
	public boolean invincible = true;
	//������ɫ���󣬲�������ɫ
	public MyTank(){
		this.c1 = new Color(128,128,128);
		this.c2 = new Color(0,255,0);
		this.c3 = new Color(0,0,255);
	}
	
	public MyTank(int x, int y, int w, int direct) {
		super(x, y, w, direct);
		this.c1 = new Color(128,128,128);
		this.c2 = new Color(0,255,0);
		this.c3 = new Color(0,0,255);
	}
	//�����ӵ�����
	ArrayList<Shot> ss = new ArrayList<Shot>();
	Shot s = null;
	//�����ӵ���������
	public void shotEnemyTank(){
		//����̹�˵ķ���ȷ���ӵ������λ��
		switch(direct){
			case 0: s = new Shot((x-1)+4*w,y,direct);ss.add(s);break;
			case 1: s = new Shot(x+h,(y-1)+4*w,direct);ss.add(s);break;
			case 2: s = new Shot((x-1)+4*w,y+h,direct);ss.add(s);break;
			case 3: s = new Shot(x,(y-1)+4*w,direct);ss.add(s);break;
		}
		Thread t = new Thread(s);
		t.start();
	}
}

//����̹����
class EnemyTank extends Tank implements Runnable{
	//����̹�˵ļ���
	ArrayList<EnemyTank> ets = new ArrayList<EnemyTank>();
	//�����ӵ��ļ���(��̬�ӵ�����)
	public static ArrayList<Shot> ss = new ArrayList<Shot>();
	public EnemyTank(){
		this.c1 = new Color(128,128,128);
		this.c2 = new Color(0,255,0);
		this.c3 = new Color(0,0,255);
	}
	
	public EnemyTank(int x, int y, int w, int direct) {
		super(x, y, w, direct);
		this.speed = 3;
		this.c1 = new Color(200,200,120);
		this.c2 = new Color(0,255,127);
		this.c3 = new Color(200,0,0);
	}
	//��ȡMyPanel�ĵ���̹�˼���
	public void setEts(ArrayList<EnemyTank> ets){
		this.ets = ets;
	}
	//�ж�̹��֮����û�нӴ����о͸ı䷽��
	public boolean isTouchOtherTank(Tank t){
		for(int i=0;i<ets.size();i++){
			EnemyTank et = ets.get(i);
			if(et != this){
				//�ж�et�͵�ǰ̹�˵ľ��룬������<h+15����ı䷽��
				if(distance(t,et)<h+15){
					//����С��̹�˵ĳ���+10ʱ�޸�̹�˵ķ���
					//direct = (direct+1)%4;
					return true;
				}
			}
		}
		return false;
	}
	//������̹�˵ľ���
	private int distance(Tank t1, Tank t2) {
		Point p1,p2;
		p1 = centerPoint(t1);
		p2 = centerPoint(t2);
		return (int)(Math.sqrt((p2.x-p1.x)*(p2.x-p1.x)+(p2.y-p1.y)*(p2.y-p1.y)));
	}
	//����̹�˵����ĵ�
	public Point centerPoint(Tank t){
		Point p = new Point(0,0);
		if(t.direct == 0 || t.direct == 2){
			p.x = t.x+4*t.w;
			p.y = t.y+6*t.w;
		}
		else if(t.direct == 1 || t.direct == 3){
			p.x = t.x+6*t.w;
			p.y = t.y+4*t.w;
		}
		return p;
	}
	//�õ���̹���ƶ����߳�
	@Override
	public void run() {
		//��̹���˶�
		int num = 0;
		Tank t;		//tΪ�ƶ����̹��
		while(true){
			t = new Tank(x,y,w,direct);
			switch(t.direct){
			case 0:t.y -= this.speed;break;
			case 1:t.x += this.speed;break;
			case 2:t.y += this.speed;break;
			case 3:t.x -= this.speed;break;
			}
			if(isTouchOtherTank(t)){
				changeDirect();		//���ص���ı䷽��
			}
			else{
				x = t.x;
				y = t.y;
			}
			if(num == 50 || isToBorder()){
				//��̹������һ�������ƶ�һ��ʱ��򵽴�߽�󣬾͸ı䷽��
				num = 0;
				changeDirect();			//�ı�̹�˷���
			}
			num++;
			try {
				Thread.sleep(50);
			} catch (Exception e) {}
			//����ӵ�
			if(this.isLive && ss.size()<50 && num%10 == 0){
				Shot s = null;
				switch(direct){
				case 0:s = new Shot((x-1)+4*w,y,direct);break;
				case 1:s = new Shot(x+h,(y-1)+4*w,direct);break;
				case 2:s = new Shot((x-1)+4*w,y+h,direct);break;
				case 3:s = new Shot(x,(y-1)+4*w,direct);break;
				}
				ss.add(s);
				Thread t1 = new Thread(s);
				t1.start();
			}
		}
		
	}
	//̹�˸ı䷽��
	private void changeDirect() {
		int d = (int)(Math.random()*4);
		if(d == direct){
			direct = (direct+1)%4;
		}
		else
			direct = d;
	}
	//�ж�̹���Ƿ񵽴�߽�
	private boolean isToBorder() {
		switch (direct) {
		case 0:if(y<4*w)return true;break;
		case 1:if(x+h>600-4*w)return true;break;
		case 2:if(y+h>500-4*w)return true;break;
		case 3:if(x<4*w)return true;break;
		}
		return false;
	}
}

//�����ӵ���
class Shot implements Runnable{
	int x;
	int y;
	int direct;
	int speed = 10;
	boolean isLive = true;
	
	public Shot(int x,int y,int direct){
		this.x = x;
		this.y = y;
		this.direct = direct;
	}
	
	//�ӵ��Զ��ƶ�
	@Override
	public void run() {
		while(true){
			//�ӵ����̣߳�ÿ50�����ƶ�һ��
			try {
				Thread.sleep(50);
			} catch (Exception e) {}
			//�ж��ӵ��ķ���
			switch(direct){
			case 0:y -= speed;break;
			case 1:x += speed;break;
			case 2:y += speed;break;
			case 3:x -= speed;break;
			}
			//�ж��ӵ��Ĵ��
			if(x<=0||y<=0||x>=600||y>=500){
				isLive = false;
				break;
			}
		}
		
	}
	
}
