package beans;

import javax.xml.bind.annotation.XmlRootElement;
@XmlRootElement
public class ErrorMessage {

	private String errorText;
	private int errorCode;
	
	public ErrorMessage() {
		
	}
	public ErrorMessage(String errorText, int errorCode) {
		this.errorText = errorText;
		this.errorCode = errorCode;
	}
	public String getErrorText() {
		return errorText;
	}

	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	

}
