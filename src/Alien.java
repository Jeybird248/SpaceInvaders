import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Alien {
	int x = (int) (Math.random()*341), y=75;
	int speedX;
	private Image img; 	
	int random = (int) (Math.random()*2);
	int counter = 0;
	private AffineTransform tx;
	int deadCounter = 0;
	boolean dead = false;
	public Alien(int x, int y, int speedX) {
		if(random == 0) {
			img = getImage("imgs/alien1.png");
		}
		else {
			img = getImage("imgs/squid1.png");
		}
		this.x = x;
		this.y = y;
		this.speedX = speedX;
		
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
		counter++;
		x += speedX;
		
		update();
		if(dead) {
			deadCounter++;
		}
		
		if (x < 0 || x > 360) {
			changeDirection();
		}
		
		g2.drawImage(img, tx, null);
		if(counter >= 2) {
			if(img == getImage("imgs/alien1.png")) {
				changePicture("imgs/alien2.png");
			}
			else if(img == getImage("imgs/alien2.png")) {
				changePicture("imgs/alien1.png");
			}
			else if(img == getImage("imgs/squid1.png")) {
				changePicture("imgs/squid2.png");
			}
			else if(img == getImage("imgs/squid2.png")) {
				changePicture("imgs/squid1.png");
			}
			counter = 0;
		}

	}
	
	public boolean isDead() {
		return dead;
	}
	public void setDead(boolean dead) {
		this.dead = dead;
	}
	public int getDeadCounter() {
		return deadCounter;
	}
	public void setDeadCounter(int deadCounter) {
		this.deadCounter = deadCounter;
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
	private void changeDirection() {
		speedX *= -1.3;
		y+=30;
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
	public boolean checkCollision(Bullet bullet) {
		if((bullet.getX() >= x-15 && bullet.getX() <= x+30) && (bullet.getY() >= y-30 && bullet.getY() <= y+15)) {
			changePicture("imgs/dead.png");
			dead = true;
			return true;
		}
		return false;
	}

}
