package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;
import unsw.utils.MathsHelper;

/**
 * Satellite
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class Satellite {

    private List<File> files = new ArrayList<File>();
    private List<File> receivingFiles = new ArrayList<File>();
    private List<File> sendingFiles = new ArrayList<File>();
    private double height;
    private Angle position = new Angle();
    private double velocity;
    private int range;
    private int sendingSpeed;
    private int receivingSpeed;
    private int maxBytes;
    private int maxFiles;
    private boolean hasTeleported = false;
    private String satelliteId;

    public Satellite(String satelliteId, double height, Angle position, double velocity,
                        int range, int sendingSpeed, int receivingSpeed, int maxBytes, int maxFiles) {
        this.satelliteId = satelliteId;
        this.height = height;
        this.position = this.position.add(position);
        this.range = range;
        this.sendingSpeed = sendingSpeed;
        this.receivingSpeed = receivingSpeed;
        this.maxBytes = maxBytes;
        this.maxFiles = maxFiles;
        this.velocity = velocity;
    }

    public boolean isSupporting(Satellite satellites) {
        return true;
    }

    public boolean isSupporting(Device device) {
        return true;
    }

    public List<File> getFiles() {
        return files;
    }

    public List<File> getReceivingFiles() {
        return receivingFiles;
    }

    public List<File> getSendingFiles() {
        return sendingFiles;
    }

    public boolean reachMaxFiles() {
        return maxFiles <= files.size();
    }

    public boolean reachMaxBytes(int num) {
        return getAllBytes() + num > maxBytes;
    }

    public int getAllBytes() {
        int currBytes = 0;
        for (File file : files) {
            currBytes += file.getSize();
        }
        return currBytes;
    }

    public boolean reachMaxBanwidth() {
        return (receivingFiles.size() >= receivingSpeed)
                || (sendingFiles.size() >= sendingSpeed);
    }

    public boolean getHasTeleported() {
        return hasTeleported;
    }

    public void addSendingList(File file) {
        sendingFiles.add(file);
    }

    public void removeSendingList(File file) {
        sendingFiles.remove(file);
    }

    public String getId() {
        return satelliteId;
    }

    public double getHeight() {
        return height;
    }

    public Angle getPosition() {
        return position;
    }

    public double getVelocity() {
        return velocity;
    }

    public int getSendingSpeed() {
        return (sendingFiles.size() == 0) ? 1 : sendingSpeed / sendingFiles.size();
    }

    public int getReceivingSpeed() {
        return receivingSpeed;
    }

    public int getCapacity() {
        return maxBytes;
    }

    public void setPosition(Angle angle) {
        this.position = angle;
    }

    public void setHasTeleported(boolean teleport) {
        this.hasTeleported = teleport;
    }

    public void setFiles(List<File> list) {
        this.files = list;
    }

    public void setReceivingFiles(List<File> list) {
        this.receivingFiles = list;
    }

    public void setSendingFiles(List<File> list) {
        this.sendingFiles = list;
    }

    public void addFile(String name, String content, Device fromDevice) {
        File file = new File(name, content, fromDevice);
        receivingFiles.add(file);
        files.add(file);
    }

    public void addFile(String name, String content, Satellite fromSatellite) {
        File file = new File(name, content, fromSatellite);
        fromSatellite.addSendingList(file);
        receivingFiles.add(file);
        files.add(file);
    }

    private List<String> removeDuplicate(List<String> entities) {
        List<String> result = new ArrayList<String>();
        for (String name : entities) {
            if (!result.contains(name)) {
                result.add(name);
            }
        }
        return result;
    }

    public List<String> getEntitiesInRange(List<Satellite> satellites, List<Device> devices) {
        List<String> entitiesInRange = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (MathsHelper.isVisible(height, position, satellite.getHeight(), satellite.getPosition())
                    && MathsHelper.getDistance(height, position, satellite.getHeight(), satellite.getPosition())
                        <= range) {
                if (this.isSupporting(satellite)) {
                    entitiesInRange.add(satellite.getId());
                }
                if (satellite instanceof RelaySatellite) {
                    entitiesInRange.addAll(satellite.getEntitiesInRange(satellites, devices, this, entitiesInRange));
                }
            }
        }
        entitiesInRange.addAll(getDevicesInRange(this, devices));
        List<String> result = removeDuplicate(entitiesInRange);
        result.remove(satelliteId);
        return result;
    }

    public List<String> getEntitiesInRange(List<Satellite> satellites, List<Device> devices, Satellite origin,
                                            List<String> existEntities) {
        List<String> entitiesInRange = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (MathsHelper.isVisible(height, position, satellite.getHeight(), satellite.getPosition())
                    && MathsHelper.getDistance(height, position, satellite.getHeight(), satellite.getPosition())
                        <= range) {
                if (origin.isSupporting(satellite)) {
                    entitiesInRange.add(satellite.getId());
                }
                if (satellite instanceof RelaySatellite && !existEntities.contains(satellite.getId())) {
                    existEntities.addAll(entitiesInRange);
                    entitiesInRange.addAll(satellite.getEntitiesInRange(satellites, devices, origin, existEntities));
                }
            }
        }
        entitiesInRange.addAll(getDevicesInRange(origin, devices));
        return removeDuplicate(entitiesInRange);
    }

    public List<String> getSatellitesInRange(List<Satellite> satellites, Device origin) {
        List<String> satellitesInRange = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (MathsHelper.isVisible(height, position, satellite.getHeight(), satellite.getPosition())
                    && MathsHelper.getDistance(height, position, satellite.getHeight(), satellite.getPosition())
                        <= range) {
                if (origin.isSupporting(satellite)) {
                    satellitesInRange.add(satellite.getId());
                }
                if (satellite instanceof RelaySatellite) {
                    satellitesInRange.addAll(satellite.getSatellitesInRange(satellites, origin, satellitesInRange));
                }
            }
        }
        return removeDuplicate(satellitesInRange);
    }

    private List<String> getSatellitesInRange(List<Satellite> satellites, Device origin, List<String> existSatellites) {
        List<String> satellitesInRange = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            if (MathsHelper.isVisible(height, position, satellite.getHeight(), satellite.getPosition())
                    && MathsHelper.getDistance(height, position, satellite.getHeight(), satellite.getPosition())
                        <= range) {
                if (origin.isSupporting(satellite)) {
                    satellitesInRange.add(satellite.getId());
                }
                if (satellite instanceof RelaySatellite && !existSatellites.contains(satellite.getId())) {
                    existSatellites.addAll(satellitesInRange);
                    satellitesInRange.addAll(satellite.getSatellitesInRange(satellites, origin, existSatellites));
                }
            }
        }
        return removeDuplicate(satellitesInRange);
    }

    public List<String> getDevicesInRange(Satellite satellite, List<Device> devices) {
        List<String> devicesInRange = new ArrayList<String>();
        for (Device device : devices) {
            if (MathsHelper.isVisible(height, position, device.getPosition())
                    && MathsHelper.getDistance(height, position, device.getPosition()) <= range) {
                if (satellite.isSupporting(device)) {
                    devicesInRange.add(device.getId());
                }
            }
        }
        return devicesInRange;
    }

    public boolean isCommunicable(Satellite satellite, List<Satellite> satellites, List<Device> devices) {
        List<String> satellitesInRange = getEntitiesInRange(satellites, devices);
        return satellitesInRange.contains(satellite.getId());
    }

    public boolean isCommunicable(Device device, List<Satellite> satellites, List<Device> devices) {
        List<String> entitiesInRange = getEntitiesInRange(satellites, devices);
        return entitiesInRange.contains(device.getId());
    }

    public void filesTransfer(List<Satellite> satellites, List<Device> devices) {
        for (File file : receivingFiles) {
            if (file.isFromDevice()) {
                Device device = file.getSenderDevice();
                if (hasTeleported) {
                    List<File> deviceFiles = device.getFiles();
                    File originFile = deviceFiles.stream().
                        filter(element -> element.getName().equals(file.getName())).findAny().orElse(null);
                    originFile.setContent(originFile.getFullContent().replaceAll("t", ""));
                    files.remove(file);
                } else if (!device.isCommunicable(this, satellites)) {
                    files.remove(file);
                } else {
                    file.filesTransfer(receivingSpeed / receivingFiles.size());
                }
            } else {
                Satellite satellite = file.getSenderSatellite();
                if (hasTeleported || satellite.getHasTeleported()) {
                    file.setContent(file.getFullContent().replaceAll("t", ""));
                } else if (!satellite.isCommunicable(this, satellites, devices)) {
                    files.remove(file);
                } else {
                    file.filesTransfer(Math.min((receivingSpeed / receivingFiles.size()), satellite.getSendingSpeed()));
                }
            }
        }
    }

    public void boundPosition() {
        double degrees = position.toDegrees();
        if (degrees < 0) {
            position = Angle.fromDegrees(degrees + 360);
        } else if (degrees >= 360) {
            position = Angle.fromDegrees(degrees - 360);
        }
    }

    public void updateFilesTransfer() {
        receivingFiles.retainAll(files);
        receivingFiles.removeIf(element -> element.hasTransferCompleted());
        sendingFiles.removeIf(element -> (element.hasTransferCompleted() || element.getIsTransient()));
    }

    public void move() {
        position = position.subtract(Angle.fromRadians(velocity / height));
        boundPosition();
    }
}
