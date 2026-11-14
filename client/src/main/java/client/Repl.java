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
        if (authToken == null){
            return preloginCommand(words);
        }
        return loggedInCommand(words);


    }

    private String preloginCommand(String[] words){
        String splitLine = words[0].toLowerCase();
        switch (splitLine) {
            case "quit":
                return quit();
            case "help":
                return helpLoggedOut();

            case "register":
                return register(words);

            case "login":
                return login(words);

            default:
                return("Please type one of the set commands or 'help' to view command options");

        }
    }

    private String loggedInCommand(String[] words){
        String splitLine = words[0].toLowerCase();

        switch (splitLine){
            case "quit":
                return quit();
            case "help":
                return helpLoggedIn();
            case "logout":
                return logout(words);
            case "create":
                return create(words);
            default:
                return("Please type one of the set commands or 'help' to view command options");
        }
    }

    private String quit(){
        running = false;
        return("Thanks For Playing!");
    }

    private String helpLoggedOut(){
        return """
                register [USERNAME] [PASSWORD] [EMAIL] - to create an account
                login [USERNAME] [PASSWORD] - to login 
                quit - to end the program
                help - see list of commands
                """;
    }
    private String helpLoggedIn(){
        return """
                logout - to logout
                quit - to end the program
                help - see list of commands
                create [GAMENAME] - to create a game
                """;
    }

    private String register(String[] words){
        if (words.length != 4){
            return("Please provide the correct number of words. Type help for more assistance");
        } try {
            AuthData auth = facade.register(words[1], words[2], words[3]);
            authToken = auth.authToken();
            return( auth.username() + " successfully registered!");
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    private String login(String[] words){
        if (words.length != 3){
            return("Please provide the correct number of words. Type help for more assistance");
    } try{
        AuthData auth = facade.login(words[1], words[2]);
        authToken = auth.authToken();
            return( auth.username() + " successfully logged in!");
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    private String logout(String[] words){
        if (words.length != 1){
            return("Logout doesn't need any words after it");
        } try {
            facade.logout(authToken);
            authToken = null;
            return("successfully logged out!");
        } catch (ResponseException e){
            return e.getMessage();
        }
    }

    private String create(String[] words){
        if (words.length != 2){
            return("Please provide the correct number of words. game name cannot contain any spaces");
        } try {
            int gameID = facade.createGame(authToken, words[1]);
            return("Game created!");
        } catch (ResponseException e){
            return e.getMessage();
        }
    }



    }


