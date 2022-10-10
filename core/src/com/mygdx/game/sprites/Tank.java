package com.mygdx.game.sprites;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;

public class Tank {
    private Vector3 position;
    private Vector3 velocity;
    
    private Texture tank;
    
    public Tank(int x, int y) {
        position = new Vector3(x, y, 0);
        velocity = new Vector3(0, 0, 0);
        //tank = new Texture("tank.png");
    }
    
    public void update(float dt) {
//        if(position.y > 0)
//            velocity.add(0, 0, 0);
//        velocity.scl(dt);
//        position.add(velocity.x, velocity.y, 0);
//        if(position.y < 0)
//            position.y = 0;
//        if(position.x < 0)
//            position.x = 0;
//        velocity.scl(1/dt);
        // change the position and velocity of the tank, depending on input
    }
    
    public Vector3 getPosition() {
        return position;
    }

    public Texture getTexture() {
        return tank;
    }
}
