public class SelfCheckOut extends Server {

    SelfCheckOut(int id, double nextAvailTime, Queue queue) {
        super(id, nextAvailTime, queue, () -> 0.0);
    }

    SelfCheckOut(Server server, double nextAvailTime, Queue queue) {
        super(server, nextAvailTime, queue);
    }

    SelfCheckOut(Server server, Queue queue) {
        super(server, server.getNextAvailTime(), queue);
    }

    public boolean isSCO() {
        return true;
    }
    
    @Override
    public String toString() {
        return String.format("self-check %s", super.getId());
    }

}
