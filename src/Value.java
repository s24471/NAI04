import java.util.ArrayList;

public class Value {
    ArrayList<Double> arr;
    String name;

    public Value(String[] s) {
        arr = new ArrayList<>();
        for (int i = 0; i < s.length - 1; i++) {
            arr.add(Double.parseDouble(s[i].replace(',', '.').strip()));
        }
        name = s[s.length - 1].strip();
    }

    @Override
    public String toString() {
        String ans = "";
        for (Double d : arr) {
            ans += d.toString() + "\t";
        }
        ans += name;
        return ans;
    }
}