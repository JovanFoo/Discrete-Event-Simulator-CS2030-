public class Wait extends Event {
    private final Server server;
    private final boolean isDuplicate;

    Wait(Customer customer, double eventTime, Server server) {
        super(customer, eventTime);
        this.server = server;
        this.isDuplicate = false;
    }

    Wait(Customer customer, double eventTime, Server server, boolean isDuplicate) {
        super(customer, eventTime);
        this.server = server;
        this.isDuplicate = isDuplicate;
    }

    public boolean gotNextEvent() {
        return true;
    }

    public boolean isDuplicate() {
        return this.isDuplicate;
    }

    // wait event still needs to keep track of the customer waittime
    public Pair<Store, Event> execute(Store store) {

        Server serverStore = store.getServerList().get(this.server.getId() - 1);
        double nextAvailTime = server.getNextAvailTime();
        Event nextEvent;

        //if server is available
        if (serverStore.isAvail(customer)) {
            nextEvent = new Serve(customer, nextAvailTime, serverStore);
        } else {
            //Human server
            if (!serverStore.isSCO()) {
                nextEvent = new Wait(customer.updateArrivalTime(nextAvailTime), 
                nextAvailTime, serverStore, true);

                //Update store
                store = new Store(store.getServerList(),
                    store.getData().updateWaitTime(nextAvailTime - customer.getArrivalTime()));
            } else {
                //SCO, find the one with earliest nextAvailTime
                int serverIdx = serverStore.getId() - 1;
                double newAvailTime = serverStore.getNextAvailTime();
                int newServerIdx = getEarliestServerIdx(store.getServerList(), 
                    newAvailTime, serverIdx);
                Server newServer = store.getServerList().get(newServerIdx);
                newAvailTime = newServer.getNextAvailTime();
                nextEvent = new Wait(customer.updateArrivalTime(newAvailTime), 
                newAvailTime, newServer, true);

                //Update store
                store = new Store(store.getServerList(),
                    store.getData().updateWaitTime(newAvailTime - customer.getArrivalTime()));
            }
        }

        return new Pair<Store, Event>(store, nextEvent);
    }

    public int getEarliestServerIdx(ImList<Server> serverList, double currentTime, int idx) {
        int index = idx; // original server index
        for (int i = 0; i < serverList.size(); i++) {
            Server tempServer = serverList.get(i);
            if (tempServer.isSCO()) {
                if (tempServer.getNextAvailTime() <= currentTime) {
                    index = i;
                    currentTime = tempServer.getNextAvailTime();
                }
            }
        }
        return index;
    }

    @Override
    public String toString() {
        return String.format("%.3f %s waits at %s\n",
                this.eventTime, this.customer.getId(), this.server.toString());
    }
}
