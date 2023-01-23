package com.mygdx.game.utils;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.mygdx.game.gameobjects.Tank;

public class Listener implements ContactListener {
    private Tank tank;
    
    public Listener(Tank tank) {
        this.tank = tank;
    }
    
    @Override
    public void beginContact(Contact cntct) {
         if (cntct.getFixtureA().getFilterData().categoryBits == 2 && cntct.getFixtureA().getFilterData().maskBits == 8
                || cntct.getFixtureA().getFilterData().categoryBits == 2 &&  cntct.getFixtureA().getFilterData().maskBits == 8) {
            tank.setAlive(false);
            if (cntct.getFixtureB().getFilterData().categoryBits == 8 && cntct.getFixtureB().getFilterData().maskBits == 2) {
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
