/**
 * Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://robocode.sourceforge.net/license/epl-v10.html
 */
package sampleteam;


import robocode.HitByBulletEvent;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

import java.awt.*;
import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;


/**
 * MyFirstLeader - a sample team robot by Mathew Nelson.
 * <p/>
 * Looks around for enemies, and orders teammates to fire
 *
 * @author Mathew A. Nelson (original)
 * @author Flemming N. Larsen (contributor)
 */
public class Leader extends TeamRobot {

	/**
	 * run:  Leader's default behavior
	 */
    Color leaderColor = Color.WHITE;
    
	public void run() {
		// Prepare RobotColors object
		RobotColors c = new RobotColors();
		

		c.bodyColor = Color.black;
		c.gunColor = Color.black;
		c.radarColor = Color.black;
		c.scanColor = Color.yellow;
		c.bulletColor = Color.yellow;

		// Set the color of this robot containing the RobotColors
		setBodyColor(leaderColor);
		setGunColor(leaderColor);
		setRadarColor(leaderColor);
		setScanColor(leaderColor);
		setBulletColor(leaderColor);
		try {
			// Send RobotColors object to our entire team
			broadcastMessage(c);
			
		} catch (IOException ignored) {}
		
		// Normal behavior
		while (true) {
			setTurnRadarRight(10000);
			ahead(100);
            if (getRandom(1, 2) == 1) {
                turnRight(getRandom(0, 180));
            } else {
                turnLeft(getRandom(0, 180));
            }
            back(getRandom(0, 50));
		}
	}

	/**
	 * onScannedRobot:  What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
		// Don't fire on teammates
		if (isTeammate(e.getName())) {
			return;
		}
		
		// Calculate enemy bearing
		double enemyBearing = this.getHeading() + e.getBearing();
		// Calculate enemy's position
		double enemyX = getX() + e.getDistance() * Math.sin(Math.toRadians(enemyBearing));
		double enemyY = getY() + e.getDistance() * Math.cos(Math.toRadians(enemyBearing));

		try {
			// Send enemy position to teammates
			broadcastMessage(new Point(enemyX, enemyY));
		} catch (IOException ex) {
			out.println("Unable to send order: ");
			ex.printStackTrace(out);
		}
	}

	/**
	 * onHitByBullet:  Turn perpendicular to bullet path
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		turnLeft(60 - e.getBearing());
	}
	
	public int getRandom(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1);
    }
}
