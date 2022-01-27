import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Frame extends JPanel implements KeyListener, ActionListener, MouseListener{
	int score = 0;
	int counter = 0;
	int enemyCounter = 0;
	int healPackCounter = 0;
	int level = 1;
	int randomNum;
	boolean wallRemoved;
	boolean scoreAdded = false;
	int enemySpawnX = 0;
	int enemySpawnY = 75;
	int nukeCounter = 0;
	int nukeTimer = 0;
	int highestScore;
	Timer t;
	ArrayList<Wall> walls = new ArrayList<Wall>();
	ArrayList<Bullet> bullet = new ArrayList<Bullet>();
	ArrayList<Bullet> enemyBullet = new ArrayList<Bullet>();
	Health health = new Health(50, 650);
	ArrayList<Alien> aliens = new ArrayList<Alien>();
	Player player = new Player(200, 550);	
	ArrayList<HealPack> healPack = new ArrayList<HealPack>();
	ArrayList<Nuke> nuke = new ArrayList<Nuke>();
	Nuke nukeCount = new Nuke();
	static Music music = new Music("bgm.wav", true);
	static Music pew = new Music("pew.wav", false);
	static Music heal = new Music("heal.wav", false);
	
	public void paint(Graphics g) {
		super.paintComponent(g);
		InputStream myFile = Frame.class.getResourceAsStream("/fonts/PressStart2P.ttf");
		try {
			g.setFont(Font.createFont(Font.TRUETYPE_FONT, myFile).deriveFont(Font.BOLD, 12F));
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(health.gameOver()) {
			//show gameover elements, score and retry text
			try {
				FileReader reader = new FileReader("scores.txt");
				FileWriter writer = new FileWriter("scores.txt", true);
				BufferedReader bufferedReader = new BufferedReader(reader);
				if(scoreAdded == false) {
					writer.write("\n"+ score);
					scoreAdded = true;
				}
	            String line;
	            while ((line = bufferedReader.readLine()) != null) {
	                if(highestScore <= Integer.valueOf(line)) {
	                	highestScore = Integer.valueOf(line);
	                }
	            }
	            reader.close();
	            writer.close();
			} catch (IOException e) {
		        e.printStackTrace(); 
		        // handle exception correctly.
		    }
			
			for(int i = 0; i < aliens.size(); i++) {
				aliens.remove(i);
			}
			for(int i = 0; i < walls.size(); i++) {
				walls.remove(i);
			}
			for(int i = 0; i < bullet.size(); i++) {
				bullet.remove(i);
			}
			for(int i = 0; i < healPack.size(); i++) {
				healPack.remove(i);
			}
			g.drawString("Game Over", 100, 100);
			g.drawString("Your Score was: " + score, 100, 150);
			

			g.drawString("The Highest Score was: " + highestScore, 10, 200);
			if(highestScore == score) {
				g.drawString("NEW HIGH SCORE", 100, 350);
			}
			g.setColor(Color.RED);
			g.fillRect(100, 400, 200, 100);
			g.setColor(Color.BLACK);;
			g.drawString("RETRY?", 150, 450);
		}
		else {
			scoreAdded = false;
			g.setColor(Color.BLACK);
			g.fillRect(0, 0, 400, 800);
			g.setColor(Color.WHITE);
			
			counter++;
			enemyCounter++;
			player.paint(g);
			g.drawString("Score: \n" + String.valueOf(score), 50, 50);
			g.drawString("Level: "+ String.valueOf(level), 250, 50);
			health.paint(g);
			if(nukeCounter == 1) {
				nukeCount.paint(g);
			}
			for(int i = 0; i < bullet.size(); i++) {
				bullet.get(i).paint(g);
				if(bullet.get(i).getY() <= 70) {
					bullet.remove(i);
				}
			}
			for(int i = 0; i < aliens.size(); i++) {
				aliens.get(i).paint(g);
				if (aliens.get(i).getDeadCounter() >= 3) {
					aliens.remove(i);
					continue;
				}
				if(aliens.get(i).getY() >= 670) {
					health.loseHP();
					aliens.remove(i);
					break;
				}
				for(int k = 0; k < level*2 && enemyCounter >= 10; k++) {
					randomNum = (int) (Math.random()*(aliens.size()));
					enemyBullet.add(new Bullet(aliens.get(randomNum).getX()+10, aliens.get(randomNum).getY(), -20));
					enemyCounter = 0;
				}
				for(int l = 0; l < enemyBullet.size(); l++) {
					if(player.checkCollision(enemyBullet.get(l))) {
						health.loseHP();
						enemyBullet.remove(l);
						break;
					}
				}
				for(int j = 0; j < bullet.size(); j++) {
					if(aliens.get(i).checkCollision(bullet.get(j))) {
						score+=50;
						bullet.remove(j);
						break;
						
					}
					
					
				}
			}
			for(int i = 0; i < enemyBullet.size(); i++) {
				enemyBullet.get(i).paint(g);
				if(enemyBullet.get(i).getY() >= 600) {
					enemyBullet.remove(i);
				}
			}
			for(int i = 0; i < walls.size();i++) {
				walls.get(i).paint(g);
				for(int j = 0; j < bullet.size(); j++) {
					if(walls.get(i).checkCollision(bullet.get(j))) {
						walls.get(i).setHealth(walls.get(i).getHealth()-1);
						bullet.remove(j);
						if(walls.get(i).getHealth() <= 0) {
							walls.remove(i);
							wallRemoved = true;
							break;
						}
					}
				}
				if(wallRemoved) {
					wallRemoved = false;
					break;
				}
				for(int j = 0; j < enemyBullet.size(); j++) {
					if(walls.size() > 0) {
						if(walls.get(i).checkCollision(enemyBullet.get(j))) {
							walls.get(i).setHealth(walls.get(i).getHealth()-1);
							enemyBullet.remove(j);
							if(walls.get(i).getHealth() <= 0) {
								walls.remove(i);
								wallRemoved = true;
								break;
							}
						}
					}
				}
				if(wallRemoved) {
					wallRemoved = false;
					break;
				}
			}
			for(int i = 0; i < healPack.size(); i++) {
				healPack.get(i).paint(g);
				if(healPack.get(i).checkCollision(player) && health.getHealth() <= 2) {
					health.setHealth(health.getHealth()+1);
					healPack.remove(i);
					heal.play();
				}
			}
			
			for(int i = 0; i < nuke.size(); i++) {
				nuke.get(i).paint(g);
				if(nuke.get(i).checkCollision(player)) {
					nuke.remove(i);
					nukeCounter++;
				}
			}
			
			healPackCounter++;
			nukeTimer++;
			if(healPackCounter >= 200) {
				healPack.add(new HealPack( (int)(Math.random()*370) , 550));
				healPackCounter = 0;
			}
			if(nukeTimer >= 300 & nukeCounter == 0) {
				nuke.add(new Nuke((int)(Math.random()*370) , 550));
				nukeTimer = 0;
			}
			
			if(aliens.size() == 0) {
				level++;
				for(int i = 0; i < level*7; i++) {
					aliens.add(new Alien(enemySpawnX, enemySpawnY, 5));
					if (enemySpawnX >= 340) {
						enemySpawnY += 50;
						enemySpawnX = 0;
					}
					else {
						enemySpawnX += 50;
					}
				}
				enemySpawnX = 0;
				enemySpawnY = 75;
			}
			
			//prevent player from leaving screen
			if(player.getX()<10) {
				player.setX(0);
			}
			if(player.getX() >= 360) {
				player.setX(360);
			}
		}
		
	}
	
	public Frame() {
		JFrame f = new JFrame("Space Invaders");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(400,800);
		f.add(this);
		f.addKeyListener(this);	
		f.addMouseListener(this);
		t = new Timer(50, this);
		t.start();
		f.setVisible(true);
		walls.add(new Wall(30));
		walls.add(new Wall(165));
		walls.add(new Wall(320));
		for(int i = 0; i < level*7; i++) {
			aliens.add(new Alien(enemySpawnX, enemySpawnY, 5));
			if (enemySpawnX >= 340) {
				enemySpawnY += 50;
				enemySpawnX = 0;
			}
			else {
				enemySpawnX += 50;
			}
		}
		enemySpawnX = 0;
		enemySpawnY = 75;
	}
	
	public static void main(String[] arg) {
		Frame f = new Frame();
		music.play();
	}	
	public void reset() {
		score = 0;
		level = 1;
		nukeCounter = 0;
		nukeTimer = 0;
		walls.add(new Wall(30));
		walls.add(new Wall(165));
		walls.add(new Wall(320));
		for(int i = 0; i < level*7; i++) {
			aliens.add(new Alien(enemySpawnX, enemySpawnY, 5));
			if (enemySpawnX >= 340) {
				enemySpawnY += 50;
				enemySpawnX = 0;
			}
			else {
				enemySpawnX += 50;
			}
		}
		healPackCounter = 0;
		enemySpawnX = 0;
		enemySpawnY = 75;
		health.setHealth(3);
	}
	
	public void wipe() {
		 for(int i = 0; i < aliens.size(); i++) {
			aliens.remove(i);
			score+=50;
		}
		nukeCounter--;
	}
	@Override
	public void keyPressed(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int key = arg0.getKeyCode(); //code for what is being pressed
		//prevent paddles from leaving boundary
		if (key == KeyEvent.VK_LEFT && player.getX() > 10) {
			player.setSpeedX(-20);
		}
		else if (key == KeyEvent.VK_RIGHT && player.getX() < 310) {
			player.setSpeedX(20);
		}
		if (key == KeyEvent.VK_SPACE && counter >= 5) {
			bullet.add(new Bullet(player.getX()+15, 550, 20));
			counter = 0;
			pew.play();
		}
		if (key == KeyEvent.VK_ENTER && nukeCounter >= 1) {
			wipe();
		}
	}	
	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		int key = arg0.getKeyCode();
		//stop moving paddle when key is released
		if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
			player.setSpeedX(0);
		}
	}
	@Override
	public void actionPerformed(ActionEvent arg0) {
		repaint();
	}
	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		System.out.println("key pressed at (" + e.getX() + ", " + e.getY()+")");
		if(health.gameOver() && e.getX() > 100 && e.getX() < 300 && e.getY() > 430 && e.getY() < 530) {
			reset();
			System.out.println("reset pressed");
		}
		
	}
}
