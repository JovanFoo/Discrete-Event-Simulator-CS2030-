public class Data {
    private final double waitTime;
    private final int customerLeft;
    private final int customerServed;

    Data(double waitTime, int customerLeft, int customerServed) {
        this.waitTime = waitTime;
        this.customerLeft = customerLeft;
        this.customerServed = customerServed;
    }

    public Data updateWaitTime(double time) {
        return new Data(waitTime + time, customerLeft, customerServed);
    }

    public Data addCustomerServed() {
        return new Data(waitTime, customerLeft, customerServed + 1);
    }

    public Data addCustomerLeft() {
        return new Data(waitTime, customerLeft + 1, customerServed);
    }

    public double getWaitTime() {
        return this.waitTime;
    }

    public int getCustomerServed() {
        return this.customerServed;
    }

    public int getCustomerLeft() {
        return this.customerLeft;
    }

}
