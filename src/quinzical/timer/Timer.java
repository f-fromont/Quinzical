package quinzical.timer;

import java.io.BufferedReader;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Timer extends javafx.concurrent.Task {


    /**
     * Starts a 20 second countdown for the user to answer the question.
     */
    @Override
    protected Object call() {
        try {
            //We use a process builder and a process with a bash command to countdown every second from 20.
            String cmd = "for (( i = 20 ; $i >= 0; i=i-1)) ; do echo $i ; sleep 1; done";
            ProcessBuilder builder = new ProcessBuilder("/bin/bash", "-c", cmd);

            Process process = builder.start();
            InputStream out = process.getInputStream();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(out));

            String number;
            while ((number = stdout.readLine()) != null ) {
                //If the process is cancelled we break the loop.
                if(isCancelled()) {
                    break;
                }
                //Update the message on the text for every iteration of the while loop(everytime the number changes.)
                updateMessage(number);
            }

        } catch(Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
