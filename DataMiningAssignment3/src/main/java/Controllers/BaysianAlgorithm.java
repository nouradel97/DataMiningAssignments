package Controllers;

import Models.AttributeValue;
import Models.ClassLabel;
import Models.Data;
import Models.DataObject;
import Views.GUI;

import java.util.ArrayList;
import java.util.List;

public class BaysianAlgorithm {

    private GUI gui;
    private Data data;
    private List<ClassLabel> classes;
    private List<AttributeValue> values;

    public BaysianAlgorithm(GUI gui) {

        this.gui = gui;
        data = new Data();
        classes = new ArrayList<ClassLabel>();
        values = new ArrayList<AttributeValue>();
    }

    public Data getData() { return data; }

    public List<ClassLabel> getClasses() { return classes; }

    public List<AttributeValue> getValues() { return values; }

    private void prepareClassesLabels(){

        ClassLabel label;
        for (DataObject object : data.getTrainingData()) {

            label = new ClassLabel(object.getValues().get(object.getValues().size() - 1).toString());
            if(classes.contains(label)){
                classes.get(classes.indexOf(label)).incrementCounter();
            }else {
                classes.add(label);
            }
        }

        for(ClassLabel classLabel: classes) {
            classLabel.setProbability((float) classLabel.getCounter() / data.getTrainingData().size());
        }
    }

    private int countValues(AttributeValue attribute){

        int counter = 0;
        boolean exists;
        String [] strings = attribute.getAttribute_value_label().split("_");

        for(DataObject object: data.getTrainingData()){

            exists = true;
            for (int i=1; i<strings.length; i++){

                if(!object.getValues().contains(strings[i])) {
                    exists = false;
                    break;
                }
            }
            if(exists)
                counter++;
        }
        return counter;
    }

    private void useLaplacianCorrection(){

        ClassLabel temp = new ClassLabel("");
        ClassLabel classLabel;
        List<AttributeValue> attributeValues = new ArrayList<AttributeValue>();
        int counter;

        for (AttributeValue attributeValue: values){

            counter = 0;
            if(attributeValue.getCounter() == 0){

                String [] strings = attributeValue.getAttribute_value_label().split("_");
                for(AttributeValue value: values){

                    String [] str = value.getAttribute_value_label().split("_");
                    if(strings[0].equalsIgnoreCase(str[0]) && strings[2].equalsIgnoreCase(str[2])){

                        value.incrementCounter();
                        attributeValues.add(value);
                        counter++;
                    }
                }

                temp.setName(strings[2]);
                classLabel = classes.get(classes.indexOf(temp));

                for(AttributeValue value: attributeValues){
                    value.setProbability((float)value.getCounter()/(classLabel.getCounter() + counter));
                }
            }
        }
    }

    private void prepareAttributesProbabilities(){

        String temp;
        List valuesOfAttribute;
        AttributeValue attribute;

        for(ClassLabel label: classes) {
            for (int j = 0; j <data.getCols() - 1; j++) {
                for (int i = 0; i < data.getTrainingData().size(); i++) {

                    attribute = new AttributeValue();
                    valuesOfAttribute = data.getTrainingData().get(i).getValues();

                    temp = j + "_" + valuesOfAttribute.get(j) + "_" + label.getName();
                    attribute.setAttribute_value_label(temp);

                    if (!values.contains(attribute)) {
                        attribute.setCounter(countValues(attribute));
                        values.add(attribute);
                    }
                }
            }
        }

        useLaplacianCorrection();

        ClassLabel classLabel;
        for(AttributeValue value: values){

            String [] strings = value.getAttribute_value_label().split("_");
            classLabel = new ClassLabel(strings[2]);

            if(value.getProbability() == 0) {

                int counter = classes.get(classes.indexOf(classLabel)).getCounter();
                value.setProbability((float) value.getCounter() / counter);
            }
        }
    }

    private ClassLabel applyAlgorithm(DataObject object){

        ClassLabel classlabel = new ClassLabel("");
        String temp;
        AttributeValue value;
        float totalProbability, prob = 0;

        for(ClassLabel label: classes) {

            totalProbability = 1;
            for (int i = 0; i < object.getValues().size() - 1; i++) {

                value = new AttributeValue();
                temp = i + "_" + object.getValues().get(i) + "_" + label.getName();
                value.setAttribute_value_label(temp);

                if(values.indexOf(value) == -1){

                    AttributeValue newValue = new AttributeValue();
                    newValue.setAttribute_value_label(temp);
                    useLaplacianCorrection();
                    totalProbability *= newValue.getProbability();
                }
                else {

                    value = values.get(values.indexOf(value));
                    totalProbability *= value.getProbability();
                }
            }
            totalProbability *= label.getProbability();

            if(totalProbability > prob){
                prob = totalProbability;
                classlabel = label;
            }
        }
        return classlabel;
    }

    private float calculateAlgorithmAccurecy(List<ClassLabel> labels){

        int counter = 0;
        float accurecy = 0;
        List objectValues;

        for (int i=0; i<labels.size(); i++){

            objectValues = data.getTestingData().get(i).getValues();
            if(labels.get(i).getName().equalsIgnoreCase(objectValues.get(objectValues.size() - 1).toString()))
                counter++;
        }

        accurecy = ((float) counter / data.getTestingData().size()) * 100;
        return accurecy;
    }

    public float run(){

        prepareClassesLabels();
        prepareAttributesProbabilities();

        List<ClassLabel> labels = new ArrayList<ClassLabel>();

        for(int i=0; i<data.getTestingData().size(); i++){
            labels.add(applyAlgorithm(data.getTrainingData().get(i)));
        }

        List temp;
        for(int i=0; i<data.getTestingData().size(); i++){

            temp = data.getTestingData().get(i).getValues();
            gui.appendToFirstTextArea("actual class label: " + temp.get(temp.size() - 1) + '\t');
            gui.appendToFirstTextArea(",predicted class label: " + labels.get(i).getName());
            gui.appendToFirstTextArea("");
        }

        float accuracy = calculateAlgorithmAccurecy(labels);
        gui.appendToFirstTextArea("accurecy = " + accuracy);
        return accuracy;
    }
}
