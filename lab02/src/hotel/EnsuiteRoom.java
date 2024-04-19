package hotel;

import org.json.JSONArray;
import org.json.JSONObject;

public class EnsuiteRoom extends Room {

    @Override
    public JSONObject toJSON() {
        JSONObject room = new JSONObject();
        room.put("type", "ensuite");
        JSONArray bookingsArray = new JSONArray();
        for (Booking booking : bookings) {
            bookingsArray.put(booking.toJSON());
        }
        room.put("bookings", bookingsArray);
        return room;
    }

    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your beautiful ensuite room which overlooks the Sydney harbour. Enjoy your stay");
    }
    
}