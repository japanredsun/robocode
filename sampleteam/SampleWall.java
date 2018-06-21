/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sample;

import robocode.*;

import static robocode.util.Utils.normalRelativeAngleDegrees;

import java.awt.*;

/**
 * Walls - a sample robot by Mathew Nelson, and maintained by Flemming N. Larsen
 * <p/>
 * Moves around the outer edge with the gun facing in.
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Walls extends TeamRobot implements Droid {

    boolean peek; // Don't turn if there's a robot there
    double moveAmount; // How much to move

    /**
     * run: Move around the walls
     */
    public void run() {

        // Initialize moveAmount to the maximum possible for this battlefield.
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
        turnGunRight(90);
        turnRight(90);

        while (true) {
            // Look before we turn when ahead() completes.
            peek = true;
            // Move up the wall
            ahead(moveAmount);
            // Don't look now
            peek = false;
            // Turn to the next wall
            turnRight(90);
        }
    }

    /**
     * onHitRobot: Move away a bit.
     */
    public void onHitRobot(HitRobotEvent e) {
        // If he's in front of us, set back up a bit.
        if (e.getBearing() > -90 && e.getBearing() < 90) {
            back(100);
        } // else he's in back of us, so set ahead a bit.
        else {
            ahead(100);
        }
    }

    public void onMessageReceived(MessageEvent e) {

    }

    /**
     * onScannedRobot: Fire!
     */
    public void onScannedRobot(ScannedRobotEvent e) {
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

            double distance = e.getDistance(); // get the distance of the
                                               // scanned robot
            if (distance > 800) // this conditions adjust the fire force
                                // according the distance of the scanned robot.
                fire(5);
            else if (distance > 600 && distance <= 800)
                fire(4);
            else if (distance > 400 && distance <= 600)
                fire(3);
            else if (distance > 200 && distance <= 400)
                fire(2);
            else if (distance < 200)
                fire(1);

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
}
