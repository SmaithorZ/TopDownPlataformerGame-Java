package com.harpystudios.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.harpystudios.world.World;

public class Menu {

      public String[] options = {"Novo Jogo","Carregar Jogo","Sair", "Continuar"};
      
      public int currentOption = 0;
      public int maxOption = options.length -1;
      
      public boolean up,down,enter;
     	
      public static boolean pause = false;
      public static boolean saveExists = false;
      public static boolean saveGame = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
				
		}else {
			saveExists = false;
		}
		if(up){
			up = false;
		 currentOption--;
		 if(currentOption < 0)
			 currentOption = maxOption;
			 
		}
		if(down) {
			down = false;
			currentOption++;
			if(currentOption > maxOption)
				currentOption = 0;
		}
		if(enter) {
			enter = false;
			if(options[currentOption] == "Novo Jogo" || options[currentOption] == "Continuar") {
				Game.gameState = "NORMAL";
				pause = false;
				 file = new File("save.txt");
				file.delete();
				String saver = loadGame(10);
				applySave(saver);
			}else if(options[currentOption] == "Carregar Jogo") {
				file = new File("save.txt");
				if(file.exists()) {
					
				}
			}else if(options[currentOption] == "Sair") {
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) {
			case "level":
				World.restartGame("level"+spl2[1]+".png");
				Game.gameState = "NORMAL";
				pause = false;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
	    File file = new File("save.txt"); 
	    if(file.exists()) {
	    	try {
	    		String singleLine = null;
	    		BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
	    		try {
	    			while((singleLine = reader.readLine()) != null) {
	    				String[] trans = singleLine.split(":");
	    				char[] val = trans[1].toCharArray();
	    				trans[1] = "";
	    				for(int i = 0; i < val.length; i++) {
	    					val[i] -= encode;
	    					trans[1] += val[i];
	    				}
	    				line+= trans[0];
	    				line+= ":";
	    				line+= trans[1];
	    				line+= "/";
	    			}
	    		}catch(IOException e) {}
	    	}catch(FileNotFoundException e) {}
	    	
	    	
	    }
	    return line;
	 
	}
	
	public static void saveGame(String[] val1, int[]val2, int encode) {
		
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {
			e.printStackTrace();
		}
		for(int i = 0; i <  val1.length; i++) {
			String current = val1[i];
			current +=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode;
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1)
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e) {}
	}
	
	public void render(Graphics g) {
		g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE);
		g.setColor(Color.RED);
		g.setFont(new Font("arial",Font.BOLD,36));
		g.drawString(">Harpy<",(Game.WIDTH*Game.SCALE)/2 - 87,(Game.HEIGHT*Game.SCALE)/2 - 180);
		
		//Opções de Menu
		g.setColor(Color.WHITE);
		g.setFont(new Font("arial",Font.BOLD,27));
		g.drawString("Novo Jogo",(Game.WIDTH*Game.SCALE)/2 - 80,100);
		g.drawString("Carregar Jogo",(Game.WIDTH*Game.SCALE)/2 - 97,140);
		g.drawString("Sair",(Game.WIDTH*Game.SCALE)/2 - 35,187);
		
		if(options[currentOption] == "Novo Jogo") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 100,100);
		}else if(options[currentOption] == "Carregar Jogo") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 100,140);
		}else if(options[currentOption] == "Sair") {
			g.drawString(">",(Game.WIDTH*Game.SCALE)/2 - 100,187);
		}
		
		
	
}
}