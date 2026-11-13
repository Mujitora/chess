package client;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private boolean running = true;

    public Repl(int port){
        facade = new ServerFacade(port);
    }


    public void run(){
        Scanner scanner = new Scanner(System.in);

        while (running){
            System.out.print(">>> ");
            String line = scanner.nextLine();
            System.out.println(answer(line));


        }
    }
    private String answer(String line) {
        String[] words = line.trim().split("\\s+");
        String splitLine = words[0].toLowerCase();
        switch (splitLine) {
            case "quit":
                running = false;
                return("Thanks For Playing!");
            default:
                return("Please type one of the set commands or 'help' to view command options");

        }
    }

    }


