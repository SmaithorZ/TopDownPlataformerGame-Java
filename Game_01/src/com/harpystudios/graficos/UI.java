package com.harpystudios.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.harpystudios.entities.Player;
import com.harpystudios.main.Game;

public class UI {

	
	
   public void render(Graphics g) {
    g.setColor(Color.red);	
	g.fillRect(2,2,50,6);	
	g.setColor(Color.green);	
	g.fillRect(2, 2,(int)((Game.player.life/Game.player.maxLife)*50), 6);
	g.setColor(Color.white);
	g.setFont(new Font("arial", Font.BOLD,8));
	g.drawString((int)Game.player.life+"/"+(int)Game.player.maxLife, 12, 8);
		
	}
	
}
