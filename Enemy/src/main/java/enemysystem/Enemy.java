package enemysystem;

import dk.sdu.mmmi.cbse.common.data.Entity;

import java.util.Random;

public class Enemy extends Entity {
    private final Random rand;
    private long lastShot;
    public Enemy(){
        rand = new Random();
        this.setRotationSpeed(300);
        this.setForwardSpeed(100);
        this.lastShot = 0;
    }

    public Random getRand() {
        return rand;
    }

    public boolean canShoot(long now){
        boolean res = (now - lastShot) > 1_000_000_000/2; //twice a second
        if(res) lastShot = now;
        return res;
    }
}
