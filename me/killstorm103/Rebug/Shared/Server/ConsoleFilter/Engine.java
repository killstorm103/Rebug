package me.killstorm103.Rebug.Shared.Server.ConsoleFilter;

public interface Engine 
{
	public void HideConsoleMessages();
    public int getHiddenMessagesCount();
    public void AddHiddenMessage ();
}
