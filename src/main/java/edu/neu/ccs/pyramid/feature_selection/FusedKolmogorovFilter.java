package edu.neu.ccs.pyramid.feature_selection;

import edu.neu.ccs.pyramid.util.EmpiricalCDF;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by chengli on 3/26/15.
 */
public class FusedKolmogorovFilter {
    private int numBins=100;

    public void setNumBins(int numBins) {
        this.numBins = numBins;
    }

    List<List<Double>> generateInputsEachClass(Vector vector, int[] labels, int numClasses){
        Vector input;
        if (vector.isDense()){
            input = vector;
        } else {
            input = new DenseVector(vector);
        }
        List<List<Double>> inputsEachClass = new ArrayList<>();
        for (int k=0;k<numClasses;k++){
            inputsEachClass.add(new ArrayList<>());
        }
        for (int i=0;i<labels.length;i++){
            int label = labels[i];
            inputsEachClass.get(label).add(input.get(i));
        }
        return inputsEachClass;

    }

    List<EmpiricalCDF> generateCDFs(List<List<Double>> inputsEachClass){
        return inputsEachClass.stream().map(list -> new EmpiricalCDF(list,numBins)).collect(Collectors.toList());

    }

    double maxDistance(List<EmpiricalCDF> empiricalCDFs){
        int numClasses = empiricalCDFs.size();
        double max = Double.NEGATIVE_INFINITY;
        for (int k=0;k<numClasses-1;k++){
            for (int j=k+1;j<numClasses;j++){
                double distance = EmpiricalCDF.distance(empiricalCDFs.get(k),empiricalCDFs.get(j));
                if (distance> max){
                    max = distance;
                }
            }
        }
        return max;
    }
}
