package cn.bjsxt.solar;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.GameUtil;
/**
 * 行星类
 * @author Administrator
 *
 */
public class Planet extends Star{
	//除了图片、坐标。行星沿着某个椭圆运行。长轴、短轴、速度、角度。绕着某个star飞。
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
		//关于行星的移动
		//沿着椭圆轨迹飞行
		x = (center.x + center.width/2) + longAxis*Math.cos(degree);//圆心
		y = (center.y + center.height/2) + shortAxis*Math.sin(degree);

		degree += speed;            //速度是角度的增量

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
