package com.harpystudios.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.harpystudios.main.Game;

public class Tile {

	public static BufferedImage TILE_FLOOR = Game.spritesheet.getSprite(112, 144, 16, 16);
	public static BufferedImage TILE_WALL = Game.spritesheet.getSprite(144, 144, 16, 16);
	
	
	private BufferedImage sprite;
	private int x,y;
	
	
	public Tile(int x, int y, BufferedImage sprite) {
		
		this.x = x;
		this.y = y;
		this.sprite = sprite;		
		
	}
	
	
	
	public void render(Graphics g) {
		
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		
		
	}
	
	
}
