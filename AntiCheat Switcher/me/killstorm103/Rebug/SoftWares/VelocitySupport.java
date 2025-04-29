package me.killstorm103.Rebug.SoftWares;

import lombok.Getter;

public class VelocitySupport 
{
	// TODO This isn't done add support!
	@Getter
	private static VelocitySupport INSTANCE;
	
	@Getter
	private final com.velocitypowered.api.proxy.ProxyServer proxyServer;
	public VelocitySupport (com.velocitypowered.api.proxy.ProxyServer server)
	{
		INSTANCE = this;
		this.proxyServer = (com.velocitypowered.api.proxy.ProxyServer) server;
	}
}
