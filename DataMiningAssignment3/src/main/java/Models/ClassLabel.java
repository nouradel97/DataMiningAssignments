package Models;

public class ClassLabel {

    private String name;
    private int counter;
    private float probability;

    public ClassLabel(String name) {

        this.name = name;
        counter = 1;
        probability = 0;
    }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getCounter() { return counter; }

    public void setCounter(int counter) { this.counter = counter; }

    public float getProbability() { return probability; }

    public void setProbability(float probability) { this.probability = probability; }

    public void incrementCounter(){ counter++; }

    @Override
    public boolean equals(Object obj) {

        ClassLabel classLabel = (ClassLabel) obj;
        return name.equalsIgnoreCase(classLabel.name);
    }

    @Override
    public String toString() {

        return "ClassLabel{" + "name = " + name  + ", counter = "
                + counter + ", probability = " + probability + '}';
    }
}
