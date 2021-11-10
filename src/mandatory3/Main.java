package mandatory3;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.Semaphore;

class SharedRes {
    static int count = 0;
    static double[] values = new double[2];
}

//thread that uses the feigenbaum
//tries to access resource at the same time
class FeigenBaumThread extends Thread{
    Semaphore semaphore;
    int limit;
    feigenbaumClass obj = new feigenbaumClass();
    double myArray[] = new double[2];


    public FeigenBaumThread(Semaphore semaphore){
        this.semaphore = semaphore;
        this.limit = limit;
    }
    @Override
    public void run(){
        while(true) {//keep regenerating the tree
            runFeigenWithSem();
            obj.setLambda(1);
        }
    }

    void runFeigenWithSem(){//calculates 500 feigenbaum values
        try {
            //increment shared resourc
            while(obj.increaseLambda()) {
                for (int i = 0; i < 500; i++) {
                    myArray = obj.feigenbaum();//calc values

                    semaphore.acquire(); //lock
                    SharedRes.values = myArray;
                    System.out.println(myArray[0] + " " + myArray[1]);
                    semaphore.release(); //open
                }
            }
        }catch ( InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }
    }
}

public class Main {

    public static void main(String[] args) {


        Semaphore semaphore = new Semaphore(1);
        //creating and running the feigenbaum thread
	    Thread feigen = new FeigenBaumThread(semaphore);
        feigen.run();


        try {
            feigen.join();
        } catch (InterruptedException interruptedException){
            interruptedException.printStackTrace();
        }

	    //testing making a window
        JFrame frame = new JFrame("frame test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Label emptyLabel = new Label("sup");
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}

