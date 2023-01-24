package com.harpystudios.entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.harpystudios.graficos.Spritesheet;
import com.harpystudios.main.Game;
import com.harpystudios.world.Camera;
import com.harpystudios.world.World;

public class Player extends Entity {
	
	
	public boolean right, up,left, down;
	public int right_dir = 0,left_dir = 1;
	public int dir = right_dir;
	
	public double speed = 0.9;
	
	private int frames = 0, maxFrames = 5, index = 0,maxIndex = 3;
	private boolean moved = false;
   private BufferedImage[] rightPlayer;
   private BufferedImage[] leftPlayer;
   
   private BufferedImage playerDamage;
   
   private boolean hasGun = false;
   
   public int ammo = 0;
   int dx = 0;
   int dy = 0;
   int px = 0;
   int py = 8;
   
   public boolean isDamaged = false;
   private int damageFrames = 0;
  
   public boolean shoot = false;
   
   public double life = 100,maxLife = 100;
   
   
	
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		playerDamage = Game.spritesheet.getSprite(64, 0, 16, 16);
	
		
		
		for(int i =0; i < 4; i++ ) {
		rightPlayer[i] = Game.spritesheet.getSprite(0+ (i*16), 0, 16, 16);
			
		}
		
		for(int i =0; i < 4; i++ ) {
			leftPlayer[i] = Game.spritesheet.getSprite(0+ (i*16), 16, 16, 16);
				
			}
		
		
		
		
		
	}
      public void tick() {
	     moved = false;
	     if(right && World.isFree((int)(x+speed),this.getY())) {
	    	 moved = true;
	    	 dir = right_dir;
	      x+=speed;
	     }	
	     else if(left && World.isFree((int)(x-speed),this.getY())) {
	    	 moved = true;
	    	 dir = left_dir;
	    	x-=speed;
	    }
	  if(up && World.isFree(this.getX(),(int)(y-speed))) {
		  moved = true;
		  y-=speed;
	  }
	  else if(down && World.isFree(this.getX(),(int)(y+speed))) {
		  moved = true;
		  y+=speed;
	  }
	       	 
    	
	  if(moved) {
		  frames++;
		  if(frames == maxFrames) {
			  frames = 0;
			  index++;
			  if(index > maxIndex) {
				  index = 0;
				  
			  }
		  }
	  }
	 
	  this.checkCollisionLifePack();
	  this.checkCollisionAmmo();
	  this.checkCollisionGun();
	 
	  if(isDamaged) {
		  this.damageFrames++;
		  if(this.damageFrames == 8) {
			  this.damageFrames = 0;
			  isDamaged = false;
		  }
	  }
	 
	  if(shoot) {
		  shoot = false;
		  if(hasGun && ammo > 0) {
		  ammo--;
		  //Criar projétil e atirar
		  
		  
		  if(dir == right_dir) {
	          px = 8;
			  dx = 2;
                			  
	  }else {
		   dx = -1;
		   px = -3;
	  }
		  
	  }
		  
		  
		  BulletShoot bullet = new BulletShoot(this.getX() + px, this.getY() + py, 3, 3, null, dx, 0);
			Game.bullets.add(bullet);  
		  
		  
	  }
     
     
		  
		  
		    
		  
		  
	  
      
	  
	  
	  if(life<= 0) {
		  life = 0;
		  //GAME OVER!
		   Game.gameState = "GAME_OVER"; 
	  }
	  
	  Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH);
	  Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
      }
	
      public void checkCollisionGun() {
  		
 		 for(int i = 0; i < Game.entities.size(); i++){
 			  
 			  Entity atual = Game.entities.get(i);
 			  if(atual instanceof Weapon) {
 				  if(Entity.isColliding(this, atual)) {
 					  
 					  hasGun = true;
 					 //System.out.println("Pegou a arma!");
 					  Game.entities.remove(atual);
 		 
 	                }
 				  }
 			  }
 	  
    } 
	 public void checkCollisionAmmo() {
		
		 for(int i = 0; i < Game.entities.size(); i++){
			  
			  Entity atual = Game.entities.get(i);
			  if(atual instanceof Bullet) {
				  if(Entity.isColliding(this, atual)) {
					  
					  ammo+=150;
					 //System.out.println("Munição atual: "+ ammo); 
					  Game.entities.remove(atual);
		 
	                }
				  }
			  }
	  
   }
    
     
	 
	 
   public void checkCollisionLifePack() {
	   
	  for(int i = 0; i < Game.entities.size(); i++){
		  
		  Entity atual = Game.entities.get(i);
		  if(atual instanceof Lifepack) {
			  if(Entity.isColliding(this, atual)) {
				  
				  life+=50;
				  if(life > 100)
					  life = 100;
				  Game.entities.remove(atual);
			  }
			  
		  }
		  
	  }
	   
	   
	   
	   
   }
      
      
   public void render(Graphics g) {
	   if(!isDamaged) {
	   if(dir == right_dir) {
	   g.drawImage(rightPlayer[index],this.getX() - Camera.x, this.getY() - Camera.y,null);
	   if(hasGun) {
		   //Desenhar arma para direita
		   g.drawImage(Entity.WEAPON_RIGHT,this.getX()+2 - Camera.x,this.getY()-0 - Camera.y,null);
	   }
	   }else if(dir == left_dir) {
		   g.drawImage(leftPlayer[index],this.getX() - Camera.x, this.getY() - Camera.y,null);
		   if(hasGun) {
			   //Desenhar arma para esquerda
			   g.drawImage(Entity.WEAPON_LEFT,this.getX()-2 - Camera.x,this.getY()-1 - Camera.y,null);
		   }
	    
	   }
	   
	   }else {
		   g.drawImage(playerDamage,this.getX() - Camera.x, this.getY() - Camera.y, null);
		  if(hasGun) {
			  if(dir == left_dir) {
				  g.drawImage(Entity.WEAPON_LEFT, this.getX()-2 - Camera.x, this.getY()-1 - Camera.y,null);
			  }else {
				  g.drawImage(Entity.WEAPON_RIGHT, this.getX()+2 - Camera.x, this.getY()-0 - Camera.y, null);
				  
			  }
		   
		   
		   
		   
		   
	   }
	   }
	 
	   
	   
   }
}      

