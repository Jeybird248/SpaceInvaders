import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class Health {
	private Image img; 	
	private AffineTransform tx;
	private int x, y;
	private int health = 3;
	
	public Health(int x, int y) {
		img = getImage("/imgs/health_3.png"); //load the image for Tree
		
		this.x = x;
		this.y = y;
		
		tx = AffineTransform.getTranslateInstance(x, y);
		init(x, y); 				//initialize the location of the image
									//use your variables
	}
	
	public void setHealth(int health) {
		this.health = health;
		if(health > 0) {
			changePicture("/imgs/health_"+health+".png");
		}
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
	private void update() {
		tx.setToTranslation(x, y);
	}
	private void init(double a, double b) {
		tx.setToTranslation(a, b);
		tx.scale(2, 2);
	}

	public int getHealth() {
		return health;
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

	public void loseHP() {
		setHealth(health-1);
	}

	public boolean gameOver() {
		return(health <= 0);
	}

}
