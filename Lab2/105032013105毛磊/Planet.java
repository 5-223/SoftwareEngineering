package cn.bjsxt.solar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.GameUtil;
/**
 * ������
 * @author Administrator
 *
 */
public class Planet extends Star{
	//����ͼƬ�����ꡣ��������ĳ����Բ���С����ᡢ���ᡢ�ٶȡ��Ƕȡ�����ĳ��star�ɡ�
	double longAxis;
	double shortAxis;
	double speed;
	double degree;
	Star center;
	
	public void draw(Graphics g){
		//g.drawImage(img, (int)x, (int)y, null);
		super.draw(g);
		drawTrace(g);
		move();
	}
	
	public void drawTrace(Graphics g){
		double ovalX,ovalY,ovalWidth,voalHeight;
		
		ovalWidth = longAxis*2;
		voalHeight = shortAxis*2;
		ovalX = (center.x + center.width/2) - longAxis;
		ovalY = (center.y + center.height/2) - shortAxis;
		
		Color c = g.getColor();
		g.setColor(Color.red);
		g.drawOval((int)ovalX,(int)ovalY,(int)ovalWidth,(int)voalHeight);
		g.setColor(c);
	}
	
	public void move(){
		//�������ǵ��ƶ�
		//������Բ�켣����
		x = (center.x + center.width/2) + longAxis*Math.cos(degree);//Բ��
		y = (center.y + center.height/2) + shortAxis*Math.sin(degree);

		degree += speed;            //�ٶ��ǽǶȵ�����

	}
	
	public Planet(String imgpath, double longAxis, double shortAxis, double speed, Star center) {
		super(GameUtil.getImage(imgpath));
		
		this.center = center;
		this.x = center.x + this.longAxis;
		this.y = center.y;
		//this.img = GameUtil.getImage(imgpath); 
		
		this.longAxis = longAxis;
		this.shortAxis = shortAxis;
		this.speed = speed;
		
	}
	public Planet(Image img, double x, double y) {
		super(img, x, y);
	}
	public Planet(String imgpath, double x, double y) {
		super(imgpath, x, y);
	}
	
}
