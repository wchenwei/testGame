package com.hm.model.serverpublic;

import com.hm.libcore.db.mongo.ClassChanged;
import org.springframework.data.annotation.Transient;

public class ServerPublicContext extends ClassChanged{
	@Transient
	private transient ServerData context;

	public ServerData getContext() {
		return context;
	}

	public void setContext(ServerData context) {
		this.context = context;
	}
	
	public void save() {
		SetChanged();
		this.context.save();
	}
}
