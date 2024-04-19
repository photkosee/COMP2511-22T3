package unsw.blackout;

import java.util.ArrayList;
import java.util.List;

import unsw.utils.Angle;

/**
 * ElephantSatellite
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class ElephantSatellite extends Satellite {

    private List<File> transientFiles = new ArrayList<File>();

    public ElephantSatellite(String satelliteId, double height, Angle position) {
        super(satelliteId, height, position, 2500, 400000, 20, 20, 90, 90);
    }

    @Override
    public boolean isSupporting(Satellite satellites) {
        return !(satellites instanceof TeleportingSatellite);
    }

    @Override
    public boolean isSupporting(Device device) {
        return !(device instanceof HandheldDevice);
    }

    @Override
    public boolean reachMaxBanwidth() {
        return (getReceivingFiles().size() - transientFiles.size() >= getReceivingSpeed())
                || (getSendingFiles().size() >= getSendingSpeed());
    }

    private int getTrasientBytes() {
        int transientBytes = 0;
        for (File file : getReceivingFiles()) {
            if (file.getIsTransient()) {
                transientBytes += file.getSize();
            }
        }
        return transientBytes;
    }

    @Override
    public boolean reachMaxBytes(int num) {
        return (getTrasientBytes() == 0 && getCapacity() < getAllBytes() + num)
                || (getAllBytes() + num - getCapacity() > getTrasientBytes());
    }

    private void makeRoom(File file) {
        List<File> receivingFiles = getReceivingFiles();
        receivingFiles.add(file);
        setReceivingFiles(receivingFiles);
        List<File> files = getFiles();
        files.add(file);
        setFiles(files);

        if (getAllBytes() > getCapacity()) {
            Knapsack solution = new Knapsack(getCapacity() - getAllBytes() + getTrasientBytes(), transientFiles);
            this.transientFiles = solution.solution();
            files.removeIf(element -> (element.getIsTransient() && !transientFiles.contains(element)));
            receivingFiles.retainAll(files);
            setReceivingFiles(receivingFiles);
            setFiles(files);
        }
    }

    @Override
    public void addFile(String name, String content, Device fromDevice) {
        File file = new File(name, content, fromDevice);
        makeRoom(file);
    }

    @Override
    public void addFile(String name, String content, Satellite fromSatellite) {
        File file = new File(name, content, fromSatellite);
        fromSatellite.addSendingList(file);
        makeRoom(file);
    }

    public void updateTransientFiles(List<Satellite> satellites, List<Device> devices) {
        for (File file : getReceivingFiles()) {
            if (file.isFromDevice()) {
                Device device = file.getSenderDevice();
                if (device.isCommunicable(this, satellites) && file.getIsTransient()) {
                    file.setIsTransient(false);
                    transientFiles.remove(file);
                } else if (!device.isCommunicable(this, satellites) && !file.getIsTransient()) {
                    file.setIsTransient(true);
                    transientFiles.add(file);
                }
            } else {
                Satellite satellite = file.getSenderSatellite();
                if (satellite.isCommunicable(this, satellites, devices) && file.getIsTransient()) {
                    file.setIsTransient(false);
                    transientFiles.remove(file);
                    satellite.addSendingList(file);
                } else if (!satellite.isCommunicable(this, satellites, devices) && !file.getIsTransient()) {
                    file.setIsTransient(true);
                    satellite.removeSendingList(file);
                    transientFiles.add(file);
                }
            }
        }
    }

    @Override
    public void filesTransfer(List<Satellite> satellites, List<Device> devices) {
        for (File file : getReceivingFiles()) {
            int numFiles = getReceivingFiles().size() - transientFiles.size();
            numFiles = (numFiles <= 0) ? 1 : numFiles;
            if (file.isFromDevice() && !file.getIsTransient()) {
                file.filesTransfer(getReceivingSpeed() / numFiles);
            } else if (!file.getIsTransient()) {
                Satellite satellite = file.getSenderSatellite();
                file.filesTransfer(Math.min((getReceivingSpeed() / numFiles), satellite.getSendingSpeed()));
            }
        }
    }

    @Override
    public void updateFilesTransfer() {
        List<File> receivingFiles = getReceivingFiles();
        List<File> files = getFiles();
        List<File> sendingFiles = getSendingFiles();
        receivingFiles.retainAll(files);
        receivingFiles.removeIf(element -> element.hasTransferCompleted() && !element.getIsTransient());
        sendingFiles.removeIf(element -> element.hasTransferCompleted() || element.getIsTransient());
        transientFiles.retainAll(files);
        transientFiles.removeIf(element -> element.hasTransferCompleted() || !element.getIsTransient());
        setReceivingFiles(receivingFiles);
        setSendingFiles(sendingFiles);
    }
}
