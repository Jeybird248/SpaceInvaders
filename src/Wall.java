import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Wall {
	private Image img; 	
	private AffineTransform tx;
	int x, y = 400;
	int health = 8;
	public Wall(int x) {
		img = getImage("imgs/wall1.jpg"); //load the image 
		this.x = x;
		tx = AffineTransform.getTranslateInstance(x, y);
		init(x, y); 				//initialize the location of the image
									//use your variables
	}
	public void changePicture(String newFileName) {
		img = getImage(newFileName);
		init(x, y);
	}
	
	public void paint(Graphics g) {
		//these are the 2 lines of code needed draw an image on the screen
		Graphics2D g2 = (Graphics2D) g;		
		update();
		g2.drawImage(img, tx, null);
	}
	public boolean checkCollision(Bullet bullet) {
		if((bullet.getX() >= x-15 && bullet.getX() <= x+50) && (bullet.getY() >= y-30 && bullet.getY() <= y+50)) {
			return true;
		}
		return false;
	}
	
	public int getHealth() {
		return health;
	}
	public void setHealth(int health) {
		if(health > 0) {
			changePicture("/imgs/wall"+(9-health)+".jpg");
		}
		this.health = health;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	private void update() {
		tx.setToTranslation(x, y);
		tx.scale(.25, .25);
	}
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		
	}
	
	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Alien.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	
}
