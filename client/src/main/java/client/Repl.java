package client;

import model.AuthData;

import java.util.Scanner;

public class Repl {
    private ServerFacade facade;
    private boolean running = true;
    private String authToken;

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

            case "help":
                return help();

            case "register":
               return register(words);

            default:
                return("Please type one of the set commands or 'help' to view command options");

        }
    }

    private String help(){
        return """
                register [USERNAME] [PASSWORD] [EMAIL] - to create an account
                login [USERNAME] [PASSWORD] - to login 
                quit - to end the program
                help - see list of commands
                """;
    }

    private String register(String[] words){
        if (words.length != 4){
            return("Please provide the correct number of arguments. Type help for more assistance");
        } try {
            AuthData auth = facade.register(words[1], words[2], words[3]);
            authToken = auth.authToken();
            return( auth.username() + " successfully registered!");
        } catch (ResponseException e){
            return e.getMessage();
        }
    }
    }


