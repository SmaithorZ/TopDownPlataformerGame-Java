package com.harpystudios.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import com.harpystudios.entities.BulletShoot;
import com.harpystudios.entities.Enemy;
import com.harpystudios.entities.Entity;
import com.harpystudios.entities.Player;
import com.harpystudios.graficos.Spritesheet;
import com.harpystudios.graficos.UI;
import com.harpystudios.world.World;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Game extends Canvas implements Runnable, KeyListener  {
	
	private static final long serialVersionUID = 1L;
public static JFrame frame;	
   private Thread thread;
   private boolean isRunning = true;
	public static final int WIDTH = 240;
	public static final int 	HEIGHT = 160;
    public static final int SCALE = 3;
    
    private int CUR_LEVEL = 1, MAX_LEVEL = 2;
    private BufferedImage image;
   
   
     public static List<Entity> entities;
     public static List<Enemy> enemies;
     public static List<BulletShoot> bullets;
    public static Spritesheet spritesheet;
    
    public static World world;
    
    public static  Player player;
    
    public static Random rand;
    public UI ui;
    
    public InputStream stream = ClassLoader.getSystemClassLoader().getResourceAsStream("pixelfont.ttf");
    public Font newfont;
    
    public static String gameState = "NORMAL";
    private boolean showMessageGameOver = true;
    private int framesGameOver = 0;
    private boolean restartGame = false;
    public boolean saveGame = false;
    
    public Menu menu;
  
    public static BufferedImage minimapa;
    
    
     public Game() {
    	 rand = new Random();
	   addKeyListener(this); 
	setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
	    iniFrame();
	  //Inicializando objetos;
	 ui = new UI();      
    image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
    entities = new ArrayList<Entity>();
     enemies = new ArrayList<Enemy>();
     bullets = new ArrayList<BulletShoot>();
   
       spritesheet = new Spritesheet("/Sprite_Player.png");
        player = new Player(0,0,16,16,spritesheet.getSprite(0,0, 16, 16));
        entities.add(player);
        world = new World("/level1.png");
        
        
        menu = new Menu();
       /* 
        try {
			newfont = Font.createFont(Font.TRUETYPE_FONT,stream).deriveFont(20f);
		} catch (FontFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       */ 
   }
     
     public void iniFrame() {
    	 frame = new JFrame("Killer Girl");
         frame.add(this);
        frame.setResizable(false); 
        frame.pack();
        //�cone da janela
        Image imagem = null;
        try {
        	imagem = ImageIO.read(getClass().getResource("/icon.png"));
        }catch (IOException e) {
        	e.printStackTrace();
        }
        //CURSOR DO MOUSE
        //Toolkit toolkit = Toolkit.getDefaultToolkit();
        //Image image = toolkit.getImage(getClass().getResource("/icon1.png"));
       // Cursor c = toolkit.createCustomCursor(image,new Point(0,0), "img");
       //frame.setCursor(c);
        frame.setIconImage(imagem);
        frame.setAlwaysOnTop(true);
         frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
     }
     
     
     public synchronized void start() {
    	 thread = new Thread(this);
    	 thread.start();
    	 isRunning = true;
    }
     
     public synchronized void stop() {
    	 
    	 isRunning = false;
    	 try {
			thread.join();
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		} 
    	 
     }
     
public static void main(String []args) {
	Game game = new Game();
	game.start();
	
	
	
}
    public void tick()	{
    	if(gameState == "NORMAL") {
    		if(this.saveGame) {
    			this.saveGame = false;
    			String[] opt1 = {"level"};
    			int[] opt2 = {this.CUR_LEVEL};
    			Menu.saveGame(opt1,opt2,10);
    			System.out.println("Jogo Salvo Com Sucesso");
    		}
    		this.restartGame = false;
    	for(int i = 0; i < entities.size(); i++) {
    		Entity e = entities.get(i);
    		e.tick();
    		
    		
    	}
    	
    	for(int i = 0; i < bullets.size(); i++) {
    		bullets.get(i).tick();
    		
    	}
    	
    	if(enemies.size() == 0) {
    		//Avan�ar para o pr�ximo level!
    		CUR_LEVEL++;
    		if(CUR_LEVEL > MAX_LEVEL) {
    			CUR_LEVEL = 1;
    		}
    		String newWorld = "level"+CUR_LEVEL+".png";
    		World.restartGame(newWorld);
    		System.out.println("reiniciando");
    	}
    	
    	
    	}else if(gameState == "GAME_OVER") {
    		this.framesGameOver++;
    		if(this.framesGameOver == 35) {
    			this.framesGameOver = 0;
    			if(this.showMessageGameOver)
    				this.showMessageGameOver = false;
    			else
    				this.showMessageGameOver = true;
    		}
    		if(restartGame) {
    			this.restartGame = false;
    			gameState = "NORMAL";
    			CUR_LEVEL = 1;
    			String newWorld = "level"+CUR_LEVEL+".png";
        		World.restartGame(newWorld);
        		//System.out.println("reiniciando");
    		}else if(gameState == "MENU") {
    			//
    			menu.tick();
    		}
    	}
    }
   
    
   
    
    
    public void render() {
    	BufferStrategy bs = this.getBufferStrategy();
    	
    	if(bs == null) {
    		
    		this.createBufferStrategy(3);
    		return;
    		
    	
    		
    		
    	}
    	
    	Graphics g = image.getGraphics();
    	g.setColor(new Color(0,0,0));
    	g.fillRect(0,0,WIDTH,HEIGHT);
    	g.setColor(Color.GREEN);	
    	/*RENDERIZA��O DO JOGO*/
    	
    	//Graphics2D g2 = (Graphics2D) g;
    	world.render(g);
    	for(int i = 0; i < entities.size(); i++) {
    		Entity e = entities.get(i);
    		e.render(g);
    	}
    	
    	for(int i = 0; i < bullets.size(); i++) {
    		bullets.get(i).render(g);
    		
    	}
    	ui.render(g);
    	/****/
    	g.dispose();
    	g = bs.getDrawGraphics();
    g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
    	g.setFont(new Font("arial",Font.BOLD,18));
    	g.setFont(newfont);
    	g.setColor(Color.white);
    	g.drawString("AMMO: "+player.ammo, 600, 20);
    	if(gameState == "GAME_OVER") {
    		Graphics2D g2 = (Graphics2D) g;
    		g2.setColor(new Color(0,0,0,100));
    		g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
    		g.setFont(new Font("arial",Font.BOLD,40));
        	g.setColor(Color.white);
        	g.drawString("GAME OVER", 240, 270);
        	g.setFont(new Font("arial",Font.BOLD,20));
        	if(showMessageGameOver)
        	g.drawString(">Pressione ENTER para reiniciar<", 230, 300);
    	}else if(gameState == "MENU") {
    		menu.render(g);
    	}
    	
    	bs.show();
    	
    	
    }
    
    
	public void run() {
		Sound.music.play();
		requestFocus();
		long Lastime = System.nanoTime();
		double amountsOfTicks = 60.0;
		double ns = 1000000000 / amountsOfTicks;
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning) {
			long now = System.nanoTime();
			delta += (now - Lastime) / ns;
			Lastime = now;
			if(delta >= 1) {
				tick();
				render();
				frames++;
				delta --;
			}
			
			if(System.currentTimeMillis() - timer >= 1000) {
				
				System.out.println("FPS:"+frames);
				frames = 0;
				timer +=1000;
			}
			
			
		}
		stop();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
	
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
		if(gameState == "MENU") {
			menu.down = true;
		}
			
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X) {
			player.shoot = true;
		}
		
		if(e.getKeyChar() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "NORMAL")
			this.saveGame = true;
			
		}
		
	}
	

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT ||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT ||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
	
		if(e.getKeyCode() == KeyEvent.VK_UP ||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
			
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN ||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
			
		}	
		
	}

	

	


	}

