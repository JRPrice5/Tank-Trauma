package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.gameobjects.Projectile;
import com.mygdx.game.gameobjects.Tank;
import java.util.LinkedList;

public class Listener implements ContactListener {
    private Tank tank1;
    private LinkedList projectiles1;
    private Tank tank2;
    private LinkedList projectiles2;
    
    public Listener(Tank tank1, LinkedList projectiles1, Tank tank2, LinkedList projectiles2) {
        this.tank1 = tank1;
        this.projectiles1 = projectiles1;
        this.tank2 = tank2;
        this.projectiles2 = projectiles2;
    }
    
    @Override
    public void beginContact(Contact cntct) {
        Fixture FixtureA = cntct.getFixtureA();
        Fixture FixtureB = cntct.getFixtureB();
        if (cntct.getFixtureB().getFilterData().categoryBits == 8 && cntct.getFixtureB().getFilterData().maskBits == 2
                && cntct.getFixtureA().getFilterData().categoryBits == 2 && cntct.getFixtureA().getFilterData().maskBits == 8) {
            Projectile projectile = null;
            Tank currTank = null;
            if (cntct.getFixtureA().getUserData().equals(tank1.getColour())) {
                projectile = (Projectile) projectiles1.peekLast();
                currTank = tank1;
            } else if (cntct.getFixtureA().getUserData().equals(tank2.getColour())) {
                projectile = (Projectile) projectiles2.peekLast();
                currTank = tank2;
            } 
            projectile.incrementCollisionCount();
            if (projectile.getCollisionCount() >= 2) {
                currTank.setAlive(false);
                cntct.getFixtureB().getBody().setAwake(false);
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
