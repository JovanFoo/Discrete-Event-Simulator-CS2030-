public class Store {
    private final ImList<Server> serverList;
    private final Data data;

    Store(ImList<Server> serverList, Data data) {
        this.serverList = serverList;
        this.data = data;
    }

    public ImList<Server> getServerList() {
        return this.serverList;
    }

    public Data getData() {
        return this.data;
    }
}
