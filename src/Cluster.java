import java.util.ArrayList;

public class Cluster {
    ArrayList<Value> values ;
    ArrayList<Double> centroid;

    public Cluster(Value general) {
        this.centroid = general.arr;
        values = new ArrayList<>();
    }
    public void newCentroid() {
        if (values.size() == 0) {
            return;
        }
        int dimensions = centroid.size();
        ArrayList<Double> newCentroidArr = new ArrayList<>();
        for (int i = 0; i < dimensions; i++) {
            double sum = 0;
            for (Value value : values) {
                sum += value.arr.get(i);
            }
            double avg = sum / values.size();
            newCentroidArr.add(avg);
        }
        centroid = newCentroidArr;
    }

    public void clearValues() {
        values = new ArrayList<>();
    }

    public void addValue(Value value) {
        values.add(value);
    }
    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("Centroid: " + centroid.toString() + "\n");
        s.append("Values:\n");
        for (Value value : values) {
            s.append(value.toString()).append("\n");
        }
        return s.toString();
    }

}
