package exceptions;

public class ApplicationException extends Exception {

	private static final long serialVersionUID = 5393653636493250337L;
	public ErrorType errortype;
	public Exception exception; 

	public ApplicationException(ErrorType errortype, Exception e) {
		super();
		this.errortype = errortype;
		this.exception = e;
		
	}

	public ApplicationException(ErrorType errortype) {
		super();
		this.errortype = errortype;
		
	}
	
	
}
