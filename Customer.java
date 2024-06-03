import java.util.function.Supplier;

class Customer {
    private final int id;
    private final double arrivalTime;
    private final Supplier<Double> serviceTime;

    Customer(int id, double arrivalTimes, Supplier<Double> serviceTimes) {
        this.id = id;
        this.arrivalTime = arrivalTimes;
        this.serviceTime = serviceTimes;
    }

    public int getId() {
        return this.id;
    }

    public double getArrivalTime() {
        return this.arrivalTime;
    }

    public Customer updateArrivalTime(double time) {
        return new Customer(id, time, serviceTime);
    }

    public double getServiceTime() {
        return this.serviceTime.get();
    }

}
