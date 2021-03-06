package cs2030.simulator;

public class ServeEvent extends Event {

    private final Customer  customer;
    private final double   eventTime;
    private final int linkedServerID;

    ServeEvent(Customer customer, double eventTime, int linkedServerID) {

        super(customer, 
              eventTime, 
              linkedServerID,
              shop -> {
                Server oldServer   = shop.find(x -> x.getID() == linkedServerID).get();
                double servingTime = customer.getServiceTime();
                double nextAvailableTime = eventTime + servingTime;
                Customer nextCustomer = oldServer.pollNextCustomer();

                Server updatedServer = new Server(oldServer.getID(),
                                                  false,
                                                  oldServer.hasQueue(),
                                                  nextAvailableTime,
                                                  nextCustomer);

                updatedServer.copyQueue(oldServer);

                DoneEvent newDE = new DoneEvent(customer,
                                                nextAvailableTime,
                                                linkedServerID);

                return Pair.of(shop.replace(updatedServer), newDE);
              });
        this.customer       = customer;
        this.eventTime      = eventTime;
        this.linkedServerID = linkedServerID;
    }

    public String toString() {

        return String.format("%.3f %d served by server %d", 
                             this.eventTime,
                             this.customer.getID(),
                             linkedServerID);
    }
}