package mandatory3;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args) {
        //testing threads
	    System.out.println("elloello");

	    Thread thread = new Thread(){
	        @Override
            public void run(){
                System.out.println("test");
            }
        };
	    thread.run();

	    //testing making a window
        JFrame frame = new JFrame("frame test");
        Label emptyLabel = new Label("sup");
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        frame.pack();
        frame.setVisible(true);
    }
}
