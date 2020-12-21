package brickBreaker;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JPanel;
import javax.swing.Timer;

public class PlayGame extends JPanel implements KeyListener, ActionListener{

	private boolean play = false;
	private int score = 0;
	private int totalBricks = 35;
	
	private Timer timer;
	private int delay = 6;
	
	private int playerX = 310;
	
	private int ballposX = 420;
	private int ballposY = 350;
	private int ballXdir = -1;
	private int ballYdir = -2;
	
	private MapGenerator map;
	
	 PlayGame() {
		 map = new MapGenerator(5, 7);
		addKeyListener(this);
		setFocusable(true);
		requestFocus();
		setFocusTraversalKeysEnabled(false);
		timer = new Timer(delay, this);
		timer.start();
	}
	
	public void paint(Graphics g) {
		
		//background
		g.setColor(Color.gray);
		g.fillRect(1, 1, 692, 592);
		
		//draw bricks
		map.draw((Graphics2D)g);
		
		//borders
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, 3, 592);
		g.fillRect(0, 0, 692, 3);
		g.fillRect(691, 0, 3, 592);
		
		//display scores
		g.setColor(Color.black);
		g.setFont(new Font("serif", Font.BOLD, 25));
		g.drawString("Score : " + score, 500, 30);
		
		//paddle
		g.setColor(Color.DARK_GRAY);
		g.fillRect(playerX, 550, 100, 8);
		
		//the ball
		g.setColor(Color.red);
		g.fillOval(ballposX, ballposY, 20, 20);
		
		if(totalBricks <= 0) {

			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("You won ", 260, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter to Restart ", 230, 350);
		
		}
		
		// if ball goes down
		if(ballposY > 570) {
			play = false;
			ballXdir = 0;
			ballYdir = 0;
			g.setColor(Color.RED);
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Game Over", 190, 300);
			
			g.setFont(new Font("serif", Font.BOLD, 30));
			g.drawString("Press Enter to Restart ", 230, 350);
		}
		
		
		
		g.dispose();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		timer.start();
		
		if(play) {
			
			if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8))) {
				ballYdir = - ballYdir;
			}
			
			A : for(int i = 0; i < map.map.length; i ++) {
				for(int j = 0; j < map.map[0].length; j ++) {
					if(map.map[i][j] > 0) {
						int brickX = j * map.brickWidth + 80;
						int brickY = i * map.brickHeight + 50;
						int brickWidth = map.brickWidth;
						int brickHeight = map.brickHeight;
						
						Rectangle rect = new Rectangle(brickX, brickY, brickWidth, brickHeight);
						Rectangle ballRect = new Rectangle(ballposX, ballposY, 20, 20);
						Rectangle brickRect = rect;
						
						if(ballRect.intersects(brickRect)) {
							map.setBrickValue(0, i, j);
							totalBricks -= 1;
							score += 5;
							
							if(ballposX + 19 <= brickRect.x || ballposY + 1 >= brickRect.x + brickRect.width) {
								ballXdir = - ballXdir;
							} else {
								ballYdir = - ballYdir;
							}
							
							break A;
						}
					}
				}
			}
			ballposX += ballXdir;
			ballposY += ballYdir;
			
			if(ballposX < 0) {
				ballXdir = - ballXdir;
			}
			
			if(ballposY < 0) {
				ballYdir = - ballYdir;
			}
			
			if(ballposX > 670) {
				ballXdir = - ballXdir;
			}
		}
		repaint();
	}
	
	@Override
	public void keyReleased(KeyEvent e) {}

	@Override
	public void keyTyped(KeyEvent e) {}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			if(playerX >= 600) {
				playerX = 600;
			} else {
				moveRight();
			}	
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT) {
			if(playerX < 10) {
				playerX = 10;
			} else {
				moveLeft();
			}			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			if(! play) {
				play = true;
				ballposX = 120;
				ballposY = 350;
				ballXdir = -1;
				ballYdir = -2;
				playerX = 310;
				score = 0;
				totalBricks = 35;
				map = new MapGenerator(5, 7);
				
				repaint();
			}
			
		}
		
	}
	
	public void moveRight() {
		play = true;
		playerX += 20;
	}
	public void moveLeft() {
		play = true;
		playerX -= 20;
	}



}

