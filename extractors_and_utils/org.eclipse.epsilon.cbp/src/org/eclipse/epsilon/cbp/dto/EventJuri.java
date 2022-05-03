package org.eclipse.epsilon.cbp.dto;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.epsilon.cbp.event.ChangeEvent;

public class EventJuri {

	private List<ChangeEvent> changeEvents;
	private Map<String, EClass> eObjectMap;

	public List<ChangeEvent> getChangeEvents() {
		return changeEvents;
	}
	public EventJuri(List<ChangeEvent> changeEvents, Map<String, EClass> classMap) {
		super();
		this.changeEvents = changeEvents;
		this.eObjectMap = classMap;
	}
	public void setChangeEvents(List<ChangeEvent> changeEvents) {
		this.changeEvents = changeEvents;
	}
	public Map<String, EClass> geteObjectMap() {
		return eObjectMap;
	}
	public void seteObjectMap(Map<String, EClass> eObjectMap) {
		this.eObjectMap = eObjectMap;
	}
}
