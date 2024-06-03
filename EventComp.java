import java.util.Comparator;

public class EventComp implements Comparator<Event> {
    public int compare(Event event1, Event event2) {
        if (event1.getEventTime() > event2.getEventTime()) {
            return 1;
        } else if (event1.getEventTime() < event2.getEventTime()) {
            return -1;
        } else if (event1.getCustomer().getId() > event2.getCustomer().getId()) { 
            //time same so check customerID
            return 1;
        } else if (event1.getCustomer().getId() < event2.getCustomer().getId()) {
            return -1;
        } else {
            return 0;
        }
    }
}