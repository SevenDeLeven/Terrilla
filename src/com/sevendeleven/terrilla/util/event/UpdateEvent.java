package com.sevendeleven.terrilla.util.event;

public interface UpdateEvent {
	
	UpdateEventType getType();
	void callback();
}
