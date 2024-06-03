//interface
abstract class Event {
    protected final Customer customer;
    protected final double eventTime;

    //execute something using pair class, event and server
    public Event(Customer customer, double eventTime) {
        this.customer = customer;
        this.eventTime = eventTime;
    }

    public double getEventTime() {
        return this.eventTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    //to prevent infinite while loop in checkAvail (simulator class)
    abstract boolean gotNextEvent();

    abstract boolean isDuplicate();

    abstract Pair<Store, Event> execute(Store store);

}