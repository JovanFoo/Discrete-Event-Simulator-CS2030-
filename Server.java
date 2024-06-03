import java.util.function.Supplier;

class Server {
    private final int id;
    private final double nextAvailTime;
    private final Queue queue;
    private final Supplier<Double> restTime;

    public Server(Server server, double nextAvailTime, Queue queue) {
        this.id = server.id;
        this.nextAvailTime = nextAvailTime;
        this.queue = queue;
        this.restTime = server.restTime;
    }

    public Server(int id, double nextAvailTime, Queue queue, Supplier<Double> restTime) {
        this.id = id;
        this.nextAvailTime = nextAvailTime;
        this.queue = queue;
        this.restTime = restTime;
    }

    public int getId() {
        return id;
    }

    public boolean isSCO() {
        return false;
    }

    public double getNextAvailTime() {
        return nextAvailTime;
    }

    public double getRestTime() {
        return this.restTime.get();
    }

    public Queue getQueue() {
        return this.queue;
    }

    public int getQueueSize() {
        return this.queue.getSize();
    }

    public Queue addQueue(Customer customer) {
        return this.queue.addQueue(customer);
    }

    public Queue updateQueue() {
        return this.queue.updateQueue();
    }

    public boolean customerNext(Customer customer) {
        return customer == this.queue.getQueue().get(0);
    }

    public boolean queueFull() {
        return queue.queueFull();
    }

    public Server checkServer(int newServerID, double newEndTime, Queue newQueue) {
        return new Server(newServerID, newEndTime, newQueue, this.restTime);
    }

    public String toString() {
        return String.format("%s", this.id);
    }

    public boolean isAvail(Customer customer) {
        if (customer.getArrivalTime() >= nextAvailTime) {
            return true;
        } else {
            return false;
        }
    }

}
