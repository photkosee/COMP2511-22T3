package unsw.blackout;

import unsw.utils.Angle;

import static unsw.utils.MathsHelper.CLOCKWISE;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;

/**
 * TeleportingSatellite
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class TeleportingSatellite extends Satellite {

    private int direction = ANTI_CLOCKWISE;

    public TeleportingSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 1000, 200000, 10, 15, 200, 200);
    }

    @Override
    public boolean isSupporting(Satellite satellite) {
        return !(satellite instanceof ElephantSatellite);
    }

    private void teleport() {
        setPosition(Angle.fromDegrees(0));
        this.direction = (direction == CLOCKWISE) ? ANTI_CLOCKWISE : CLOCKWISE;
        setHasTeleported(true);
    }

    @Override
    public void move() {
        double currPosition;
        double prePosition = getPosition().toDegrees();
        setHasTeleported(false);
        Angle position = getPosition();
        double velocity = getVelocity();
        double height = getHeight();
        switch (direction) {
            case CLOCKWISE:
                setPosition(position.subtract(Angle.fromRadians(velocity / height)));
                boundPosition();
                currPosition = getPosition().toDegrees();
                if (prePosition > 180 && currPosition <= 180) {
                    teleport();
                }
                break;
            case ANTI_CLOCKWISE:
                setPosition(position.add(Angle.fromRadians(velocity / height)));
                boundPosition();
                currPosition = getPosition().toDegrees();
                if (prePosition < 180 && currPosition >= 180) {
                    teleport();
                }
                break;
            default:
                System.out.println("Invalid movement");
        }
    }
}
