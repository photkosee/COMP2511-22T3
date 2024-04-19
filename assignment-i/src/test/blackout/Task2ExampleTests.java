package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.blackout.FileTransferException;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task2ExampleTests {
    @Test
    public void testEntitiesInRange() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createSatellite("Satellite2", "StandardSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceD", "HandheldDevice", Angle.fromDegrees(180));
        controller.createDevice("DeviceE", "DesktopDevice", Angle.fromDegrees(315));
        controller.createSatellite("Satellite3", "StandardSatellite", 2000 + RADIUS_OF_JUPITER, Angle.fromDegrees(175));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceC", "Satellite2"),
            controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "Satellite1"),
            controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"), controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceD"), controller.communicableEntitiesInRange("Satellite3"));
    }

    @Test
    public void testSomeExceptionsForSend() {
        // just some of them... you'll have to test the rest
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 5000 + RADIUS_OF_JUPITER, Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () ->
            controller.sendFile("NonExistentFile", "DeviceC", "Satellite1"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
            controller.getInfo("Satellite1").getFiles().get("FileAlpha"));
        controller.simulate(msg.length() * 2);
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () ->
            controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
    }

    @Test
    public void testMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER,
            "StandardSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER,
            "StandardSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testExample() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "StandardSatellite", 10000 + RADIUS_OF_JUPITER,
            Angle.fromDegrees(320));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));

        String msg = "Hey";
        controller.addFileToDevice("DeviceC", "FileAlpha", msg);
        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "DeviceC", "Satellite1"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
            controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        controller.simulate(msg.length() * 2);
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
            controller.getInfo("Satellite1").getFiles().get("FileAlpha"));

        assertDoesNotThrow(() -> controller.sendFile("FileAlpha", "Satellite1", "DeviceB"));
        assertEquals(new FileInfoResponse("FileAlpha", "", msg.length(), false),
            controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        controller.simulate(msg.length());
        assertEquals(new FileInfoResponse("FileAlpha", msg, msg.length(), true),
            controller.getInfo("DeviceB").getFiles().get("FileAlpha"));

        // Hints for further testing:
        // - What about checking about the progress of the message half way through?
        // - Device/s get out of range of satellite
        // ... and so on.
    }

    @Test
    public void testRelayDirectionAndComminucable() {
        BlackoutController controller = new BlackoutController();
        // testing the supposed starting movement direction
        controller.createSatellite("A", "RelaySatellite", 89926,
                                Angle.fromDegrees(143));
        controller.createSatellite("B", "RelaySatellite", 104963,
                                Angle.fromDegrees(185));
        controller.createSatellite("C", "RelaySatellite", 88785,
                                Angle.fromDegrees(247));
        controller.createSatellite("D", "RelaySatellite", 93638,
                                Angle.fromDegrees(327));
        controller.createSatellite("E", "RelaySatellite", 93405,
                                Angle.fromDegrees(6));
        controller.createSatellite("F", "RelaySatellite", 100262,
                                Angle.fromDegrees(66));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "F", "E", "D"),
            controller.communicableEntitiesInRange("A"));
        assertListAreEqualIgnoringOrder(Arrays.asList("A", "C", "D", "E", "F"),
            controller.communicableEntitiesInRange("B"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "A", "D", "E", "F"),
            controller.communicableEntitiesInRange("C"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "A", "E", "F"),
            controller.communicableEntitiesInRange("D"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "D", "A", "F"),
            controller.communicableEntitiesInRange("E"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "D", "E", "A"),
            controller.communicableEntitiesInRange("F"));

        controller.simulate();
        assertTrue(Math.abs(controller.getInfo("A").getPosition().toDegrees() - 142) <= 1);
        assertTrue(Math.abs(controller.getInfo("B").getPosition().toDegrees() - 185) <= 1);
        assertTrue(Math.abs(controller.getInfo("C").getPosition().toDegrees() - 246) <= 1);
        assertTrue(Math.abs(controller.getInfo("D").getPosition().toDegrees() - 326) <= 1);
        assertTrue(Math.abs(controller.getInfo("E").getPosition().toDegrees() - 7) <= 1);
        assertTrue(Math.abs(controller.getInfo("F").getPosition().toDegrees() - 67) <= 1);

        // testing supportive entities and in range with multi Realay Satellites
        controller.createDevice("L1", "LaptopDevice", Angle.fromDegrees(216));
        controller.createDevice("D1", "DesktopDevice", Angle.fromDegrees(21));
        controller.createDevice("H1", "HandheldDevice", Angle.fromDegrees(131));
        controller.createSatellite("T1", "TeleportingSatellite", 92278,
            Angle.fromDegrees(73));
        controller.createSatellite("S1", "StandardSatellite", 86600,
            Angle.fromDegrees(281));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "F", "E", "D", "A", "S1", "T1"),
            controller.communicableEntitiesInRange("L1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("A", "C", "D", "E", "F", "B", "T1"),
            controller.communicableEntitiesInRange("D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "A", "D", "E", "F", "C", "S1", "T1"),
            controller.communicableEntitiesInRange("H1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("B", "C", "F", "E", "D", "A", "L1", "D1", "H1", "S1"),
            controller.communicableEntitiesInRange("T1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("A", "C", "D", "E", "F", "B", "L1", "H1", "T1"),
            controller.communicableEntitiesInRange("S1"));
    }

    @Test
    public void testRelayMovement() {
        // Task 2
        // Example from the specification
        BlackoutController controller = new BlackoutController();

        // Creates 1 satellite and 2 devices
        // Gets a device to send a file to a satellites and gets another device to download it.
        // StandardSatellites are slow and transfer 1 byte per minute.
        controller.createSatellite("Satellite1", "RelaySatellite", 100 + RADIUS_OF_JUPITER,
                                Angle.fromDegrees(180));

        // moves in negative direction
        assertEquals(
                        new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                        "RelaySatellite"),
                        controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(178.77), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(177.54), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(176.31), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(170.18), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(24);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // edge case
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // coming back
        controller.simulate(1);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(5);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(146.85), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // getting back again
        controller.simulate(27);
        assertEquals(
                        new EntityInfoResponse("Satellite1", Angle.fromDegrees(180), 100 + RADIUS_OF_JUPITER,
                                        "RelaySatellite"),
                        controller.getInfo("Satellite1"));
        controller.simulate(9);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(191.07), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        // moving back after hit edge 190 degrees
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(189.84), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
        controller.simulate(41);
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(139.49), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));

        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(140.72), 100 + RADIUS_OF_JUPITER,
                        "RelaySatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testTeleportingMovement() {
        // Test for expected teleportation movement behaviour
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(0));

        controller.simulate();
        Angle clockwiseOnFirstMovement = controller.getInfo("Satellite1").getPosition();
        controller.simulate();
        Angle clockwiseOnSecondMovement = controller.getInfo("Satellite1").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 250 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(250);

        // Verify that Satellite1 is now at theta=0
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(0), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        // moveing clockwise
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(359.284), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("Satellite1"));

        controller.simulate(251);
        // teleport again
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(0), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        // moveing anti-clockwise
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(0.716), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("Satellite1"));

        controller.createSatellite("B", "TeleportingSatellite", 10000 + RADIUS_OF_JUPITER,
                        Angle.fromDegrees(181));

        controller.simulate();
        clockwiseOnFirstMovement = controller.getInfo("B").getPosition();
        controller.simulate();
        clockwiseOnSecondMovement = controller.getInfo("B").getPosition();
        assertTrue(clockwiseOnSecondMovement.compareTo(clockwiseOnFirstMovement) == 1);

        // It should take 499 simulations to reach theta = 180.
        // Simulate until Satellite1 reaches theta=180
        controller.simulate(499);

        // Verify that Satellite1 is now at theta=0
        assertEquals(new EntityInfoResponse("B", Angle.fromDegrees(0), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("B"));
        controller.simulate();
        // moveing clockwise
        assertEquals(new EntityInfoResponse("B", Angle.fromDegrees(359.284), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("B"));

        controller.simulate(251);
        // teleport again
        assertEquals(new EntityInfoResponse("B", Angle.fromDegrees(0), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("B"));
        controller.simulate();
        // moveing anti-clockwise
        assertEquals(new EntityInfoResponse("B", Angle.fromDegrees(0.716), 10000 + RADIUS_OF_JUPITER,
                        "TeleportingSatellite"), controller.getInfo("B"));
    }

    @Test
    public void testSendFileWhileTeleporting() {
        // device -> satellite
        BlackoutController controller = new BlackoutController();
        controller.createDevice("H1", "HandheldDevice", Angle.fromDegrees(164));
        controller.createSatellite("T1", "TeleportingSatellite", 93506,
                                Angle.fromDegrees(178));
        controller.addFileToDevice("H1", "F1", "tttttttttTTTTTTttttTTTTljtklk654ttt64684ttttttttttttttt");
        assertDoesNotThrow(() -> controller.sendFile("F1", "H1", "T1"));
        assertEquals(new FileInfoResponse("F1", "", 55, false),
            controller.getInfo("T1").getFiles().get("F1"));
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "tttttttttTTTTTTttttTTTTljtklk654ttt64684ttttttttttttttt", 55, true),
            controller.getInfo("H1").getFiles().get("F1"));
        controller.simulate(3);
        assertEquals(null,
            controller.getInfo("T1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F1", "TTTTTTTTTTljklk65464684", 23, true),
            controller.getInfo("H1").getFiles().get("F1"));

        // satellite -> device & satellite -> satellite
        BlackoutController controller2 = new BlackoutController();
        controller2.createDevice("H1", "HandheldDevice", Angle.fromDegrees(164));
        controller2.createDevice("L1", "LaptopDevice", Angle.fromDegrees(144));
        controller2.createSatellite("T1", "TeleportingSatellite", 101950,
                                Angle.fromDegrees(175));
        controller2.createSatellite("S1", "StandardSatellite", 94028,
                                Angle.fromDegrees(157));
        controller2.addFileToDevice("H1", "F1", "tTtTt1234t5678abc");
        assertDoesNotThrow(() -> controller2.sendFile("F1", "H1", "T1"));
        assertEquals(new FileInfoResponse("F1", "", 17, false),
            controller2.getInfo("T1").getFiles().get("F1"));
        controller2.simulate(8);
        assertEquals(new FileInfoResponse("F1", "tTtTt1234t5678abc", 17, true),
            controller2.getInfo("T1").getFiles().get("F1"));
        assertDoesNotThrow(() -> controller2.sendFile("F1", "T1", "L1"));
        assertDoesNotThrow(() -> controller2.sendFile("F1", "T1", "S1"));
        controller2.simulate();
        assertEquals(new FileInfoResponse("F1", "tTtTt1234t5678abc", 17, true),
            controller2.getInfo("T1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F1", "TT12345678abc", 13, true),
            controller2.getInfo("S1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F1", "TT12345678abc", 13, true),
            controller2.getInfo("L1").getFiles().get("F1"));
    }

    @Test
    public void testSendFileWhenOutOfRange() {
        // satellite can send to device eventhough device couldn't see satellite due to it's range
        BlackoutController controller = new BlackoutController();
        controller.createDevice("L1", "LaptopDevice", Angle.fromDegrees(8));
        controller.createDevice("H1", "HandheldDevice", Angle.fromDegrees(25));
        controller.createSatellite("S1", "StandardSatellite", 118340,
                                Angle.fromDegrees(356));
        controller.addFileToDevice("L1", "F1", "1234567");
        assertDoesNotThrow(() -> controller.sendFile("F1", "L1", "S1"));
        controller.simulate(12);
        assertDoesNotThrow(() -> controller.sendFile("F1", "S1", "H1"));
        controller.simulate(7);
        assertEquals(new FileInfoResponse("F1", "1234567", 7, true),
            controller.getInfo("H1").getFiles().get("F1"));

        // get out of communication range while transferring a file
        controller.createDevice("H2", "HandheldDevice", Angle.fromDegrees(20));
        controller.createSatellite("S2", "StandardSatellite", 110441,
            Angle.fromDegrees(347));
        assertDoesNotThrow(() -> controller.sendFile("F1", "L1", "S2"));
        controller.simulate(7);
        assertDoesNotThrow(() -> controller.sendFile("F1", "S2", "H2"));
        controller.simulate(6);
        assertEquals(new FileInfoResponse("F1", "123456", 7, false),
            controller.getInfo("H2").getFiles().get("F1"));
        controller.simulate();
        assertEquals(null,
            controller.getInfo("H2").getFiles().get("F1"));
    }

    @Test
    public void testEverything() {
        BlackoutController controller = new BlackoutController();
        // testing the supposed starting movement direction
        controller.createSatellite("R1", "RelaySatellite", 90774,
                                Angle.fromDegrees(100));
        controller.createSatellite("R2", "RelaySatellite", 87770,
                                Angle.fromDegrees(357));
        controller.createSatellite("S1", "StandardSatellite", 102387,
                                Angle.fromDegrees(209));
        controller.createSatellite("S2", "StandardSatellite", 90518,
                                Angle.fromDegrees(246));
        controller.createSatellite("T1", "TeleportingSatellite", 89221,
                                Angle.fromDegrees(60));
        controller.createSatellite("T2", "TeleportingSatellite", 90608,
                                Angle.fromDegrees(315));
        controller.createDevice("L1", "LaptopDevice", Angle.fromDegrees(216));
        controller.createDevice("D1", "DesktopDevice", Angle.fromDegrees(21));
        controller.createDevice("H1", "HandheldDevice", Angle.fromDegrees(131));
        controller.createDevice("L2", "LaptopDevice", Angle.fromDegrees(343));
        controller.createDevice("D2", "DesktopDevice", Angle.fromDegrees(285));
        controller.createDevice("H2", "HandheldDevice", Angle.fromDegrees(88));
        assertListAreEqualIgnoringOrder(Arrays.asList("R1", "R2", "T2", "H1", "H2", "L2", "D1"),
            controller.communicableEntitiesInRange("T1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("T1", "T2", "R2"),
            controller.communicableEntitiesInRange("D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("R1", "T1"),
            controller.communicableEntitiesInRange("H2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "S2"),
            controller.communicableEntitiesInRange("L1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("T1", "D1", "L2", "D2", "S2", "R2"),
            controller.communicableEntitiesInRange("T2"));
        // testing moving many times and listing communicable entities
        controller.simulate(60);
        assertTrue(Math.abs(controller.getInfo("T1").getPosition().toDegrees() - 99) <= 1);
        assertTrue(Math.abs(controller.getInfo("T2").getPosition().toDegrees() - 353) <= 1);
        assertTrue(Math.abs(controller.getInfo("R1").getPosition().toDegrees() - 157) <= 1);
        assertTrue(Math.abs(controller.getInfo("R2").getPosition().toDegrees() - 55) <= 1);
        assertTrue(Math.abs(controller.getInfo("S1").getPosition().toDegrees() - 126) <= 1);
        assertTrue(Math.abs(controller.getInfo("S2").getPosition().toDegrees() - 151) <= 1);

        assertListAreEqualIgnoringOrder(Arrays.asList("R1", "R2", "T2", "T1", "S2", "H2", "H1"),
            controller.communicableEntitiesInRange("S1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("T1", "S1", "S2", "R1"),
            controller.communicableEntitiesInRange("H1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "T1", "H2", "D1", "T2"),
            controller.communicableEntitiesInRange("R2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("S1", "T1", "H2", "D1", "R2", "L2"),
            controller.communicableEntitiesInRange("T2"));

        // testing moving many times and listing communicable entities
        controller.simulate(120);
        assertTrue(Math.abs(controller.getInfo("T1").getPosition().toDegrees() - 176) <= 1);
        assertTrue(Math.abs(controller.getInfo("T2").getPosition().toDegrees() - 68) <= 1);
        assertTrue(Math.abs(controller.getInfo("R1").getPosition().toDegrees() - 168) <= 1);
        assertTrue(Math.abs(controller.getInfo("R2").getPosition().toDegrees() - 173) <= 1);
        assertTrue(Math.abs(controller.getInfo("S1").getPosition().toDegrees() - 318) <= 1);
        assertTrue(Math.abs(controller.getInfo("S2").getPosition().toDegrees() - 321) <= 1);
        assertListAreEqualIgnoringOrder(Arrays.asList("H2"),
            controller.communicableEntitiesInRange("T2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("R1", "T1", "H1"),
            controller.communicableEntitiesInRange("R2"));

        controller.simulate(11);

        // test sending files and expected exception
        controller.addFileToDevice("D1", "F1", "123456789123456789");
        assertDoesNotThrow(() -> controller.sendFile("F1", "D1", "T1"));
        assertEquals(new FileInfoResponse("F1", "", 18, false),
            controller.getInfo("T1").getFiles().get("F1"));

        // checking order of throwing exception
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () ->
            controller.sendFile("F1", "T1", "L2"));
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () ->
            controller.sendFile("F1", "T1", "S2"));
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () ->
            controller.sendFile("F1", "T1", "D1"));

        controller.simulate();
        assertThrows(FileTransferException.VirtualFileAlreadyExistsException.class, () ->
            controller.sendFile("F1", "D1", "T1"));
        assertThrows(FileTransferException.VirtualFileNotFoundException.class, () ->
            controller.sendFile("F1", "T1", "D1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("L2", "S2", "S1", "D1"),
            controller.communicableEntitiesInRange("T1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("T1"),
            controller.communicableEntitiesInRange("D1"));
        assertEquals(new FileInfoResponse("F1", "123456789123456", 18, false),
            controller.getInfo("T1").getFiles().get("F1"));
        controller.addFileToDevice("L2", "F2", "123456789123456789");

        // uploading 2 files at the time
        assertDoesNotThrow(() -> controller.sendFile("F2", "L2", "T1"));
        assertEquals(new FileInfoResponse("F2", "", 18, false),
            controller.getInfo("T1").getFiles().get("F2"));

        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "123456789123456789", 18, true),
            controller.getInfo("T1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F2", "1234567", 18, false),
            controller.getInfo("T1").getFiles().get("F2"));
        controller.simulate();
        assertEquals(new FileInfoResponse("F2", "123456789123456789", 18, true),
            controller.getInfo("T1").getFiles().get("F2"));

        // exception
        assertDoesNotThrow(() -> controller.sendFile("F1", "T1", "S1"));
        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class, () ->
            controller.sendFile("F1", "T1", "S1"));
        assertDoesNotThrow(() -> controller.sendFile("F2", "T1", "S2"));
        assertThrows(FileTransferException.VirtualFileNoBandwidthException.class, () ->
            controller.sendFile("F1", "T1", "S2"));
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "1", 18, false),
            controller.getInfo("S1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F2", "1", 18, false),
            controller.getInfo("S2").getFiles().get("F2"));

        // test sending bandwidth
        controller.simulate();
        controller.createSatellite("R3", "RelaySatellite", 96718,
                                Angle.fromDegrees(30));
        assertDoesNotThrow(() -> controller.sendFile("F1", "T1", "T2"));

        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "123", 18, false),
            controller.getInfo("T2").getFiles().get("F1"));
        assertDoesNotThrow(() -> controller.sendFile("F1", "T1", "L2"));

        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "12345", 18, false),
            controller.getInfo("T2").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F1", "12", 18, false),
            controller.getInfo("L2").getFiles().get("F1"));
    }
}
