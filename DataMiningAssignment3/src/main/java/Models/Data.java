package Models;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Data {

    private int rows = 0;
    private int cols = 0;

    private List<DataObject<String>> trainingData = new ArrayList<DataObject<String>>();
    private List<DataObject<String>> testingData = new ArrayList<DataObject<String>>();
    private List<DataObject<Integer>> convertedTrainingData = new ArrayList<DataObject<Integer>>();
    private List<DataObject<Integer>> convertedTestingData = new ArrayList<DataObject<Integer>>();

    public Data() {

        readData();
        prepareDataForKnearestAlgorithm();
    }

    public int getRows() { return rows; }

    public int getCols() { return cols; }

    public List<DataObject<String>> getTrainingData() { return trainingData; }

    public List<DataObject<String>> getTestingData() { return testingData; }

    public List<DataObject<Integer>> getConvertedTrainingData() { return convertedTrainingData; }

    public List<DataObject<Integer>> getConvertedTestingData() { return convertedTestingData; }

    private void readData(){

        FileInputStream file = null;
        try {
            file = new FileInputStream("car.data.xlsx");

            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("car.data");

            rows = sheet.getLastRowNum();
            cols = sheet.getRow(0).getLastCellNum();

            int rowsOfTrainingData = (int) Math.ceil(rows * 0.75);
            DataObject<String> object;

            Random rand = new Random();
            List<Integer> randomRows = new ArrayList<Integer>();

            for(int i=0,rowNum; i<rowsOfTrainingData; i++){

                rowNum = rand.nextInt(rows - 1) + 1;
                if(randomRows.contains(rowNum)){
                    i--;
                    continue;
                }

                randomRows.add(rowNum);
                object = new DataObject<String>();

                for (int j=0; j<cols; j++){
                    object.addValue(sheet.getRow(rowNum).getCell(j).toString());
                }
                trainingData.add(object);
            }

            for(int i=0; i<=rows; i++){

                if(randomRows.contains(i))
                    continue;

                object = new DataObject<String>();
                for (int j=0; j<cols; j++){
                    object.addValue(sheet.getRow(i).getCell(j).toString());
                }
                testingData.add(object);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertFirstAndSecondAttribute(DataObject<String> stringObject, DataObject<Integer> convertedObject, int index){

        if (stringObject.getValues().get(index).equalsIgnoreCase("vhigh"))
            convertedObject.addValue(4);
        else if (stringObject.getValues().get(index).equalsIgnoreCase("high"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(index).equalsIgnoreCase("med"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);
    }

    private void convertThirdAttribute(DataObject<String> stringObject, DataObject<Integer> convertedObject){

        if (stringObject.getValues().get(2).equalsIgnoreCase("5more"))
            convertedObject.addValue(4);
        else if (stringObject.getValues().get(2).equalsIgnoreCase("4.0"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(2).equalsIgnoreCase("3.0"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);
    }

    private void convertForthAttribute(DataObject<String> stringObject, DataObject<Integer> convertedObject){

        if (stringObject.getValues().get(3).equalsIgnoreCase("more"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(3).equalsIgnoreCase("4.0"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);
    }

    private void convertFifthAttribute(DataObject<String> stringObject, DataObject<Integer> convertedObject){

        if (stringObject.getValues().get(4).equalsIgnoreCase("big"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(4).equalsIgnoreCase("med"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);
    }

    private void convertSixthAttribute(DataObject<String> stringObject, DataObject<Integer> convertedObject){

        if (stringObject.getValues().get(5).equalsIgnoreCase("high"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(5).equalsIgnoreCase("med"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);
    }

    private void convertClassLabel(DataObject<String> stringObject, DataObject<Integer> convertedObject){

        if (stringObject.getValues().get(6).equalsIgnoreCase("unacc"))
            convertedObject.addValue(4);
        else if (stringObject.getValues().get(6).equalsIgnoreCase("acc"))
            convertedObject.addValue(3);
        else if (stringObject.getValues().get(6).equalsIgnoreCase("vgood"))
            convertedObject.addValue(2);
        else
            convertedObject.addValue(1);

    }

    private DataObject<Integer> covertObjectToNumerical(DataObject<String> object){

        DataObject<Integer> convertedObject = new DataObject<Integer>();
        for(int i=0; i<object.getValues().size(); i++){

            if(i == 0 || i == 1) {
                convertFirstAndSecondAttribute(object,convertedObject,i);
            }else if(i == 2){
                convertThirdAttribute(object,convertedObject);
            }else if(i == 3){
                convertForthAttribute(object,convertedObject);
            }else if(i == 4){
                convertFifthAttribute(object,convertedObject);
            }else if(i == 5){
                convertSixthAttribute(object,convertedObject);
            }else {
                convertClassLabel(object,convertedObject);
            }
        }

        return convertedObject;
    }

    public void prepareDataForKnearestAlgorithm(){

        for(DataObject object: trainingData){
            convertedTrainingData.add(covertObjectToNumerical(object));
        }

        for(DataObject object: testingData){
            convertedTestingData.add(covertObjectToNumerical(object));
        }
    }
}