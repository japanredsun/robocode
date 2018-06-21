package rsa;

import java.awt.Color;

import robocode.*;
import robocode.util.Utils;

public class SoloBot extends AdvancedRobot{

    boolean peek;
    double moveAmount;

    @Override
    public void run() {

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

        do {
            // ...
            // Turn the radar if we have no more turn, starts it if it stops and at the start of round
            if ( getRadarTurnRemaining() == 0.0 )
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );

            execute();

            setMaxVelocity(5);
            // Start moving (and turning)
            peek = true;
            ahead(moveAmount);
            peek = false;
            turnRight(180);
        } while ( true );
    }

    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        // ...
        // Absolute angle towards target
        double angleToEnemy = getHeadingRadians() + e.getBearingRadians();

        // Subtract current radar heading to get the turn required to face the enemy, be sure it is normalized
        double radarTurn = Utils.normalRelativeAngle( angleToEnemy - getRadarHeadingRadians() );

        // Distance we want to scan from middle of enemy to either side
        // The 36.0 is how many units from the center of the enemy robot it scans.
        double extraTurn = Math.min( Math.atan( 120.0 / e.getDistance() ), Rules.RADAR_TURN_RATE_RADIANS );

        // Adjust the radar turn so it goes that much further in the direction it is going to turn
        // Basically if we were going to turn it left, turn it even more left, if right, turn more right.
        // This allows us to overshoot our enemy so that we get a good sweep that will not slip.
        if (radarTurn < 0)
            radarTurn -= extraTurn;
        else
            radarTurn += extraTurn;

        //Turn the radar
        setTurnRadarRightRadians(radarTurn);

        // ...
    }
}
