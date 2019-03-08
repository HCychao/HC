package tankgames;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import javax.swing.*;

public class TankGame extends JFrame implements ActionListener{
	private static final long serialVersionUID = 1L;
	
	boolean startnum = false;//�ж��Ƿ��һ��ִ�п�ʼ��Ϸ�˵�
	MyPanel mp;
	MyStartPanel msp = null;
//	MyStartPanel myStartPanel = new MyStartPanel();
	JMenuBar jmb = null;		//�˵���
	JMenu jm1 = null;			//�˵�
	JMenuItem jmi1 = null;		//�˵���

	public static void main(String[] args) {
		new TankGame().setVisible(true);

	}
	
	public TankGame(){
		this.setTitle("̹�˴�ս");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(false);
		this.setBounds(200, 50, 750, 650);
		
		//�����ҵĿ�ʼ�����󣬲���ӵ��ҵĴ�������
		msp = new MyStartPanel();
		this.add(msp);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		Thread t = new Thread(msp);
		t.start();
		
		//�����˵�
		jmb = new JMenuBar();
		jm1 = new JMenu("��Ϸ(G)");
		jm1.setMnemonic('G');   	//���ÿ�ݼ�
		jmi1 = new JMenuItem("��ʼ����Ϸ(N)");
		jmi1.setMnemonic('N');		//��ݼ�
		jm1.add(jmi1);			//���˵�����ӵ��˵���
		jmb.add(jm1);			//���˵���ӵ��˵�����
		this.setJMenuBar(jmb);	//���˵�����ӵ�������
		
		//ע������¼�����
//		this.addKeyListener(mp);
		jmi1.addActionListener(this);//Ϊ��ʼ����Ϸ�˵���ע�����
		jmi1.setActionCommand("newgame");//������ActionCommandֵΪ"newgame"
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("newgame")){
			if(startnum){//������ǵ�һ��ִ�У�������յ��˵�̹�˼����ӵ�
				while(!mp.ets.isEmpty()){
					mp.ets.get(0).isLive = false;
					mp.ets.get(0).life = 0;
					mp.ets.remove(0);
//				this.remove(mp);
				}
				while(!EnemyTank.ss.isEmpty()){
					EnemyTank.ss.get(0).isLive = false;
					EnemyTank.ss.remove(0);
				}
				this.remove(mp);//�Ƴ���һ�ε���Ϸ���
			}
			else{
				this.remove(msp);//�Ƴ���ʼ���
			}
			startnum = true;
			mp = new MyPanel();
			Thread t = new Thread(mp);
			t.start();
			this.remove(msp);
			this.add(mp);
			this.addKeyListener(mp);
			
			Tank et =mp.ets.get(0);
			this.setVisible(true);
		}
		
	}

}

//������ʼ���
class MyStartPanel extends JPanel implements Runnable{
	int times = 0;			//���ڿ�����ʾ
	public void paint(Graphics g){
		super.paint(g);
		g.fillRect(0, 0, 750, 650);
		//��ʾ��Ϣ
		if(times%2 == 0){
			g.setColor(Color.yellow);
			//������Ϣ������
			Font myFont = new Font("������κ",Font.BOLD,90);
			g.setFont(myFont);
			g.drawString("̹�˴�ս", 180, 290);
		}
	}
	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(150);
			} catch (Exception e) {}
			times++;
			//�ػ�
			this.repaint();
		}
		
	}
}

//�����ҵ��������
class MyPanel extends JPanel implements KeyListener,Runnable{
	private static final long serialVersionUID = 1L;
	int time = 0;
	int level = 1;//���ڽ���ڶ���
	int total = 0;//����ͳ�ƻ��еĵ���̹������
	//�ҵ�̹��
	MyTank myTank;
	//���˵�̹��
	public static int ensize = 3;
	public static int ensize2 = 5;
	ArrayList<EnemyTank> ets = new ArrayList<EnemyTank>();
	//��ը��ͼƬ
	Image image1 = null;
	Image image2 = null;
	Image image3 = null;
	
	//�������ӵ����������
	public MyPanel(){
		this.setLayout(null);
		//�����ҵ�̹��
		myTank = new MyTank();
		//�������˵�̹��
		for(int i=0;i<ensize;i++){
			EnemyTank et = new EnemyTank(50+i*150,10,5,2);
			Thread t = new Thread(et);
			t.start();
			ets.add(et);
			et.setEts(ets);
		}
		//��ըͼƬ
		image1 = Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("bomb_1.gif"));
		image2=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("bomb_2.gif"));
	    image3=Toolkit.getDefaultToolkit().getImage(this.getClass().getResource("bomb_3.gif"));
	}
	
	//����������ﻭ�����
	public void paint(Graphics g){
		super.paint(g);
		//���߽�
		g.setColor(new Color(20,20,20));
		g.fill3DRect(0, 0, 600, 500, false);
		//���ҵ�̹��
		if(myTank.isLive){
			drawTank(myTank,g);
		}
		else if(myTank.life>0){
			drawBomb(myTank, g);
		}
		// �����˵�̹��
		for (int i = 0; i < ets.size(); i++) {
			EnemyTank et = ets.get(i);
			if (et.isLive) {
				drawTank(et, g);
				// ��������̹�˵��ӵ�
				for (int j = 0; j < et.ss.size(); j++) {
					Shot s = et.ss.get(j);
					if (s.isLive) {
						g.setColor(new Color(200, 0, 0));
						g.fill3DRect(s.x, s.y, 2, 2, false);
					} else {
						et.ss.remove(s);
					}
				}
			} else if (et.life > 0) {
				drawBomb(et, g);
			} 
			else {
				ets.remove(et);
			}
		}
		//�����ҵ�̹���ӵ�
		for(int i=0;i<myTank.ss.size();i++){
			//���ӵ�������ȡ���ӵ�
			Shot myShot = myTank.ss.get(i);
			//���ӵ������գ����ӵ�û����ʧ�������ӵ�
			if(myShot != null && myShot.isLive == true){
				g.setColor(new Color(0,0,150));
				g.draw3DRect(myShot.x, myShot.y, 2, 2, false);
			}
			//�ӵ���ʧ�����ӵ�����ɾ�����ӵ�
			else if(myShot != null && myShot.isLive == false){
				myTank.ss.remove(myShot);
			}
		}
		//����ըЧ��
		g.drawImage(image1, 0, 0, 1, 1,this);
		//������ʾ��Ϣ
		this.showInfo(g);
		//��Ϸ˵��
        g.setColor(Color.black);
        g.drawString("����˵��",615,250);
        String str[] = {"   ��Ϸ��ʼ  N", "   �˳�   ESC","   ����      �� /  W", "   ����      �� /  S", "   ����      �� /  A", "   ����      �� /  D", "   �����ӵ�    �ո�","   ԭ�ظ���    Enter" };
        for(int i=0;i<str.length;i++){
            g.drawString(str[i],605,280+30*i);
        }
        
        // ����ҵ�̹�������������꣬��Ϸ����
        if (myTank.size == 0) {
            for (int i = 0; i < ets.size(); i++) {
                ets.remove(i);
            }
            g.setColor(Color.black);
            g.fillRect(0, 0, 600, 500);
            // ��ʾ��Ϣ
            if (time % 2 == 0)// ͨ����ʾ����ʾ��������Ч��
            {
                g.setColor(Color.red);
                // ������Ϣ������
                Font myFont = new Font("����", Font.BOLD, 50);
                g.setFont(myFont);
                g.drawString("Game over��", 200, 250);
            }
        }
        
        // ��������˵�̹�˶����𣬽��뵽�ڶ���
        if (total == ensize && level == 1) {

            myTank = new MyTank();
            // �������˵�̹��
            for (int i = 0; i < ensize2; i++) {
                EnemyTank et = new EnemyTank(15+i*100,10,5,2);
                et.speed=5;
                ets.add(et);
                Thread t = new Thread(et);
                t.start();
                // ��MyPanel�ĵ���̹�����������õ���̹��
                et.setEts(ets);
            }
            level = 0;
        }
        
        if (total == 3 + ensize2) {
            g.setColor(Color.black);
            g.fillRect(0, 0, 600, 500);
            // ��ʾ��Ϣ
            if (time % 2 == 0)// ͨ����ʾ����ʾ��������Ч��
            {
                g.setColor(Color.CYAN);
                // ������Ϣ������
                Font myFont = new Font("����", Font.BOLD, 50);
                g.setFont(myFont);
                g.drawString("ʤ������", 200, 250);
            }
        }
	}
	
	//��̹��
	public void drawTank(Tank t,Graphics g){
		int x = t.x, y = t.y, w = t.w, h = 12*t.w;
		//�������ϵ�̹��
		if(t.direct == 0){
			Graphics2D g2d = (Graphics2D)g;
			g.setColor(t.c1);
			g.fill3DRect(x, y, w, h, false);
			g.fill3DRect(x+7*w, y, w, h, false);
			g.setColor(t.c2);
			g.fill3DRect(x+w, y+2*w, 6*w, 8*w, false);
			g.fillOval(x+2*w, y+4*w, 4*w, 4*w);
			g2d.setColor(t.c3);
			g2d.setStroke(new BasicStroke(5.0f));
			g2d.drawLine(x+4*w, y, x+4*w, y+6*w);
		}
		//�������µ�̹��
		else if(t.direct == 2){
			Graphics2D g2d = (Graphics2D)g;
			g.setColor(t.c1);
			g.fill3DRect(x, y, w, h, false);
			g.fill3DRect(x+7*w, y, w, h, false);
			g.setColor(t.c2);
			g.fill3DRect(x+w, y+2*w, 6*w, 8*w, false);
			g.fillOval(x+2*w, y+4*w, 4*w, 4*w);
			g2d.setColor(t.c3);
			g2d.setStroke(new BasicStroke(5.0f));
			g2d.drawLine(x+4*w, y+6*w, x+4*w, y+12*w);
		}
		//���������̹��
		else if(t.direct == 3){
			Graphics2D g2d = (Graphics2D)g;
			g.setColor(t.c1);
			g.fill3DRect(x, y, h, w, false);
			g.fill3DRect(x, y+7*w, h, w, false);
			g.setColor(t.c2);
			g.fill3DRect(x+2*w, y+w, 8*w, 6*w, false);
			g.fillOval(x+4*w, y+2*w, 4*w, 4*w);
			g2d.setColor(t.c3);
			g2d.setStroke(new BasicStroke(5.0f));
			g2d.drawLine(x, y+4*w, x+6*w, y+4*w);
		}
		//�������ҵ�̹��
		else if(t.direct ==1){
			Graphics2D g2d = (Graphics2D)g;
			g.setColor(t.c1);
			g.fill3DRect(x, y, h, w, false);
			g.fill3DRect(x, y+7*w, h, w, false);
			g.setColor(t.c2);
			g.fill3DRect(x+2*w, y+w, 8*w, 6*w, false);
			g.fillOval(x+4*w, y+2*w, 4*w, 4*w);
			g2d.setColor(t.c3);
			g2d.setStroke(new BasicStroke(5.0f));
			g2d.drawLine(x+6*w, y+4*w, x+12*w, y+4*w);
		}
	}
	
	//�ж��ӵ��Ƿ����̹��
	public boolean isHitTank(Shot s,Tank t){
		switch(t.direct){
		case 0:
		case 2:
			if(s.x>t.x&&s.x<t.x+8*t.w&&s.y>t.y&&s.y<t.y+t.h){
				s.isLive = false;
				t.isLive = false;
				return true;
			}
			break;
		case 1:
		case 3:
			if(s.x>t.x&&s.x<t.x+t.h&&s.y>t.y&&s.y<t.y+8*t.w){
				s.isLive = false;
				t.isLive = false;
				return true;
			}
			break;
		}
		return false;
	}
	
	//�ж��ҵ��ӵ��Ƿ���е��˵�̹��
	public void hitEnemyTank(){
		Shot s = null;
		for(int i=0;i<myTank.ss.size();i++){
			s = myTank.ss.get(i);
			if(s.isLive){
				for(int j=0;j<ets.size();j++){
					EnemyTank et =ets.get(j);
					if(et.isLive){
						if(this.isHitTank(s, et)){
							total++;
						}
					}
				}
			}
		}
	}
	//�жϵ���̹�˵��ӵ��Ƿ�����ҵ�̹��
	public boolean hitMyTank(){
		for(int i=0;i<ets.size();i++){
			EnemyTank et = ets.get(i);
			for(int j=0;j<et.ss.size();j++){
				Shot s = et.ss.get(j);
				if(myTank.isLive){
					if(isHitTank(s, myTank)){
						return true;
					}
				}
			}
		}
		return false;
	}
	//����ըЧ������
	public void drawBomb(Tank t ,Graphics g){
		if(t.life>2){
			g.drawImage(image1, t.x, t.y, 90, 90, this);
		}
		else if(t.life>1){
			g.drawImage(image2, t.x, t.y, 60, 60, this);
		}
		else if(t.life>0){
			g.drawImage(image3, t.x, t.y, 30, 30, this);
		}
		t.life--;
	}
	//������ʾ��Ϣ
	public void showInfo(Graphics g){
		g.drawString("ʣ������ֵ", 10, 560);
        //�з�tankʣ������ֵ
        EnemyTank et=new EnemyTank(80,530,4,0);
        this.drawTank(et, g);
        int t = 0;
        for (int i = 0; i < ets.size(); i++) {
            EnemyTank et1 = ets.get(i);
            if (et1.isLive)
                t++;
        }
        g.drawString(t + "", 125, 560);
        //myTankʣ������ֵ
        MyTank mt = new MyTank(300, 530, 4, 0);
        this.drawTank(mt, g);
        g.drawString(myTank.size + "", 345, 560);
        //my�÷�
        mt.x = 630;
        mt.y = 100;
        this.drawTank(mt, g);
        g.setColor(Color.red);
        g.drawString("��ĳɼ�Ϊ:", 620, 85);
        g.drawString(total + "", 645, 180);
    }
	@Override
	public void keyTyped(KeyEvent e) {
		
	}
	//�ҵ�̹���ƶ�
	@Override
	public void keyPressed(KeyEvent e) {
		//�����ƶ�
		if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A && myTank.isLive){
			if(!(myTank.x<=0)){
			myTank.x -= myTank.speed;
			myTank.direct = 3;
			myTank.invincible = false;
			}
			else{
				myTank.x = myTank.x;
				myTank.direct = 3;
			}
		}
		//�����ƶ�
		else if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D && myTank.isLive){
			myTank.invincible = false;
			if(myTank.x<600-myTank.h){
				myTank.x += myTank.speed;
				myTank.direct = 1;
			}
			else{
				myTank.x = myTank.x;
				myTank.direct = 1;
			}
		}
		//�����ƶ�
		else if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W && myTank.isLive){
			myTank.invincible = false;
			if(!(myTank.y<=0)){
				myTank.y -= myTank.speed;
				myTank.direct =0;
			}
			else{
				myTank.y = myTank.y;
				myTank.direct = 0;
			}
		}
		//�����ƶ�
		else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S && myTank.isLive){
			myTank.invincible = false;
			if(myTank.y<500-myTank.h){
				myTank.y += myTank.speed;
				myTank.direct = 2;
			}
			else{
				myTank.y = myTank.y;
				myTank.direct = 2;
			}
		}
		//�����ӵ�
		else if(e.getKeyCode() == KeyEvent.VK_SPACE && myTank.isLive){
			//��������5ö�ӵ�
			if(myTank.ss.size() < 5){
				myTank.shotEnemyTank();
				myTank.invincible = false;
			}
		}
		//�ر���Ϸ
		else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
			System.exit(0);
		}
		//����܇�I���ҵ�̹��ԭ�؏ͻ�
		else if(e.getKeyCode() == KeyEvent.VK_ENTER && myTank.isLive == false && myTank.size>0){
			myTank.isLive = true;
			myTank.life = 3;
			myTank.invincible = true;
		}
		//ˢ��ͼ��
		this.repaint();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void run() {
		while(true){
			try {
				Thread.sleep(100);
			} catch (Exception e) {}
			if(myTank.isLive && myTank.invincible == false){
			//����Ŀ��̹��
			this.hitEnemyTank();
			//����̹�˻����ҵ�̹��
			if(this.hitMyTank()){
				myTank.size--;
				}
			}
			time++;
			//�ػ�
			this.repaint();
		}
		
	}

}
