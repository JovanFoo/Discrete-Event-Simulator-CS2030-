public class Done extends Event {
    private final Server server;
    private final boolean isDuplicate;

    Done(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.isDuplicate = false;
    }

    Done(Customer customer, double eventTime, Server server, boolean isDuplicate) {
        super(customer, eventTime);
        this.server = server;
        this.isDuplicate = isDuplicate;
    }

    public double getEventTime() {
        return eventTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public boolean isDuplicate() {
        return this.isDuplicate;
    }

    public Pair<Store, Event> execute(Store store) {
        Server serverStore = store.getServerList().get(this.server.getId() - 1);
        double restTime = serverStore.getRestTime();
        Server updatedServer;
        
        if (server.isSCO()) {
            updatedServer = new SelfCheckOut(serverStore,
                    eventTime + restTime, serverStore.getQueue());
        } else {
            updatedServer = new Server(serverStore,
                    eventTime + restTime, serverStore.getQueue());
        }

        ImList<Server> newServerList = store.getServerList().set(this.server.getId() - 1,
                updatedServer);

        store = new Store(newServerList, store.getData());
        
        Done doneEvent = new Done(customer, eventTime + restTime, server, true);
        return new Pair<Store, Event>(store, doneEvent);
    }

    public boolean gotNextEvent() {
        return false;
    }

    @Override
    public String toString() {
        return String.format("%.3f %d done serving by %s\n",
                this.eventTime, customer.getId(), this.server.toString());
    }
}
