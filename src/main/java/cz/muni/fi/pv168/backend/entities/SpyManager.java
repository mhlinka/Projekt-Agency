package cz.muni.fi.pv168.backend.entities;

import java.util.List;

/**
 * Created by FH on 27.2.2015.
 */
public interface SpyManager
{
	void addSpy(Spy spy) throws  IllegalArgumentException;

	void removeSpy(Spy spy) throws  IllegalArgumentException;

	void removeSpy(Long id) throws  IllegalArgumentException;

	void updateSpy(Spy spy) throws  IllegalArgumentException;

	Spy findSpyById(Long id) throws  IllegalArgumentException;
	List<Spy> listSpies();
}
