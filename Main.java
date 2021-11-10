package com.company;

public class Main {

    public static void main(String[] args) {
        feigenbaumClass obj = new feigenbaumClass();
        double myArray[] = new double[2];

        obj.increaseLambda();
        for (int i = 0; i < 500; i++) {
            myArray = obj.feigenbaum();
            System.out.println(myArray[0] + " " + myArray[1]);
        }

    }
}
