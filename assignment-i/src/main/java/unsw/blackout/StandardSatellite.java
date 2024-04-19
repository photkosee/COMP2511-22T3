package unsw.blackout;

import unsw.utils.Angle;

/**
 * StandardSatellite
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class StandardSatellite extends Satellite {

    public StandardSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 2500, 150000, 1, 1, 80, 3);
    }

    @Override
    public boolean isSupporting(Device device) {
        return !(device instanceof DesktopDevice);
    }
}
