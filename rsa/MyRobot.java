package rsa;

import robocode.*;
import robocode.tma.TTeamLeaderRobot;
import robocode.util.Utils;
import java.awt.Color;
import java.io.IOException;
import java.io.Serializable;

import static robocode.util.Utils.normalRelativeAngleDegrees;


public class MyRobot extends TTeamLeaderRobot {
    boolean peek;
    double moveAmount;
    double enemyX;
    double enemyY;


    public MyRobot(){
    }

    @Override
    public void onRun() {
        RobotColors c = new RobotColors();

        c.bodyColor = Color.blue;
        c.gunColor = Color.blue;
        c.radarColor = Color.blue;
        c.scanColor = Color.yellow;
        c.bulletColor = Color.white;

        // Set the color of this robot containing the RobotColors
        setBodyColor(c.bodyColor);
        setGunColor(c.gunColor);
        setRadarColor(c.radarColor);
        setScanColor(c.scanColor);
        setBulletColor(c.bulletColor);

        try {
            // Send RobotColors object to our entire team
            broadcastMessage(c);
        } catch (IOException ignored) {}
        moveAmount = Math.max(getBattleFieldWidth(), getBattleFieldHeight());
        // Initialize peek to false
        peek = false;

        // turnLeft to face a wall.
        // getHeading() % 90 means the remainder of
        // getHeading() divided by 90.
        turnLeft(getHeading() % 90);
        ahead(moveAmount);
        // Turn the gun to turn right 90 degrees.
        peek = true;
        turnRight(180);
        while (true){
            setMaxVelocity(5);
            // Start moving (and turning)
            peek = true;
            ahead(moveAmount);
            peek = false;
            turnRight(180);
        }
    }

    @Override
    public void onStatus(StatusEvent e) {
        setTurnRadarRightRadians(Double.POSITIVE_INFINITY);
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // Don't fire on teammates
        if (isTeammate(e.getName())) {
            return;
        }
            double enemyBearing = this.getHeading() + e.getBearing();
            // Calculate enemy's position
            enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
            enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

            double dx = enemyX - this.getX();
            double dy = enemyY - this.getY();
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            // Fire hard!
            if(this.getEnergy() > 50){
                fire(3);
            }else if(this.getEnergy() <= 50){
                fire(0.5D);
            }

        try {
            // Send enemy position to teammates
            broadcastMessage(new Point(enemyX, enemyY));
        } catch (IOException ex) {
            out.println("Unable to send order: ");
            ex.printStackTrace(out);
        }
    }


    public void onHitRobot(HitRobotEvent e) {
        if (e.getBearing() > -90.0D && e.getBearing() < 90.0D) {
            if(isTeammate(e.getName())){
                this.turnRight(90);
            }else{
                this.back(100);
            }

        } else {
            if(isTeammate(e.getName())){
                this.turnRight(90);
            }else{
                this.ahead(100.0D);
            }
        }
    }

    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        if (e.getBearing() > -90.0D && e.getBearing() < 90.0D) {
            this.turnRight(90);
            this.back(100.0D);
        } else {
            this.turnRight(90);
            this.ahead(100.0D);
        }
    }
}