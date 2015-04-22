package cz.muni.fi.pv168.backend.ex;

/**
 * Created by FH on 21.4.2015.
 */
public class MissionException extends RuntimeException
{
	public MissionException(String message)
	{
		super(message);
	}

	public MissionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public MissionException(Throwable cause)
	{
		super(cause);
	}
}
