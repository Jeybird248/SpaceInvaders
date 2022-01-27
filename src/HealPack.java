import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.net.URL;

public class HealPack {
	private Image img; 	
	private AffineTransform tx;
	int x, y;
	public HealPack(int x, int y) {
		img = getImage("imgs/healpack.png");
		this.x = x;
		this.y = y;
		tx = AffineTransform.getTranslateInstance(x, y);
		init(x, y);
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
		tx.scale(1, 1);
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
	public boolean checkCollision(Player player) {
		if((player.getX() >= x-15 && player.getX() <= x+30) && (player.getY() >= y-30 && player.getY() <= y+15)) {
			return true;
		}
		return false;
	}
}
