package Models;

public class AttributeValue {

    private String attribute_value_label;
    private int counter;
    private float probability;

    public AttributeValue() {

        attribute_value_label = "";
        counter = 0;
        probability = 0;
    }

    public String getAttribute_value_label() { return attribute_value_label; }

    public void setAttribute_value_label(String attribute_value_label) {

        this.attribute_value_label = attribute_value_label;
    }

    public void setProbability(float probability) { this.probability = probability; }

    public void setCounter(int counter) { this.counter = counter; }

    public int getCounter() { return counter; }

    public float getProbability() { return probability; }

    public void incrementCounter(){ counter++; }

    @Override
    public boolean equals(Object obj) {

        AttributeValue value = (AttributeValue) obj;
        return attribute_value_label.equalsIgnoreCase(((AttributeValue) obj).attribute_value_label);
    }

    @Override
    public String toString() {

        return "AttributeValue{" +
                "attribute_value_label='" + attribute_value_label + '\'' +
                ", counter=" + counter +
                ", probability=" + probability +
                '}';
    }
}
