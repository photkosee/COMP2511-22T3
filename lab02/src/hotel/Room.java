package hotel;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

public abstract class Room {

    protected List<Booking> bookings = new ArrayList<Booking>();

    /**
     * Checks if the room is not booked out during the given time.
     * If so, creates a booking for the room at that time.
     * @param arrival
     * @param departure
     * @return The booking object if the booking succeeded, null if failed
     */
    public Booking book(LocalDate arrival, LocalDate departure) {
        for (Booking booking : bookings) {
            if (booking.overlaps(arrival, departure)) {
                return null;
            }
        }

        Booking booking = new Booking(arrival, departure);
        bookings.add(booking);
        return booking;
    }

    /**
     * @return A JSON object of the form:
     * {
     *  "bookings": [ each booking as a JSON object, in order of creation ],
     *  "type": the type of the room (standard, ensuite, penthouse)
     * }
     */
    public abstract JSONObject toJSON();

    /**
     * Prints a welcome message to the guest staying in the room.
     */
    public abstract void printWelcomeMessage();

}
