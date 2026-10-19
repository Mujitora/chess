package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;


public class MemoryAuthDAO implements AuthDAO{
    private Map<String, AuthData> authmap = new HashMap<>();

    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        authmap.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return authmap.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        authmap.remove(authToken);
    }

    @Override
    public void clear() throws DataAccessException {
        authmap.clear();
    }
}
