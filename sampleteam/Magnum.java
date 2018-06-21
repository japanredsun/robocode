/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sampleteam;

import robocode.*;
import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.util.concurrent.ThreadLocalRandom;

/**
 * SimpleDroid - a sample robot by Mathew Nelson.
 * <p/>
 * Follows orders of team leader.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Magnum extends TeamRobot implements Droid {

    /**
     * run: Droid's default behavior
     */
    public void run() {
        
        if (getRandom(1, 2) == 1) {
            turnRight(getRandom(30, 90));
        } else {
            turnLeft(getRandom(30, 90));
        }
        
        ahead(200);
        while (true) {
            ahead(getRandom(150, 350));
            if (getRandom(1, 2) == 1) {
                turnRight(getRandom(0, 90));
            } else {
                turnLeft(getRandom(0, 90));
            }
            back(getRandom(0, 50));
        }
    }

    /**
     * onMessageReceived: What to do when our leader sends a message
     */
    public void onMessageReceived(MessageEvent e) {
        // Fire at a point
        if (e.getMessage() instanceof Point) {
            Point p = (Point) e.getMessage();
            // Calculate x and y to target
            double dx = p.getX() - this.getX();
            double dy = p.getY() - this.getY();
            
            // Calculate angle to target
            double theta = Math.toDegrees(Math.atan2(dx, dy));

            // Turn gun to target
            turnGunRight(normalRelativeAngleDegrees(theta - getGunHeading()));
            // Fire hard!
            
            fire(2);
        } // Set our colors
        else if (e.getMessage() instanceof RobotColors) {
            RobotColors c = (RobotColors) e.getMessage();

            setBodyColor(c.bodyColor);
            setGunColor(c.gunColor);
            setRadarColor(c.radarColor);
            setScanColor(c.scanColor);
            setBulletColor(c.bulletColor);
        }
    }
    
    public void onHitWall(HitWallEvent event) {
        turnRight(180);
        ahead(200);
    }
    
    public int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
