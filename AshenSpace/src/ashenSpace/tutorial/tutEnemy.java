package ashenSpace.tutorial;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import ashenSpace.GameState.Achievements;
import ashenSpace.anim.Texture;
import ashenSpace.audio.sound;
import ashenSpace.creature.DeadBasic;
import ashenSpace.game.Game;
import ashenSpace.game.GameObject;
import ashenSpace.game.HUD;
import ashenSpace.game.Handler;
import ashenSpace.game.ID;
import ashenSpace.object.Enemybullet;

public class tutEnemy extends GameObject{

	Handler handler;
	int BulletTimer = 0;
	Texture tex = Game.getInstance();
	
	public tutEnemy(float x, float y, ID id, Handler handler) {
		super(x, y, id);
		this.x = x;
		this.y = y;
		this.id = id;
		this.handler = handler;
		velY = 1;
		health = 100;
	}

	public void tick() {
		
		health = (int)Game.clamp(health, 0, 32);
		HUD.health = 75;
		tutHUD.health = 75;
		y += velY;
		
		BulletTimer++;
		if(BulletTimer >= 100){
			BulletTimer = 0;
			handler.addObject(new Enemybullet(x + 16, y + 16, ID.enemyBullet, handler));
			sound.play("/sound/Shoot.wav");

		}
		Collision();
		die();
		
		
	}

	public void render(Graphics g) {
		
		g.drawImage(tex.basicEnemy[0], (int) x, (int) y, null);
		g.setColor(Color.white);
		g.drawRect((int)x, (int) y - 5, health, 3);
		g.setColor(Color.red);
		g.fillRect((int) x, (int) y - 5, health, 3);
		
	}

	public Rectangle getBounds() {
		return new Rectangle((int) x, (int) y, 32, 32);
	}
	
	public void Collision(){
		
		for(int i = 0; i < handler.object.size(); i++){
			GameObject tempObject = handler.object.get(i);
			if(tempObject.getId() == ID.block){
				if(getBounds().intersects(tempObject.getBounds())){
					velY = 0;
					setY(getY() - 0.5f);
				}
			}
		}
	}
	
	public void die(){
		if(health <= 0){
			handler.removeObject(this);
			handler.addObject(new DeadBasic(this.x, this.y, ID.dead));
			Achievements.kills++;
			
			tutHUD.dialog = "Great! Monitors indicate you are stressed, at least. This is not good.";
			tutHUD.dialog2 = "Up ahead there is a medkit. Collect it to help your processes. TO use it,";
			tutHUD.dialog3 = "I have established a link between the 'h' command and your health. Try it.";
			tutHUD.dialog4 = "Also, reload your gun using the 'r' command. Good time as never.";
			

		}
	}

}
