package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

/**
 * Device
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class Device {

    private List<File> files = new ArrayList<File>();
    private List<File> receivingFiles = new ArrayList<File>();
    private String deviceId;
    private Angle position;
    private int range;

    public Device(String deviceId, Angle position, int range) {
        this.deviceId = deviceId;
        this.position = position;
        this.range = range;
    }

    public String getId() {
        return deviceId;
    }

    public Angle getPosition() {
        return position;
    }

    public List<File> getFiles() {
        return files;
    }

    public boolean isSupporting(Satellite satellite) {
        return true;
    }

    public boolean isSupporting(Device device) {
        return false;
    }

    public void addFile(String name, String content) {
        File file = new File(name, content);
        files.add(file);
    }

    public void addFile(String name, String content, Satellite satellite) {
        File file = new File(name, content, satellite);
        satellite.addSendingList(file);
        receivingFiles.add(file);
        files.add(file);
    }

    public List<String> getSatellitesInRange(List<Satellite> satellites) {
        List<String> satellitesInRange = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (MathsHelper.isVisible(satellite.getHeight(), satellite.getPosition(), position)
                    && MathsHelper.getDistance(satellite.getHeight(), satellite.getPosition(), position) <= range) {
                if (this.isSupporting(satellite)) {
                    satellitesInRange.add(satellite.getId());
                }
                if (satellite instanceof RelaySatellite) {
                    satellitesInRange.addAll(satellite.getSatellitesInRange(satellites, this));
                }
            }
        }
        List<String> result = new ArrayList<String>();
        for (String name : satellitesInRange) {
            if (!result.contains(name)) {
                result.add(name);
            }
        }
        return result;
    }

    public boolean isCommunicable(Satellite satellite, List<Satellite> satellites) {
        List<String> satellitesInRange = getSatellitesInRange(satellites);
        return satellitesInRange.contains(satellite.getId());
    }

    public void filesTransfer(List<Satellite> satellites, List<Device> devices) {
        for (File file : receivingFiles) {
            Satellite satellite = file.getSenderSatellite();
            if (satellite.getHasTeleported()) {
                file.setContent(file.getFullContent().replaceAll("t", ""));
            } else if (!satellite.isCommunicable(this, satellites, devices)) {
                files.remove(file);
            } else {
                file.filesTransfer(satellite.getSendingSpeed());
            }
        }
        receivingFiles.retainAll(files);
        receivingFiles.removeIf(element -> element.hasTransferCompleted());
    }
}
