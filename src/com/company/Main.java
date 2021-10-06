package com.company;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
	final String FILENAME = "src/Elevations.TXT";

	int[][] elevation = null;

	int exclusion = 0;          //exclusion number
	int numberOfRows = 0;       //number of raw
	int numberOfCols = 0;       //number of column

	try{
	    File file = new File(FILENAME);
        Scanner inputFile = new Scanner(file);

        //Read first line of data first
        numberOfRows = inputFile.nextInt();
        numberOfCols = inputFile.nextInt();
        exclusion = inputFile.nextInt();

        elevation = new int[numberOfRows][numberOfCols];

        //Getting data from file and storing in a 2D array
        for(int i = 0;i<numberOfRows;i++){
            for (int j = 0;j<numberOfCols;j++){
                elevation[i][j] = inputFile.nextInt();  
            }
        }
        /*
        for(int i = 0;i<numberOfRows;i++){
            System.out.println();
            for (int j = 0;j<numberOfCols;j++){
                System.out.print(elevation[i][j]+" ");
            }
        }
        */
        inputFile.close();

    }
	catch (FileNotFoundException ex){
        System.out.println("Error reading data from file" +FILENAME+"Exception"+ex.getMessage());
    }

	System.out.println();

	int[] lowestElevation = findLowestElevation(elevation);
	int[] localPeaks = findLocalPeaks(elevation,exclusion);
	//int[] commanElevation = findCommanElevation(elevation);
	double[] dist = findDifference(localPeaks);

	System.out.println("The lowest elevation is "+lowestElevation[0]+". It occurs "+lowestElevation[1]+" times in the map");

	System.out.println("The total number of peak is "+localPeaks[0]+".");

	System.out.println("The lowest difference between two peaks is "+String.format("%.2f",dist[0])+"m peaks are ["+(int)dist[1]+","+(int)dist[2]+" elevation ="+elevation[(int) dist[2]][(int)dist[1]]+"] and ["+(int)dist[3]+","+(int)dist[4]+" elevation ="+elevation[(int) dist[4]][(int)dist[3]]+"]");

	/*for  (int q = 1;q<(localPeaks[0]*3+1);q=q+3){
        System.out.println("["+localPeaks[q]+","+localPeaks[q+1]+" elevation = "+localPeaks[q+2]+"]");
    }*/
    //System.out.println("The most common height in the terrain is "+commanElevation[0]+". Which occurs "+commanElevation[1]+" times.");


    }

    public static int[] findLowestElevation(int elevation[][]){
        int[] result = new int[2];
        int number = elevation[0][0];
        int count = 0;
        for (int i = 0;i<elevation.length;i++){
            for (int j= 0;j<elevation[i].length;j++){
                if (number>elevation[i][j]){
                    number = elevation[i][j];
                    count =1;
                }
                else if (number == elevation[i][j]){
                    count++;
                }
            }
        }
        result[0] = number;
        result[1] = count;

        return result;
    }

    public static int[] findLocalPeaks(int[][] elevation,int exclusion){
        int[] result = new int[3300];
        int count = 0;
        int x=1;
        for (int i = exclusion;i<(elevation.length-exclusion);i++){
            for (int j = exclusion;j<(elevation[i].length-exclusion);j++){
                if (elevation[i][j]>=98480){
                    boolean isPeak = true;
                    overlook:
                    for (int m = (i-exclusion);m<=(i+exclusion);m++){
                        for (int n = (j-exclusion);n<=(j+exclusion);n++){
                            if (i!=m||j!=n) {
                                if (elevation[m][n] >= elevation[i][j]) {
                                    isPeak = false;
                                    break overlook;
                                }
                            }
                        }
                    }
                    if (isPeak == true){
                        count++;
                        result[x] = j;
                        x++;
                        result[x] = i;
                        x++;
                        result[x] = elevation[i][j];
                        x++;
                    }
                }
            }
        }

        result[0] = count;
        return result;

    }

    public static double[] findDifference(int[] localPeaks){
        double[] result = new double[20];
        int rawPeak1=0;
        int rawPeak2=0;
        int colPeak1=0;
        int colPeak2=0;
        double dist=5000000;

        outer:
        for(int i = 1;i< localPeaks.length-2;i=i+3){
            double tempDist;
            for (int j = i+3;j< localPeaks.length-2;j=j+3){
                if (localPeaks[i]==0){
                    break outer;
                }
                if (localPeaks[j]==0){
                    break;
                }
                tempDist = Math.sqrt((localPeaks[i]-localPeaks[j])*(localPeaks[i]-localPeaks[j])+(localPeaks[i+1]-localPeaks[j+1])*(localPeaks[i+1]-localPeaks[j+1]));
                if(tempDist<dist){
                    dist = tempDist;
                    rawPeak1 = localPeaks[i];
                    colPeak1 = localPeaks[i+1];
                    rawPeak2 = localPeaks[j];
                    colPeak2 = localPeaks[j+1];
                }
            }
        }
        result[0] = dist;
        result[1] = rawPeak1;
        result[2] = colPeak1;
        result[3] = rawPeak2;
        result[4] = colPeak2;
        return result;
    }

    public static int[] findCommanElevation(int[][] elevation){
        int[] res = new int[2];
        int finalValue = 0;
        int count = 0;

        for (int i = 0;i< elevation.length;i++){
            for (int j=0;j<elevation[i].length;j++){
                int tempValue = elevation[i][j];
                int tempCount = 0;
                for (int m = i;m< elevation.length;m++) {
                    int n = j;
                    if (i!=m)
                        n=0;
                    for (; n < elevation[m].length; n++) {

                        if (tempValue==elevation[m][n]){
                            tempCount++;
                        }
                    }
                }
                if (tempCount>count){
                    count = tempCount;
                    finalValue = tempValue;
                }

            }
        }
        res[0] = finalValue;
        res[1] = count;

        System.out.println(finalValue+"  "+count);
        return res;
    }
}
