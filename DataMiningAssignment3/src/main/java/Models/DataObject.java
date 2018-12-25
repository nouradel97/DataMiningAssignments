package Models;

import java.util.ArrayList;
import java.util.List;

public class DataObject<T extends Comparable<T>> {

    private List<T> values;

    public DataObject() { values = new ArrayList<T>(); }

    public List<T> getValues() { return values; }

    public void addValue(T value){ values.add(value); }

    @Override
    public String toString() {
        return "values = " + values + '\n';
    }
}
