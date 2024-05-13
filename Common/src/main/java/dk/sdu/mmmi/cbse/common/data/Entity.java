package dk.sdu.mmmi.cbse.common.data;

import java.io.Serializable;
import java.util.UUID;

public class Entity implements Serializable {

    private final UUID ID = UUID.randomUUID();
    
    private double[] polygonCoordinates;
    private double x;
    private double y;
    private double rotation;
    private float radius;
    private double heading;

    private int rotationSpeed;
    private int forwardSpeed;

    public String getID() {
        return ID.toString();
    }


    public void setPolygonCoordinates(double... coordinates ) {
        this.polygonCoordinates = coordinates;
    }

    public double[] getPolygonCoordinates() {
        return polygonCoordinates;
    }
       

    public void setX(double x) {
        this.x =x;
    }

    public double getX() {
        return x;
    }

    
    public void setY(double y) {
        this.y = y;
    }

    public double getY() {
        return y;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }
        
    public float getRadius() {
        return this.radius;
    }

    public void setForwardSpeed(int forwardSpeed) {
        this.forwardSpeed = forwardSpeed;
    }

    public int getForwardSpeed() {
        return forwardSpeed;
    }

    public void setRotationSpeed(int rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }

    public int getRotationSpeed() {
        return rotationSpeed;
    }

    public void setHeading(double heading) {
        this.heading = heading;
    }

    public double getHeading() {
        return heading;
    }

    public void rotate(long delta, boolean right){
        double rate = this.rotationSpeed * ((double) delta /1_000_000_000);
        if(right) this.rotation += rate;
        else this.rotation -= rate;
    }

    public void forward(long delta){
        double rate = this.forwardSpeed * ((double) delta /1_000_000_000);
        double radians = Math.toRadians(this.heading);
        this.y += Math.sin(radians) * rate;
        this.x += Math.cos(radians) * rate;
    }

    public void forceMove(int dist){
        double radians = Math.toRadians(this.heading);
        this.y += Math.sin(radians) * dist;
        this.x += Math.cos(radians) * dist;
    }
}
