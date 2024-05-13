package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

/**
 *
 * @author Emil
 */
public class Player extends Entity {

    private long lastShot;
    public Player(){
        this.setRotationSpeed(400);
        this.setForwardSpeed(100);
        this.lastShot = 0;
    }

    public boolean canShoot(long now){
        boolean res = (now - lastShot) > 1_000_000_000/6; //six times a second
        if(res) lastShot = now;
        return res;
    }

}
