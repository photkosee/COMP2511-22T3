package unsw.blackout;

import unsw.utils.Angle;

import static unsw.utils.MathsHelper.CLOCKWISE;
import static unsw.utils.MathsHelper.ANTI_CLOCKWISE;

/**
 * RelaySatellite
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class RelaySatellite extends Satellite {

    private static final Angle LOWER_BOUND_RELAY_SATELLITE = Angle.fromDegrees(190);
    private static final Angle UPPER_BOUND_RELAY_SATELLITE = Angle.fromDegrees(140);
    private int direction = CLOCKWISE;
    private Angle prePosition;

    public RelaySatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 1500, 300000, 1, 1, 1, 0);
        this.direction = ((position.toDegrees() >= 345 || position.toDegrees() < 140)) ? ANTI_CLOCKWISE : CLOCKWISE;
        this.prePosition = position;
    }

    @Override
    public void move() {
        Angle position = getPosition();
        this.prePosition = position;
        double velocity = getVelocity();
        double height = getHeight();
        if (direction == CLOCKWISE) {
            setPosition(position.subtract(Angle.fromRadians(velocity / height)));
            boundPosition();
            position = getPosition();
            if (prePosition.compareTo(UPPER_BOUND_RELAY_SATELLITE) != -1
                    && position.compareTo(UPPER_BOUND_RELAY_SATELLITE) == -1) {
                this.direction = ANTI_CLOCKWISE;
            }
        } else {
            setPosition(position.add(Angle.fromRadians(velocity / height)));
            boundPosition();
            position = getPosition();
            if (prePosition.compareTo(LOWER_BOUND_RELAY_SATELLITE) != 1
                    && position.compareTo(LOWER_BOUND_RELAY_SATELLITE) == 1) {
                this.direction = CLOCKWISE;
            }
        }
    }
}
