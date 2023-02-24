package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.gameobjects.Projectile;
import com.mygdx.game.gameobjects.Tank;
import java.util.LinkedList;

public class CollisionListener implements ContactListener {
    private Tank tank1;
    private LinkedList projectiles1;
    private Tank tank2;
    private LinkedList projectiles2;
    
    public CollisionListener(Tank tank1, Tank tank2) {
        this.tank1 = tank1;
        this.projectiles1 = tank1.getProjectiles();
        this.tank2 = tank2;
        this.projectiles2 = tank2.getProjectiles();
    }
    
    @Override
    public void beginContact(Contact cntct) {
        if (cntct.getFixtureB().getFilterData().categoryBits == 8 && cntct.getFixtureB().getFilterData().maskBits == 2
                && cntct.getFixtureA().getFilterData().categoryBits == 2 && cntct.getFixtureA().getFilterData().maskBits == 8) {
            Projectile projectile = null;
            Tank currTank = null;
            if (cntct.getFixtureB().getUserData().equals(tank1.getColour())) {
                projectile = (Projectile) projectiles1.peekLast();
                
            } else if (cntct.getFixtureB().getUserData().equals(tank2.getColour())) {
                projectile = (Projectile) projectiles2.peekLast();
            }
            
            if (cntct.getFixtureA().getUserData().equals(tank1.getColour())) {
                currTank = tank1;
            } else if (cntct.getFixtureA().getUserData().equals(tank2.getColour())) {
                currTank = tank2;
            }
            
            if (projectile != null) {
                if (projectile.getIsAfterInitialCollision()) {
                    currTank.setIsAlive(false);
                    cntct.getFixtureB().getBody().setAwake(false);
                    if (currTank == tank1) {
                        projectiles1.remove(projectile);
                    } else if (currTank == tank2) {
                        projectiles2.remove(projectile);
                    }
                }
                projectile.afterInitialCollision();
            }
        }
    }

    @Override
    public void endContact(Contact cntct) {
    }

    @Override
    public void preSolve(Contact cntct, Manifold mnfld) {
    }

    @Override
    public void postSolve(Contact cntct, ContactImpulse ci) {
    }
    
}
