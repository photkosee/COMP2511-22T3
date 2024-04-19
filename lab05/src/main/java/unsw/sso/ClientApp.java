package unsw.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.sso.providers.Application;

public class ClientApp {
    private final String name;
    private Map<Token, PageState> tokens = new HashMap<>();
    private Application application;
    private List<Application> listApp = new ArrayList<>();

    public ClientApp(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void registerProvider(Application o) {
        this.application = o;
        this.listApp.add(o);
    }

    public void registerUser(String email) {
        application.registerUser(email);
    }

    public boolean hasUserForProvider(String email, Application provider) {
        return provider.hasUser(email);
    }

    public void addToken(Token token, PageState state) {
        if (tokens.get(token) != null) {
            tokens.remove(token);
        }
        this.tokens.put(token, state);
        registerUser(token.getUserEmail());
    }

    public void clearToken() {
        this.tokens.clear();
    }

    public PageState getOldState(Token token) {
        return tokens.get(token);
    }

    public Application getApplication() {
        return application;
    }

    public boolean hasApplication(Object o) {
        if (application == null) return false;
        return listApp.contains(o);
    }
}
