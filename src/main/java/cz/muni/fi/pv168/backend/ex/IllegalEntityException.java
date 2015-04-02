package cz.muni.fi.pv168.backend.ex;

/**
 * Created by FH on 27.3.2015.
 */
public class IllegalEntityException extends RuntimeException
{
	public IllegalEntityException(String message)
	{
		super(message);
	}

	public IllegalEntityException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public IllegalEntityException(Throwable cause)
	{
		super(cause);
	}
}
