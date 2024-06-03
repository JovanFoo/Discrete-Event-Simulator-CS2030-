public class Arrive extends Event {
    private final boolean isDuplicate;

    Arrive(Customer customer, double eventTime) {
        super(customer, eventTime);
        this.isDuplicate = false;
    }

    @Override
    public String toString() {
        return String.format("%.3f %d arrives\n",
                customer.getArrivalTime(), customer.getId());
    }

    public boolean gotNextEvent() {
        return true;
    }

    public boolean isDuplicate() {
        return this.isDuplicate;
    }

    // create a method to return serve/wait/leave as next event
    public Pair<Store, Event> execute(Store store) {
        for (int i = 0; i < store.getServerList().size(); i++) {
            // check if server is avail, yes then go to serveEvent
            Server server = store.getServerList().get(i);

            if (server.isAvail(customer)) {
                Serve serveEvent = new Serve(super.customer,
                        super.eventTime, store.getServerList().get(i));
                return new Pair<Store, Event>(store, serveEvent);
            }
        }

        for (int i = 0; i < store.getServerList().size(); i++) {
            Server server = store.getServerList().get(i);
            // server not available
            if (!server.queueFull()) { // queue is not full
                // create wait event
                Server newServer;
                if (server.isSCO()) {
                    newServer = new SelfCheckOut(server,
                            server.getNextAvailTime(),
                            server.getQueue().addQueue(customer));
                } else {
                    newServer = new Server(server, server.getNextAvailTime(),
                            server.getQueue().addQueue(customer));
                }
                ImList<Server> newServerList = store.getServerList().set(i,
                        newServer);

                if (server.isSCO()) {
                    // update SCO serverlist queues
                    for (int j = 0; j < store.getServerList().size(); j++) {
                        Server otherServer = store.getServerList().get(j);
                        Queue updatedQueue = newServer.getQueue();
                        if (otherServer.isSCO()) {
                            newServerList = newServerList.set(j, new SelfCheckOut(otherServer,
                            otherServer.getNextAvailTime(), updatedQueue));
                        }
                    }
                }

                store = new Store(newServerList, store.getData());
                
                Wait waitEvent = new Wait(super.customer,
                        super.eventTime, newServer);
                return new Pair<Store, Event>(store, waitEvent);
            }
        }

        Leave leaveEvent = new Leave(super.customer,
                super.eventTime);
        store = new Store(store.getServerList(), store.getData().addCustomerLeft());
        return new Pair<Store, Event>(store, leaveEvent);
    }
}
