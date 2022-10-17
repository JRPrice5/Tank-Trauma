package com.mygdx.game.sprites;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Tank {
    private Vector3 position;
    private Vector3 velocity;
    private int speed;
    private Texture tank;
    private Texture turret;
    
    public Tank(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        speed = 100;
        tank = new Texture("playbtn.png");
        turret = new Texture("gameover.png");
    }
    
    public void update(float dt) {
        if(Gdx.input.isKeyPressed(Keys.W)) {
            velocity.add(0, speed, 0);
        }
        if(Gdx.input.isKeyPressed(Keys.A)) {
            
        }
        if(Gdx.input.isKeyPressed(Keys.S)) {
            velocity.add(0, -speed, 0);
        }
        if(Gdx.input.isKeyPressed(Keys.D)) {
            
        }
        velocity.scl(dt);
        position.add(velocity.x, velocity.y, 0);
        if(position.y < 0)
            position.y = 0;
        if(position.x < 0)
            position.x = 0;
        velocity.scl(1/dt);
        velocity.scl(dt);
    }
    
    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return tank;
    }
}
