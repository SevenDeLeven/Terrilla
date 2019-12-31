package com.sevendeleven.terrilla.util.event;

public interface RenderEvent {
	
	RenderEventType getType();
	void callback();
}
