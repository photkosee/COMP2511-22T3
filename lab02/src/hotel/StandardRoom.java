package hotel;

import org.json.JSONArray;
import org.json.JSONObject;

public class StandardRoom extends Room {

    @Override
    public JSONObject toJSON() {
        JSONObject room = new JSONObject();
        room.put("type", "standard");
        JSONArray bookingsArray = new JSONArray();
        for (Booking booking : bookings) {
            bookingsArray.put(booking.toJSON());
        }
        room.put("bookings", bookingsArray);
        return room;
    }
    
    @Override
    public void printWelcomeMessage() {
        System.out.println("Welcome to your standard room. Enjoy your stay :)");
    }
    
}