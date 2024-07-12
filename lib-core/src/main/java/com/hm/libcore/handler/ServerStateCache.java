package com.hm.libcore.handler;

public class ServerStateCache {
	public enum ServerState {
		None,
		Running,
		Close
	}
	
	public static transient ServerState serverState = ServerState.None;
	
	public static void changeServerState(ServerState state) {
		serverState = state;
	}
	
	public static boolean serverIsRun() {
		return serverState == ServerState.Running;
	}
	
	public static boolean serverIsClose() {
		return serverState == ServerState.Close;
	}
}
