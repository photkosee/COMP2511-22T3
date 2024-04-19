package unsw.blackout;

/**
 * File
 *
 * @author Phot Koseekrainiramon (5387411)
 * @version 07/10/2022
 *
 * Copyright notice
 */
public class File {

    private String name;
    private String content;
    private Device senderDevice;
    private Satellite senderSatellite;
    private int size;
    private int transferredBytes = 0;
    private boolean isTransient = false;

    public File(String name, String content) {
        this.name = name;
        this.content = content;
        this.size = content.length();
        this.transferredBytes = size;
    }

    public File(String name, String content, Device senderDevice) {
        this.name = name;
        this.content = content;
        this.size = content.length();
        this.senderDevice = senderDevice;
    }

    public File(String name, String content, Satellite senderSatellite) {
        this.name = name;
        this.content = content;
        this.size = content.length();
        this.senderSatellite = senderSatellite;
    }

    public String getName() {
        return name;
    }

    public boolean isFromDevice() {
        return senderDevice != null;
    }

    public Device getSenderDevice() {
        return senderDevice;
    }

    public Satellite getSenderSatellite() {
        return senderSatellite;
    }

    public String getTransferredContent() {
        return content.substring(0, transferredBytes);
    }

    public String getFullContent() {
        return content;
    }

    public int getSize() {
        return size;
    }

    public int getTransferredBytes() {
        return transferredBytes;
    }

    public boolean hasTransferCompleted() {
        return transferredBytes == size;
    }

    public boolean getIsTransient() {
        return isTransient;
    }

    public void setIsTransient(boolean isTransient) {
        this.isTransient = isTransient;
    }

    public void setContent(String content) {
        this.content = content;
        this.size = content.length();
        this.transferredBytes = size;
    }

    public void filesTransfer(int numByte) {
        transferredBytes += numByte;
        if (transferredBytes > size) {
            transferredBytes = size;
        }
    }
}
