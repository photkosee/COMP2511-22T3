package unsw.archaic_fs.exceptions;

public class UNSWFileAlreadyExistsException extends java.nio.file.FileAlreadyExistsException {

    public UNSWFileAlreadyExistsException(String file) {
        super(file);
    }

}
