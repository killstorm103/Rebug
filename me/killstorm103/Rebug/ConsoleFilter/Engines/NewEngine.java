package me.killstorm103.Rebug.ConsoleFilter.Engines;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import me.killstorm103.Rebug.ConsoleFilter.Engine;
import me.killstorm103.Rebug.ConsoleFilter.LogFilter;

public class NewEngine implements Engine
{
	private int Hidden = 0;
	
	@Override
    public int getHiddenMessagesCount ()
	{
        return Hidden;
    }

    @Override
    public void AddHiddenMessage ()
    {
        Hidden ++;
    }
    @Override
	public void HideConsoleMessages()
	{
    	 ((Logger)((Object) LogManager.getRootLogger())).addFilter(new LogFilter());
	}
}
