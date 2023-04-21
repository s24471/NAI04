import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {

    public static int k;
    public static int maxIterations;
    public static ArrayList<Value> arr;
    public static ArrayList<Cluster> clusters;


    public static void main(String[] args) {
        new Main();
    }

    public Main(){
            Scanner scanner = new Scanner(System.in);
            importData("iris_training.txt"); //importData("iris_test.txt");
            System.out.println("Podaj k: (lub -1 by zakonczyc)");
            k = scanner.nextInt();
            System.out.println("Podaj maksymalna ilosc iteracji: (lub -1)");
            maxIterations = scanner.nextInt();

            initializeClusters();
            int iteration = 0;
            boolean changed = true;

            while ((iteration++ < maxIterations || maxIterations == -1) && changed) {
                System.out.println("Sum of distances in iteration " + iteration + ": " +  calculateSumDistances());
                changed =  groupValues();
            }
            for (Cluster cluster : clusters) {
                System.out.println(cluster);
                System.out.println("Entropy: " + calculateEntropy(cluster));
            }


    }
    public void importData(String name){
        try {
            arr = new ArrayList<>();
            Scanner s = new Scanner(new File(name));
            while (s.hasNext()) {
                arr.add(new Value(s.nextLine().split("\t")));
            }
            s.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
    public static double calculateSumDistances() {
        double sumDistances = 0;
        for (Cluster cluster : clusters) {
            for (Value value : cluster.values) {
                sumDistances += distance(value, cluster.centroid);
            }
        }
        return sumDistances;
    }

    public static double calculateEntropy(Cluster cluster) {
        HashMap<String, Integer> countByClass = new HashMap<>();
        for (Value value : cluster.values) {
            countByClass.put(value.name, countByClass.getOrDefault(value.name, 0) + 1);
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
