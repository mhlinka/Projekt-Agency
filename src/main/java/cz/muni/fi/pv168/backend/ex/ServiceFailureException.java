package cz.muni.fi.pv168.backend.ex;

/**
 * Created by FH on 10.3.2015.
 */
public class ServiceFailureException extends RuntimeException
{
	public ServiceFailureException(String msg) {
		super(msg);
	}

	public ServiceFailureException(Throwable cause) {
		super(cause);
	}

	public ServiceFailureException(String message, Throwable cause) {
		super(message, cause);
	}
}
