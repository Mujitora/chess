package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    boolean running = true;

    public Repl(int port){
        facade = new ServerFacade(port);
    }


    public void run(){
        Scanner scanner = new Scanner(System.in);

        while (running){
            System.out.print(">>> ");
            String line = scanner.nextLine();
            System.out.println(line);
            if (line.toLowerCase().equals("quit")){
                running = false;
            }

        }
    }

}
