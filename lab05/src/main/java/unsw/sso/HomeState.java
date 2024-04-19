package unsw.sso;

public class HomeState extends PageState {

    public HomeState(ClientApp app) {
        super(app);
    }
    
    public HomeState(ClientApp app, PageState preState) {
        super(app);
        setPreState(preState);
    }

    @Override
    public String getPageName() {
        return "Home";
    }
}
