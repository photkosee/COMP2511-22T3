package unsw.sso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import unsw.sso.providers.Application;
import unsw.sso.providers.InstaHam;

public class Browser {
    private List<Token> existToken = new ArrayList<>();
    private Map<String, Integer> lockedList = new HashMap<>();
    private PageState state = null;
    private ClientApp currClientApp = null;

    public void visit(ClientApp app) {  
        this.state = new BaseState(app);
        this.currClientApp = app;
        for (Token token : existToken) {
            if (app.getOldState(token) != null) {
                this.state = app.getOldState(token);
                state.resetPreState();
            }
        }
    }

    public String getCurrentPageName() {
        if (state == null) {
            return null;
        }
        return state.getPageName();
    }

    public void clearCache() {
        this.existToken = new ArrayList<Token>();
        this.lockedList = new HashMap<String, Integer>();
        currClientApp.clearToken();
    }

    public void interact(Object using) {
        if (using == null) {
            this.state = state.transitionPre();
            return;
        } else if (currClientApp.hasApplication(using)) {
            currClientApp.registerProvider((Application) using);
        }

        if (state instanceof BaseState) {
            if (currClientApp.hasApplication(using)) {
                this.state = state.transitionLogin();
            }
        } else if (state instanceof LoginState) {
            if (using instanceof Token) {
                Token token = (Token) using;
                if (lockedList.containsKey(token.getUserEmail()) && lockedList.get(token.getUserEmail()) >= 2 && currClientApp.getApplication().getClass().equals(state.getCurrApp().getClass())) {
                    this.state = state.transitionLock();
                    currClientApp.registerUser(token.getUserEmail());
                    return;
                }
                if (token.getAccessToken() == null && lockedList.containsKey(token.getUserEmail())) {
                    this.state = state.transitionBase();
                    if (lockedList.get(token.getUserEmail()) >= 2) {
                        this.state = state.transitionLock();
                        currClientApp.registerUser(token.getUserEmail());
                    } else {
                        Integer count = lockedList.get(token.getUserEmail()) + 1;
                        lockedList.put(token.getUserEmail(), count);
                    }
                } else if (token.getAccessToken() != null && (!lockedList.containsKey(token.getUserEmail()) || lockedList.get(token.getUserEmail()) < 2)) {
                    this.state = state.transitionHome();
                    currClientApp.addToken(token, state);
                    this.existToken.add(token);
                } else {
                    this.state = state.transitionBase();
                    if (lockedList.get(token.getUserEmail()) == null) {
                        lockedList.put(token.getUserEmail(), 1);
                    }
                }
            } else if (currClientApp.getApplication() instanceof InstaHam && using instanceof String) {
                ((InstaHam) currClientApp.getApplication()).broadcastCode((String) using);
            } else {
                this.state = state.transitionBase();
            }
        }
    }
}
