package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;

import java.util.UUID;

public class UserService {
    private UserDAO userDAO;
    private AuthDAO authDAO;

    public UserService(UserDAO userDAO, AuthDAO authDAO){
        this.userDAO = userDAO;
        this.authDAO = authDAO;
    }

    public RegisterResult register(UserData userData) throws DataAccessException, AlreadyTakenException, BadRequestException{
        if (userData == null || userData.username() == null  || userData.password() == null || userData.email() == null || userData.username().isEmpty()  || userData.password().isEmpty() || userData.email().isEmpty() ) {
            throw new BadRequestException("bad request");
        }
        if (userDAO.getUser(userData.username()) != null){
            throw new AlreadyTakenException("already taken");
        }
        userDAO.createUser(userData);
        String token = UUID.randomUUID().toString();
        AuthData userAuthData = new AuthData(token, userData.username());
        authDAO.createAuth(userAuthData);
        return new RegisterResult(userData.username(), token);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException, BadRequestException, UnauthorizedException{
        if(loginRequest == null || loginRequest.username() == null || loginRequest.password() == null){
            throw new BadRequestException("bad request");
        }
        UserData user = userDAO.getUser(loginRequest.username());
        if(user == null){
            throw new UnauthorizedException("unauthorized");
        }
        if(!user.password().equals(loginRequest.password())){
            throw new UnauthorizedException("unauthorized");
        }
        String token = UUID.randomUUID().toString();
        AuthData userAuthData = new AuthData(token, loginRequest.username());
        authDAO.createAuth(userAuthData);
        return new LoginResult(loginRequest.username(), token);
    }

    public void logout(String authToken) throws DataAccessException, UnauthorizedException{
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("unauthorized");
        }
        authDAO.deleteAuth(authToken);

    }

}
