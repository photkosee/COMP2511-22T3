package unsw.blackout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.response.models.EntityInfoResponse;
import unsw.response.models.FileInfoResponse;
import unsw.utils.Angle;

import unsw.blackout.FileTransferException.VirtualFileAlreadyExistsException;
import unsw.blackout.FileTransferException.VirtualFileNoBandwidthException;
import unsw.blackout.FileTransferException.VirtualFileNoStorageSpaceException;
import unsw.blackout.FileTransferException.VirtualFileNotFoundException;

import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

/**
 * BlackoutController
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class BlackoutController {

    private List<Device> devices = new ArrayList<Device>();
    private List<Satellite> satellites = new ArrayList<Satellite>();

    public void createDevice(String deviceId, String type, Angle position) {
        switch (type) {
            case "HandheldDevice":
                devices.add(new HandheldDevice(deviceId, position)); break;
            case "LaptopDevice":
                devices.add(new LaptopDevice(deviceId, position)); break;
            case "DesktopDevice":
                devices.add(new DesktopDevice(deviceId, position)); break;
            default:
                System.out.println("Invalid input");
        }
    }

    public void removeDevice(String deviceId) {
        devices.removeIf(element -> element.getId().equals(deviceId));
    }

    public void createSatellite(String satelliteId, String type, double height, Angle position) {
        switch (type) {
            case "StandardSatellite":
                satellites.add(new StandardSatellite(satelliteId, height, position)); break;
            case "TeleportingSatellite":
                satellites.add(new TeleportingSatellite(satelliteId, height, position)); break;
            case "RelaySatellite":
                satellites.add(new RelaySatellite(satelliteId, height, position)); break;
            case "ElephantSatellite":
                satellites.add(new ElephantSatellite(satelliteId, height, position)); break;
            default:
                System.out.println("Invalid input");
        }
    }

    public void removeSatellite(String satelliteId) {
        satellites.removeIf(element -> element.getId().equals(satelliteId));
    }

    public List<String> listDeviceIds() {
        List<String> deviceIds = new ArrayList<String>();
        for (Device device : devices) {
            deviceIds.add(device.getId());
        }
        return deviceIds;
    }

    public List<String> listSatelliteIds() {
        List<String> satelliteIds = new ArrayList<String>();
        for (Satellite satellite : satellites) {
            satelliteIds.add(satellite.getId());
        }
        return satelliteIds;
    }

    private Device findDevice(String id) {
        return devices.stream().filter(element -> element.getId().equals(id)).findAny().orElse(null);
    }

    private Satellite findSatellite(String id) {
        return satellites.stream().filter(element -> element.getId().equals(id)).findAny().orElse(null);
    }

    public void addFileToDevice(String deviceId, String filename, String content) {
        Device device = findDevice(deviceId);
        device.addFile(filename, content);
    }

    public EntityInfoResponse getInfo(String id) {
        Satellite satellite = findSatellite(id);
        Map<String, FileInfoResponse> filesMap = new HashMap<String, FileInfoResponse>();
        if (satellite != null) {
            for (File file : satellite.getFiles()) {
                filesMap.put(file.getName(), new FileInfoResponse(file.getName(), file.getTransferredContent(),
                            file.getSize(), file.hasTransferCompleted()));
            }
            return new EntityInfoResponse(id, satellite.getPosition(), satellite.getHeight(),
                                            satellite.getClass().getSimpleName(), filesMap);
        }

        Device device = findDevice(id);
        for (File file : device.getFiles()) {
            filesMap.put(file.getName(), new FileInfoResponse(file.getName(), file.getTransferredContent(),
                                                            file.getSize(), file.hasTransferCompleted()));
        }
        return new EntityInfoResponse(id, device.getPosition(), RADIUS_OF_JUPITER,
                                        device.getClass().getSimpleName(), filesMap);
    }

    public void simulate() {
        for (Satellite satellite : satellites) {
            satellite.move();
        }
        for (Satellite satellite : satellites) {
            if (satellite instanceof ElephantSatellite) {
                ((ElephantSatellite) satellite).updateTransientFiles(satellites, devices);
            }
        }
        for (Device device : devices) {
            device.filesTransfer(satellites, devices);
        }
        for (Satellite satellite : satellites) {
            satellite.filesTransfer(satellites, devices);
        }
        for (Satellite satellite : satellites) {
            satellite.updateFilesTransfer();
        }
    }

    /**
     * Simulate for the specified number of minutes.
     * You shouldn't need to modify this function.
     */
    public void simulate(int numberOfMinutes) {
        for (int i = 0; i < numberOfMinutes; i++) {
            simulate();
        }
    }

    public List<String> communicableEntitiesInRange(String id) {
        Device device = findDevice(id);
        if (device != null) {
            return device.getSatellitesInRange(satellites);
        }

        Satellite satellite = findSatellite(id);
        return satellite.getEntitiesInRange(satellites, devices);
    }

    private File findFile(Device device, String name) {
        return device.getFiles().stream().filter(element -> element.getName().equals(name)).findAny().orElse(null);
    }

    private File findFile(Satellite satellite, String name) {
        return satellite.getFiles().stream().filter(element -> element.getName().equals(name)).findAny().orElse(null);
    }

    public void sendFile(String fileName, String fromId, String toId) throws FileTransferException {
        Device fromDevice = findDevice(fromId);
        Device toDevice = findDevice(toId);
        Satellite fromSatellite = findSatellite(fromId);
        Satellite toSatellite = findSatellite(toId);
        // devices -> satellites
        if (fromSatellite == null) {
            File file = findFile(fromDevice, fileName);
            fileException(file, toSatellite, fileName);
            toSatellite.addFile(fileName, file.getTransferredContent(), fromDevice);
        // satellites -> devices
        } else if (toSatellite == null) {
            File file = findFile(fromSatellite, fileName);
            fileException(file, fromSatellite, toDevice, fileName);
            toDevice.addFile(fileName, file.getTransferredContent(), fromSatellite);
        // satellites -> satellites
        } else {
            File file = findFile(fromSatellite, fileName);
            fileException(file, fromSatellite, toSatellite, fileName);
            toSatellite.addFile(fileName, file.getTransferredContent(), fromSatellite);
        }
    }

    private void fileException(File file, Satellite toSatellite, String fileName) throws FileTransferException {
        if (file == null) {
            throw new VirtualFileNotFoundException(fileName);
        } else if (toSatellite.reachMaxBanwidth()) {
            throw new VirtualFileNoBandwidthException(toSatellite.getId());
        } else if (findFile(toSatellite, fileName) != null) {
            throw new VirtualFileAlreadyExistsException(fileName);
        } else if (toSatellite.reachMaxFiles()) {
            throw new VirtualFileNoStorageSpaceException("Max Files Reached");
        } else if (toSatellite.reachMaxBytes(file.getSize())) {
            throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
        }
    }

    private void fileException(File file, Satellite fromSatellite, Device toDevice, String fileName)
                                throws FileTransferException {
        if (file == null || !file.hasTransferCompleted()) {
            throw new VirtualFileNotFoundException(fileName);
        } else if (fromSatellite.reachMaxBanwidth()) {
            throw new VirtualFileNoBandwidthException(fromSatellite.getId());
        } else if (findFile(toDevice, fileName) != null) {
            throw new VirtualFileAlreadyExistsException(fileName);
        }
    }

    private void fileException(File file, Satellite fromSatellite, Satellite toSatellite, String fileName)
                                throws FileTransferException {
        if (file == null || !file.hasTransferCompleted()) {
            throw new VirtualFileNotFoundException(fileName);
        } else if (fromSatellite.reachMaxBanwidth()) {
            throw new VirtualFileNoBandwidthException(fromSatellite.getId());
        } else if (toSatellite.reachMaxBanwidth()) {
            throw new VirtualFileNoBandwidthException(toSatellite.getId());
        } else if (findFile(toSatellite, fileName) != null) {
            throw new VirtualFileAlreadyExistsException(fileName);
        } else if (toSatellite.reachMaxFiles()) {
            throw new VirtualFileNoStorageSpaceException("Max Files Reached");
        } else if (toSatellite.reachMaxBytes(file.getSize())) {
            throw new VirtualFileNoStorageSpaceException("Max Storage Reached");
        }
    }

    public void createDevice(String deviceId, String type, Angle position, boolean isMoving) {
        createDevice(deviceId, type, position);
        // TODO: Task 3
    }

    public void createSlope(int startAngle, int endAngle, int gradient) {
        // TODO: Task 3
        // If you are not completing Task 3 you can leave this method blank :)
    }

}
