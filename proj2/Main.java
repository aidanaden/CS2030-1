import cs2030.simulator.Simulator;
import cs2030.simulator.RNGImpl;
import cs2030.simulator.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;
import java.util.function.Supplier;

public class Main {

    public static void main(String[] args) {

        int seed;
        int serverCount;
        int maxQueue;
        int customerCount;
        double lambda;
        double mu;

        if (args.length == 5) {
            seed        = Integer.parseInt(args[0]);
            serverCount = Integer.parseInt(args[1]);
            maxQueue    = 1;
            customerCount = Integer.parseInt(args[2]);
            lambda      = Double.parseDouble(args[3]);
            mu          = Double.parseDouble(args[4]);
        } else {
            seed        = Integer.parseInt(args[0]);
            serverCount = Integer.parseInt(args[1]);
            maxQueue    = Integer.parseInt(args[2]);
            customerCount = Integer.parseInt(args[3]);
            lambda      = Double.parseDouble(args[4]);
            mu          = Double.parseDouble(args[5]);
        }

        RNGImpl rng = new RNGImpl(seed, lambda, mu, 0);

        ArrayList<Double> customerArrivals = new ArrayList<>();

        IntStream.range(1, customerCount + 1)
                 .forEach(x -> {                    
                    if (x == 1) {
                        customerArrivals.add(0.0);
                    } else {
                        double rngArrival = rng.genInterArrivalTime();
                        int    lastIndex  = customerArrivals.size() - 1;
                        double lastEntry  = customerArrivals.get(lastIndex);
                        customerArrivals.add(lastEntry + rngArrival);
                    }
                 });

        Supplier<Double> serviceTime = () -> rng.genServiceTime();

        Simulator sim = new Simulator(customerArrivals, 
                                      serverCount, 
                                      serviceTime, 
                                      maxQueue);
        sim.run();
    }
}