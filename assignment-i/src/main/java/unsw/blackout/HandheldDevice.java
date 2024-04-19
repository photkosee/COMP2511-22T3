package unsw.blackout;

import unsw.utils.Angle;

/**
 * HandheldDevice
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class HandheldDevice extends Device {

    public HandheldDevice(String deviceId, Angle position) {
        super(deviceId, position, 50000);
    }

    @Override
    public boolean isSupporting(Satellite satellite) {
        return !(satellite instanceof ElephantSatellite);
    }
}
