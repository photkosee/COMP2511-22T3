package factorymethod;

public class ButtonFactory2 {
	private Button btn;
	private String platform = "";

	public ButtonFactory2() {
		// Detect Operating system (platform), and save it
		this.platform = System.getProperty("os.name");
	}

	public ButtonFactory2(String platform) {
		this.platform = platform;
	}

	public Button generateButton() {

		if (platform.equalsIgnoreCase("Html")) {
			btn = new ButtonHtml();
		} else if (platform.equalsIgnoreCase("Windows 10")) {  // this may be different! 
			btn = new ButtonWin10();
		} else if (platform.equalsIgnoreCase("MacOs")) {  // this may be different! 
			btn = new ButtonMacOs();
		} else if (platform.equalsIgnoreCase("Linux")) {
			btn = new ButtonLinux();
		} else {
			new Exception("Unknwon platform type!");
		}

		return btn;
	}

}
