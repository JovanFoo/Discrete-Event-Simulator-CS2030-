public class Serve extends Event {
    private final Server server;
    private final boolean isDuplicate;

    Serve(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.isDuplicate = false;
    }

    public double getEventTime() {
        return eventTime;
    }

    public Customer getCustomer() {
        return this.customer;
    }

    public boolean gotNextEvent() {
        return true;
    }

    public boolean isDuplicate() {
        return this.isDuplicate;
    }

    public Pair<Store, Event> execute(Store store) {
        double updatedTime = eventTime + customer.getServiceTime();
        Server updatedServer;

        // update serverList with updated time
        if (server.isSCO()) {
            updatedServer = new SelfCheckOut(this.server,
                    updatedTime, this.server.getQueue().updateQueue());
        } else {
            updatedServer = new Server(this.server,
                    updatedTime, this.server.getQueue().updateQueue());
        }

        int scoServerIdx = updatedServer.getId() - 1;
        ImList<Server> newServerList = store.getServerList().set(this.server.getId() - 1,
                updatedServer);

        if (server.isSCO()) {
            // update SCO serverlist queues
            for (int i = 0; i < store.getServerList().size(); i++) {
                Server otherServer = store.getServerList().get(i);
                Queue updatedQueue = updatedServer.getQueue();
                // update other SCOs except current SCO
                if (otherServer.isSCO() && i != scoServerIdx) {
                    newServerList = newServerList.set(i, new SelfCheckOut(otherServer,
                            otherServer.getNextAvailTime(), updatedQueue));
                }
            }
        }
        // update store with newServerList and Data(adding 1 to customerServed)
        store = new Store(newServerList, store.getData().addCustomerServed());

        // create done event and return new pair
        Done doneEvent = new Done(customer, updatedTime, updatedServer);
        return new Pair<Store, Event>(store, doneEvent);
    }

    @Override
    public String toString() {
        return String.format("%.3f %d serves by %s\n",
                eventTime, this.customer.getId(), this.server.toString());
    }
}
