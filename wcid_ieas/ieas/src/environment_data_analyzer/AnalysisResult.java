package environment_data_analyzer;

public class AnalysisResult {
	private boolean hasAction;
	private int act;
	private String pushMessage;
	
	public AnalysisResult(boolean hasAction, int act, String pushMessage) {
		this.hasAction = hasAction;
		this.act = act;
		this.pushMessage = pushMessage;
	}
	
	public boolean HasAction() {
		return hasAction;
	}
	
	public int getAct() {
		return act;
	}
	
	public String getPushMessage() {
		return pushMessage;
	}
}
