package unsw.sso.providers;

import java.util.ArrayList;
import java.util.List;

public abstract class Application {

    private List<String> usersExist = new ArrayList<>();
    
    public boolean hasUser(String email) {
        return usersExist.stream().anyMatch(element -> element.equals(email));
    }

    public void registerUser(String email) {
        usersExist.add(email);
    }

    public abstract String getLoginName();
}
