package Controllers;

import Models.ClassLabel;
import Models.Data;
import Models.DataObject;
import Models.ObjectDistance;
import Views.GUI;

import java.util.*;

public class KnearestAlgorithm {

    private GUI gui;
    private Data data;
    private List<ClassLabel> classes;

    public KnearestAlgorithm(List<ClassLabel> classes, GUI gui) {

        this.gui = gui;
        data = new Data();
        this.classes = classes;
    }

    public Data getData() { return data; }

    public List<ClassLabel> getClasses() { return classes; }

    private float calculateDistance(DataObject<Integer> trainingObject, DataObject<Integer> testingObject){

        float distance = 0, sum = 0;

        for(int i=0; i<trainingObject.getValues().size() - 1; i++){
            sum += Math.pow((trainingObject.getValues().get(i) - testingObject.getValues().get(i)) ,2);
        }

        distance = (float) Math.sqrt(sum);
        return distance;
    }

    private ClassLabel getMajorityClassLabel(List<String> labels){

        ClassLabel temp;
        List<ClassLabel> classLabels = new ArrayList<>();

        for(String str: labels){

            temp = new ClassLabel(str);
            if(classLabels.contains(temp)){
                classLabels.get(classLabels.indexOf(temp)).incrementCounter();
            }else
                classLabels.add(temp);

        }
        classLabels.sort(Comparator.comparing(ClassLabel::getCounter).reversed());
        return classLabels.get(0);
    }

    private float calculateAccuracyOfAlgorithm(List<ClassLabel> predictedClasses){

        float accurecy;
        int counter = 0;
        List<String> objectValues;

        for(int i=0; i<predictedClasses.size(); i++){

            objectValues = data.getTestingData().get(i).getValues();
            if(objectValues.get(objectValues.size() - 1).equalsIgnoreCase(predictedClasses.get(i).getName()))
                counter++;
        }

        accurecy = ((float) counter / predictedClasses.size()) * 100;
        return accurecy;
    }

    public float run(int k){

        List<ObjectDistance> distances = new ArrayList<ObjectDistance>();
        List<String> labels;
        List<ClassLabel> predictedClasses = new ArrayList<>();
        float distance;

        for(DataObject object: data.getConvertedTestingData()){

            labels = new ArrayList<>();
            for (int j=0; j<data.getConvertedTrainingData().size(); j++){

                distance = calculateDistance(object, data.getConvertedTrainingData().get(j));
                distances.add(new ObjectDistance(j, distance));
            }
            distances.sort(Comparator.comparing(ObjectDistance::getDistance));

            DataObject<String> temp;
            for(int i=0; i<k; i++){

                temp = data.getTrainingData().get(distances.get(i).getIndexOfObject());
                labels.add(temp.getValues().get(temp.getValues().size() - 1));
            }
            predictedClasses.add(getMajorityClassLabel(labels));
        }

        List list;
        for(int i=0; i<data.getTestingData().size(); i++){
            list = data.getTestingData().get(i).getValues();
            gui.appendToSecondTextArea("actual class label: " + list.get(list.size() - 1) + '\t');
            gui.appendToSecondTextArea(",predicted class label: " + predictedClasses.get(i).getName());
            gui.appendToSecondTextArea("");
        }

        float accuracy = calculateAccuracyOfAlgorithm(predictedClasses);
        gui.appendToSecondTextArea("accuracy = " + accuracy);
        return accuracy;
    }
}
