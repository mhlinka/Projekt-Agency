package cz.muni.fi.pv168.backend;

import cz.muni.fi.pv168.backend.ex.ServiceFailureException;

import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface SpyManager
{
	void addSpy(Spy spy) throws ServiceFailureException, IllegalArgumentException;

	void removeSpy(Spy spy) throws ServiceFailureException, IllegalArgumentException;

	void removeSpy(Long id) throws ServiceFailureException, IllegalArgumentException;

	void updateSpy(Spy spy) throws ServiceFailureException, IllegalArgumentException;

	Spy findSpyById(Long id) throws ServiceFailureException, IllegalArgumentException;
	List<Spy> listSpies() throws ServiceFailureException;
}
