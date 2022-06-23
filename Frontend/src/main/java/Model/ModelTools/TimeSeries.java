package Model.ModelTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeSeries {

    private Vector<Vector<Double>> _array;
    private List<List<String>> _records;
    private List<String> colOfTime;

    public TimeSeries(String csvFileName) {
        File file = new File(csvFileName);
        _records = new ArrayList<>();
        _array = new Vector<>();
        this.colOfTime = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(csvFileName))) {
            String line = "";
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                this._records.add(Arrays.asList(values));
            }
            for (int i = 1; i < this._records.size(); i++) {
                Vector<Double> temp_vector = new Vector<>();
                int len = this._records.get(i).size() - 1; // without the last column
                for (int j = 0; j < len; j++) {
                    String s = this._records.get(i).get(j);
                    temp_vector.add(Double.parseDouble(this._records.get(i).get(j)));
                }
                this._array.add(temp_vector);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < _records.size() - 1; i++) {
            String s = _records.get(i).get(_records.get(0).size() - 1);
            colOfTime.add(s);
        }
    }

    public TimeSeries(List<List<String>> listOfLists) {
        this._records = listOfLists;
        this._array = new Vector<>();
        this.colOfTime = new ArrayList<>();
        stringToDouble(listOfLists);
        for (int i = 0; i < _records.size() - 1; i++) {
            String s = _records.get(i).get(_records.get(0).size() - 1);
            colOfTime.add(s);
        }
    }

    public void stringToDouble(List<List<String>> records) {
        for (int i = 1; i < records.size(); i++) {
            Vector<Double> temp_vector = new Vector<>();
            for (int j = 0; j < records.get(i).size() - 1; j++) {
                String s = records.get(i).get(j);
                temp_vector.add(Double.parseDouble(records.get(i).get(j)));
            }
            this._array.add(temp_vector);
        }
    }

    public void DateToDouble(List<String> dateCol) {
        try {
            Vector<Double> temp = new Vector<>();
            for (int i = 1; i < dateCol.size(); i++) {
                SimpleDateFormat s1 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
                Date date1 = s1.parse(String.valueOf(_records.get(i)));
                double dat1 = date1.getTime();
                temp.add(dat1);
            }
            this._array.add(temp);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public Vector<Double> getColByName(String str) {
        Vector<Double> vector = new Vector<>();
        for (int i = 0; i < this._records.get(0).size(); i++) {
            if (this._records.get(0).get(i).equals(str)) {
                for (int j = 0; j < this._array.size(); j++) {
                    vector.add(this._array.get(j).get(i));
                }
            }
        }
        return vector;
    }

    public float[] getCol(String str) {
        int i = 0;
        int size = this.getArray().size();
        float[] temp_vector1 = new float[size];
        for (i = 0; i < this._records.get(1).size(); i++) {
            if(i == 14)
                break;
            if (this._records.get(0).get(i).equals(str) ) {
                for (int j = 0; j < this._array.size(); j++) {
                    temp_vector1[j] = (float) this._array.get(j).get(i).doubleValue();
                }
            }
        }
        return temp_vector1;
    }

    public float[] getColByNum(int num) {
        int i = 0;
        float[] temp_vector2 = new float[this._array.size()];
        int len = temp_vector2.length;
        for (i = 0; i < len; i++) {
            for (int j = 0; j < this._array.size(); j++) {
                temp_vector2[j] = (float) (this._array.get(j).get(num).doubleValue());
            }
        }
        return temp_vector2;
    }

    public void set_records(List<List<String>> _records) {
        this._records = _records;
    }

    public Vector<Vector<Double>> getArray() {
        return this._array;
    }

    public List<List<String>> get_records() {
        return _records;
    }
}