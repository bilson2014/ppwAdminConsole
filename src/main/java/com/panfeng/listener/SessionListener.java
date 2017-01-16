package com.panfeng.listener;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	@Override
	public void sessionCreated(HttpSessionEvent event) {
		final String sId = event.getSession().getId();
		System.err.println("session init , sessionID is " + sId);
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent event) {
		final String sId = event.getSession().getId();
		System.err.println("session destroy , sessionID is " + sId);
	}

}
