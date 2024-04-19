package archaic_fs;

import org.junit.jupiter.api.Test;

import unsw.archaic_fs.ArchaicFileSystem;
import unsw.archaic_fs.FileWriteOptions;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import unsw.archaic_fs.exceptions.UNSWNoSuchFileException;
import unsw.archaic_fs.exceptions.UNSWFileAlreadyExistsException;
import unsw.archaic_fs.exceptions.UNSWFileNotFoundException;
import java.util.EnumSet;

public class ArchaicFsTest {
    @Test
    public void testCdInvalidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        // Try to change directory to an invalid one
        assertThrows(UNSWNoSuchFileException.class, () -> {
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdValidDirectory() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
        });
    }

    @Test
    public void testCdAroundPaths() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
        });
    }

    @Test
    public void testCreateFile() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("test.txt"));
            fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
            assertEquals("New Content", fs.readFromFile("test.txt"));
        });
    }

    // Now write some more!
    // Some ideas to spark inspiration;
    // - File Writing/Reading with various options (appending for example)
    // - Cd'ing .. on the root most directory (shouldn't error should just remain on root directory)
    // - many others...
    @Test
    public void testCdRootMostPaths() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
            fs.cd("..");
            assertEquals("", fs.cwd());
            fs.cd("..");
            assertEquals("", fs.cwd());
        });
    }

    @Test
    public void testFileAlreadyExist() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
            fs.readFromFile("test.txt");
            assertEquals("My Content", fs.readFromFile("test.txt"));
        });
        assertThrows(UNSWFileAlreadyExistsException.class, () -> {
            fs.mkdir("/usr/bin/cool-stuff", false, false);
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
        });
        assertThrows(IllegalArgumentException.class, () -> {
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.TRUNCATE, FileWriteOptions.APPEND));
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.CREATE));
        });
    }

    @Test
    public void testFileNotFound() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.readFromFile("test.txt");
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.TRUNCATE));
        });
    }

    @Test
    public void testCreateSamePAth() {
        ArchaicFileSystem fs = new ArchaicFileSystem();

        assertDoesNotThrow(() -> {
            fs.lookupInode(-1);
            fs.mkdir("/usr/bin/cool-stuff", true, false);
            fs.mkdir("/usr/bin/cool-stuff", false, true);
            fs.mkdir("/usr/bin/hey", false, true);
            fs.cd("/usr/bin/cool-stuff");
            assertEquals("/usr/bin/cool-stuff", fs.cwd());
            fs.writeToFile("test.txt", "My Content", EnumSet.of(FileWriteOptions.CREATE, FileWriteOptions.APPEND));
            fs.writeToFile("test.txt", "My Content",
                EnumSet.of(FileWriteOptions.CREATE_IF_NOT_EXISTS, FileWriteOptions.TRUNCATE));
            assertEquals("My Content", fs.readFromFile("test.txt"));
            fs.cd("..");
            assertEquals("/usr/bin", fs.cwd());
            fs.cd("../bin/..");
            assertEquals("/usr", fs.cwd());
            fs.mkdir("/usr/bin/cool@", true, true);
            fs.lookupInode(0);
        });
        assertThrows(UNSWFileNotFoundException.class, () -> {
            fs.writeToFile("test.txt", "New Content", EnumSet.of(FileWriteOptions.TRUNCATE));
            fs.readFromFile("test.txt");
        });
        assertThrows(UNSWNoSuchFileException.class, () -> {
            fs.cd("/usr/bin/cool-");
            fs.mkdir("/usr/bin/hey", false, false);
            fs.mkdir("/usr/bin/hey", true, false);
        });
    }
}
