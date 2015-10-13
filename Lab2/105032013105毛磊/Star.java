package cn.bjsxt.solar;

import java.awt.Graphics;
import java.awt.Image;

import cn.bjsxt.util.GameUtil;

public class Star {
	Image img;
	double x,y;
	int width,height;
	
	public void draw(Graphics g){
		g.drawImage(img, (int)x, (int)y, null);
	}
	
	public Star(){}
	public Star(Image img){
		this.img = img;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
	}
	public 	Star(Image img,double x,double y){
		this(img);
		this.x = x;
		this.y = y;
	}
	
	public 	Star(String impath,double x,double y){
		/**
		this.img = GameUtil.getImage(impath);
		this.x = x;
		this.y = y;
		this.width = img.getWidth(null);
		this.height = img.getHeight(null);
		*/
		this(GameUtil.getImage(impath),x,y);   //通过this调用构造方法，与上面注释掉的代码结果一样
	}
}
