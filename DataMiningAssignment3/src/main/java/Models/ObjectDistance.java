package Models;

public class ObjectDistance {

    private int indexOfObject;
    private float distance;

    public ObjectDistance() { }

    public ObjectDistance(int indexOfObject, float distance) {

        this.indexOfObject = indexOfObject;
        this.distance = distance;
    }

    public int getIndexOfObject() { return indexOfObject; }

    public void setIndexOfObject(int indexOfObject) { this.indexOfObject = indexOfObject; }

    public float getDistance() { return distance; }

    public void setDistance(float distance) { this.distance = distance; }
}
