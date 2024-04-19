package unsw.sso;

public class LoginState extends PageState {

    public LoginState(ClientApp app) {
        super(app);
    }
    
    public LoginState(ClientApp app, PageState preState) {
        super(app);
        setPreState(preState);
    }

    @Override
    public String getPageName() {
        return getCurrApp().getApplication().getLoginName();
    }
}
