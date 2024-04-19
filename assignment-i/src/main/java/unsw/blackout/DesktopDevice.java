package unsw.blackout;

import unsw.utils.Angle;

/**
 * DesktopDevice
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class DesktopDevice extends Device {

    public DesktopDevice(String deviceId, Angle position) {
        super(deviceId, position, 200000);
    }

    @Override
    public boolean isSupporting(Satellite satellite) {
        return !(satellite instanceof StandardSatellite);
    }
}
