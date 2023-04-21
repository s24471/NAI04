import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static int k;
    public static int maxIterations = 100000;
    public static ArrayList<Value> arr;
    public static ArrayList<Cluster> clusters;


    public static void main(String[] args) {
        new Main();
    }

    public Main(){
        Scanner scanner = new Scanner(System.in);
        try {
            arr = new ArrayList<>();
            Scanner s = new Scanner(new File("iris_training.txt"));
            while (s.hasNext()) {
                Value value = new Value(s.nextLine().split("\t"));
                arr.add(value);
            }
            s.close();

            System.out.println("Podaj k: (lub -1 by zakonczyc)");
            k = scanner.nextInt();
            initializeClusters();
            int iteration = 0;
            boolean changed = true;
            while (iteration < maxIterations && changed) {
                double sumDistances = calculateSumDistances();
                System.out.println("Sum of distances in iteration " + iteration + ": " + sumDistances);
                iteration++;
                changed =  groupValues();
            }
            for (Cluster cluster : clusters) {
                System.out.println(cluster);
                System.out.println("Entropy: " + calculateEntropy(cluster));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
    public static double calculateSumDistances() {
        double sumDistances = 0;
        for (Cluster cluster : clusters) {
            for (Value value : cluster.values) {
                double distance = distance(value, cluster.centroid);
                sumDistances += distance;
            }
        }
        return sumDistances;
    }

    public static double calculateEntropy(Cluster cluster) {
        HashMap<String, Integer> countByClass = new HashMap<>();
        for (Value value : cluster.values) {
            String className = value.name;
            countByClass.put(className, countByClass.getOrDefault(className, 0) + 1);
        }
        double entropy = 0;
        for (int count : countByClass.values()) {
            double p = (double) count / cluster.values.size();
            entropy -= p * Math.log(p);
        }
        return entropy;
    }

    public void initializeClusters(){
        clusters = new ArrayList<>();
        ArrayList<Value> tmp = new ArrayList<>(arr);
        for (int i = 0; i < k; i++) {
            Value centroid = tmp.get((int) (Math.random() * tmp.size()));
            tmp.remove(centroid);
            clusters.add(new Cluster(centroid));

        }
    }
    public boolean groupValues() {
        boolean clustersChanged = false;

        for (Cluster cluster : clusters) {
            cluster.clearValues();
        }

        for (Value value : arr) {
            double minDistance = Double.MAX_VALUE;
            Cluster nearestCluster = null;

            for (Cluster cluster : clusters) {
                double distance = distance(value, cluster.centroid);
                if (distance < minDistance) {
                    minDistance = distance;
                    nearestCluster = cluster;
                }
            }

            nearestCluster.addValue(value);
        }

        // Calculate the new centroid for each cluster
        for (Cluster cluster : clusters) {
            ArrayList<Double> oldCentroid = cluster.centroid;
            cluster.newCentroid();
            if (!oldCentroid.equals(cluster.centroid)) {
                clustersChanged = true;
            }
        }

        return clustersChanged;
    }
    public static double distance(Value k1, ArrayList<Double> arr) {
        double dist = 0;
        for (int i = 0; i < k1.arr.size(); i++) {
            double tmp =arr.get(i) - k1.arr.get(i);
            dist += tmp * tmp;
        }
        return dist;
    }


}
