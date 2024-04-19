package unsw.sso;

public class LockState extends PageState {

    public LockState(ClientApp app) {
        super(app);
        setPreState(new BaseState(app));
    }

    @Override
    public String getPageName() {
        return "Locked";
    }
}
