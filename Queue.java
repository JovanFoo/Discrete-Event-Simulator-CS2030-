public class Queue {
    private final int qMax;
    private final ImList<Customer> queue;

    Queue(int qMax, ImList<Customer> queue) {
        this.qMax = qMax;
        this.queue = queue;
    }

    public ImList<Customer> getQueue() {
        return this.queue;
    }

    public int getSize() {
        return this.queue.size();
    }

    public Queue addQueue(Customer customer) {
        return new Queue(this.qMax, this.queue.add(customer));
    }

    public Queue updateQueue() {
        return new Queue(this.qMax, this.queue.remove(0));
    }

    public boolean queueFull() {
        if (this.queue.size() == qMax) {
            return true;
        } else {
            return false;
        }
    }
}
