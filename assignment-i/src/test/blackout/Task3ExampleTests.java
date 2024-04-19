package blackout;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import unsw.blackout.BlackoutController;
import unsw.response.models.FileInfoResponse;
import unsw.response.models.EntityInfoResponse;
import unsw.utils.Angle;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static unsw.utils.MathsHelper.RADIUS_OF_JUPITER;

import java.util.Arrays;

import static blackout.TestHelpers.assertListAreEqualIgnoringOrder;

@TestInstance(value = Lifecycle.PER_CLASS)
public class Task3ExampleTests {
    @Test
    public void testEntitiesInRangeElephant() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "ElephantSatellite", 1000 + RADIUS_OF_JUPITER, Angle.fromDegrees(315));
        controller.createSatellite("Satellite2", "TeleportingSatellite", 1000 + RADIUS_OF_JUPITER,
            Angle.fromDegrees(315));
        controller.createDevice("DeviceB", "LaptopDevice", Angle.fromDegrees(310));
        controller.createDevice("DeviceC", "HandheldDevice", Angle.fromDegrees(320));
        controller.createDevice("DeviceE", "DesktopDevice", Angle.fromDegrees(315));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceE", "DeviceB"),
            controller.communicableEntitiesInRange("Satellite1"));
        assertListAreEqualIgnoringOrder(Arrays.asList("DeviceB", "DeviceC", "DeviceE"),
            controller.communicableEntitiesInRange("Satellite2"));
        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2", "Satellite1"),
            controller.communicableEntitiesInRange("DeviceB"));

        assertListAreEqualIgnoringOrder(Arrays.asList("Satellite2"), controller.communicableEntitiesInRange("DeviceC"));
    }

    @Test
    public void testElephantMovement() {
        BlackoutController controller = new BlackoutController();

        controller.createSatellite("Satellite1", "ElephantSatellite", 100 + RADIUS_OF_JUPITER, Angle.fromDegrees(340));
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(340), 100 + RADIUS_OF_JUPITER,
            "ElephantSatellite"), controller.getInfo("Satellite1"));
        controller.simulate();
        assertEquals(new EntityInfoResponse("Satellite1", Angle.fromDegrees(337.95), 100 + RADIUS_OF_JUPITER,
            "ElephantSatellite"), controller.getInfo("Satellite1"));
    }

    @Test
    public void testSendFileElephant() {
        // device -> satellite
        BlackoutController controller = new BlackoutController();
        controller.createDevice("L1", "LaptopDevice", Angle.fromDegrees(216));
        controller.createSatellite("E1", "ElephantSatellite", 85593,
                                Angle.fromDegrees(190));
        controller.simulate(5);
        controller.addFileToDevice("L1", "F1", "123456789123456789123");
        assertDoesNotThrow(() -> controller.sendFile("F1", "L1", "E1"));
        assertEquals(new FileInfoResponse("F1", "", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        // now not visible to each other
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        // back to visible for 1 tick
        controller.simulate(172);
        assertEquals(new FileInfoResponse("F1", "", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "12345678912345678912", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "123456789123456789123", 21, true),
            controller.getInfo("E1").getFiles().get("F1"));
    }

    @Test
    public void testKnapsackSolution() {
        // device -> satellite
        BlackoutController controller = new BlackoutController();
        controller.createDevice("L1", "LaptopDevice", Angle.fromDegrees(216));
        controller.createSatellite("E1", "ElephantSatellite", 85593,
                                Angle.fromDegrees(190));
        controller.simulate(4);
        controller.addFileToDevice("L1", "F1", "123456789123456789123");
        assertDoesNotThrow(() -> controller.sendFile("F1", "L1", "E1"));
        assertEquals(new FileInfoResponse("F1", "", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        controller.simulate();
        controller.addFileToDevice("L1", "F2", "123456789123456789123");
        assertDoesNotThrow(() -> controller.sendFile("F2", "L1", "E1"));
        assertEquals(new FileInfoResponse("F1", "12345678912345678912", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F2", "", 21, false),
            controller.getInfo("E1").getFiles().get("F2"));
        // now not visible to each other
        controller.simulate();
        assertEquals(new FileInfoResponse("F1", "12345678912345678912", 21, false),
            controller.getInfo("E1").getFiles().get("F1"));
        assertEquals(new FileInfoResponse("F2", "", 21, false),
            controller.getInfo("E1").getFiles().get("F2"));

        // recieve files from another device
        controller.createDevice("L2", "LaptopDevice", Angle.fromDegrees(225));
        controller.addFileToDevice("L2", "F3", "1234567891234567891234567891");
        controller.addFileToDevice("L2", "F4", "123456789123456789123");
        assertDoesNotThrow(() -> controller.sendFile("F3", "L2", "E1"));
        assertDoesNotThrow(() -> controller.sendFile("F4", "L2", "E1"));

        // check if knapsack make the right choice
        assertEquals(null,
            controller.getInfo("E1").getFiles().get("F2"));
        assertEquals(new FileInfoResponse("F3", "", 28, false),
            controller.getInfo("E1").getFiles().get("F3"));
        assertEquals(new FileInfoResponse("F4", "", 21, false),
            controller.getInfo("E1").getFiles().get("F4"));
    }
}
