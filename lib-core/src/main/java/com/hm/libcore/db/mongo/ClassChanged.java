package com.hm.libcore.db.mongo;

import org.springframework.data.annotation.Transient;

public abstract class ClassChanged {
	@Transient
	transient private boolean Changed;
	@Transient
	transient private boolean ClientChanged;

	public boolean Changed() {
		return Changed;
	}
	public boolean ClientChanged() {
		return ClientChanged;
	}

	public void SetChanged() {
		Changed = true;
		ClientChanged = true;
	}
	public void ClearChangedFlag() {
		Changed = false;
	}
	public void ClearClientChanged() {
		ClientChanged = false;
	}
}
