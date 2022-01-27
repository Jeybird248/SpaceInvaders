import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Player {
	private Image img; 	
	private AffineTransform tx;
	private int x, y;
	int speedX = 0;
	
	public Player(int x, int y) {
		img = getImage("/imgs/spaceship.png"); //load the image for Tree
		
		this.x = x;
		this.y = y;
		
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
		x += speedX;
		update();
		g2.drawImage(img, tx, null);


	}
	private void update() {
		tx.setToTranslation(x, y);
	}
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(0.25, 0.25);
	}

	private Image getImage(String path) {
		Image tempImage = null;
		try {
			URL imageURL = Health.class.getResource(path);
			tempImage = Toolkit.getDefaultToolkit().getImage(imageURL);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tempImage;
	}
	public int getX() {
		return x;
	}
	public int getSpeedX() {
		return speedX;
	}


	public void setSpeedX(int speedX) {
		this.speedX = speedX;
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


	public boolean checkCollision(Bullet bullet) {
		if((bullet.getX() >= x-15 && bullet.getX() <= x+30) && (bullet.getY() >= y-30 && bullet.getY() <= y+15)) {
			return true;
		}
		return false;
	}
}
