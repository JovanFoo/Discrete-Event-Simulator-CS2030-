public class Leave extends Event {
    private final boolean isDuplicate;

    Leave(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.isDuplicate = false;
    }

    Leave(Customer customer, double eventTime, boolean isDuplicate) {
        super(customer, eventTime);
        this.isDuplicate = isDuplicate;
    }
    
    public double getEventTime() {
        return eventTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    //return itself since there is no event to transit to
    public Pair<Store, Event> execute(Store store) {
        Leave newEvent = new Leave(customer, eventTime, true);
        return new Pair<Store,Event>(store, newEvent);
    }

    public boolean gotNextEvent() {
        return false;
    }

    public boolean isDuplicate() {
        return this.isDuplicate;
    }

    @Override
    public String toString() {
        return String.format("%.3f %d leaves\n",
        this.customer.getArrivalTime(), this.customer.getId());
    }
}
