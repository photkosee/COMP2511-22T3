package unsw.sso;

public class BaseState extends PageState {

    public BaseState(ClientApp app) {
        super(app);
    }

    public BaseState(ClientApp app, PageState preState) {
        super(app);
        setPreState(preState);
    }

    @Override
    public String getPageName() {
        return "Select a Provider";
    }
}
