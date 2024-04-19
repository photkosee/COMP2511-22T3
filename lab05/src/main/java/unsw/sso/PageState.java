package unsw.sso;

public abstract class PageState {

    private ClientApp currentApp;
    private PageState preState = null;

    public PageState(ClientApp app) {
        this.currentApp = app;
    }

    public void setPreState(PageState pre) {
        this.preState = pre;
    }

    public PageState getPreState() {
        return preState;
    }
    
    public PageState transitionBase() {
        return new BaseState(currentApp, this);
    }

    public PageState transitionLogin() {
        return new LoginState(currentApp, this);
    }

    public PageState transitionHome() {
        return new HomeState(currentApp, this);
    }

    public PageState transitionPre() {
        return preState;
    }

    public ClientApp getCurrApp() {
        return currentApp;
    }

    public void resetPreState() {
        this.preState = null;
    }

    public PageState transitionLock() {
        return new LockState(currentApp);
    }

    public abstract String getPageName();
}
