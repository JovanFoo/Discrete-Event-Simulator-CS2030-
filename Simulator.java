import java.util.function.Supplier;

class Simulator {
    private final int numOfServers;
    private final int numOfSelfChecks;
    private final int qMax;
    private final ImList<Double> arrivalTimes;
    private final Supplier<Double> serviceTimes;
    private final Supplier<Double> restTime;

    public Simulator(int numOfServers, int numOfSelfChecks, int qMax,
            ImList<Double> arrivalTimes, Supplier<Double> serviceTimes,
            Supplier<Double> restTime) {
        this.numOfServers = numOfServers;
        this.numOfSelfChecks = numOfSelfChecks;
        this.qMax = qMax;
        this.arrivalTimes = arrivalTimes;
        this.serviceTimes = serviceTimes;
        this.restTime = restTime;
    }

    private ImList<Server> initServerList() {
        ImList<Customer> emptyQueueList = new ImList<Customer>();
        ImList<Server> serverList = new ImList<Server>();
        //add servers to serverlist
        for (int i = 1; i <= this.numOfServers; i++) {
            serverList = serverList.add(new Server(i, 0,
                    new Queue(this.qMax, emptyQueueList), restTime));
        }

        //add selfcheckout to serverlist
        for (int i = 1; i <= this.numOfSelfChecks; i++) {
            serverList = serverList.add(new SelfCheckOut(this.numOfServers +
                i, 0, new Queue(this.qMax, emptyQueueList)));
        }

        return serverList;
    }

    private ImList<Customer> initCustomerList() {
        ImList<Customer> customerList = new ImList<Customer>();
        for (int i = 1; i <= this.arrivalTimes.size(); i++) {
            customerList = customerList.add(new Customer(i, arrivalTimes.get(i - 1),
                    this.serviceTimes));
        }
        return customerList;
    }

    public String simulate() {
        String str = "";
        ImList<Server> serverList = initServerList();
        ImList<Customer> customerList = initCustomerList();

        str += checkAvail(serverList, customerList);
        return str;
    }

    public String checkAvail(ImList<Server> serverList, ImList<Customer> customerList) {

        String str = "";
        PQ<Event> pq = new PQ<Event>(new EventComp()); // Store events

        for (int i = 0; i < customerList.size(); i++) {
            Customer customer = customerList.get(i);
            // Add in arrival event into PQList
            pq = pq.add(new Arrive(customer, customer.getArrivalTime()));
        }

        Data data = new Data(0, 0, 0);
        Store store = new Store(serverList, data);

        while (!pq.isEmpty()) {
            Pair<Event, PQ<Event>> priorityPair = pq.poll();
            Event event = priorityPair.first();

            // Update store and pq with result from event execute
            Pair<Store, Event> storeEventPair = event.execute(store);
            store = storeEventPair.first();

            if (event.gotNextEvent()) {
                pq = priorityPair.second().add(storeEventPair.second());
            } else {
                pq = priorityPair.second();
            }

            str += !event.isDuplicate() ? event.toString() : "";
        }

        if (store.getData().getCustomerServed() == 0) {
            str += String.format("[%.3f %d %d]",
            0.0,
            store.getData().getCustomerServed(),
            store.getData().getCustomerLeft());
        } else {
            str += String.format("[%.3f %d %d]",
            (store.getData().getWaitTime() / store.getData().getCustomerServed()),
            store.getData().getCustomerServed(),
            store.getData().getCustomerLeft());
        }
        
        return str;
    }

}
