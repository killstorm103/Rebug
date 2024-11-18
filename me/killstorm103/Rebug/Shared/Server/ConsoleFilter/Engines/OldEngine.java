package me.killstorm103.Rebug.Shared.Server.ConsoleFilter.Engines;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.message.Message;

import me.killstorm103.Rebug.Shared.Main.Rebug;
import me.killstorm103.Rebug.Shared.Server.ConsoleFilter.Engine;

public class OldEngine implements Engine
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
    @SuppressWarnings("unused")
    @Override
    public void HideConsoleMessages () 
    {
    	((Logger) ((Object) LogManager.getRootLogger())).addFilter(new Filter()
        {
            public Filter.Result filter (LogEvent e)
            {
            	for (String s : Rebug.getINSTANCE().getConfig().getStringList("ConsoleFilter.blocked")) 
                {
                    if (!e.getMessage().toString().contains(s)) continue;
                    
                    return Filter.Result.DENY;
                }
                return null;
            }
            public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, String arg3, Object ... arg4)
            {
                return null;
            }

            public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Object arg3, Throwable arg4) 
            {
                return null;
            }

            public Filter.Result filter(Logger arg0, Level arg1, Marker arg2, Message arg3, Throwable arg4) 
            {
                return null;
            }

            public Filter.Result getOnMatch() {
                return null;
            }

            public Filter.Result getOnMismatch() {
                return null;
            }

			public void stop() {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method LifeCycle.stop()\n");
            }

            public boolean isStarted() {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method LifeCycle.isStarted()\n");
            }

			public void initialize() {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method LifeCycle.initialize()\n");
            }

            public boolean isStopped() {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method LifeCycle.isStopped()\n");
            }

            public void start() {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method LifeCycle.start()\n");
            }

			public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8, Object object9) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object, Object, Object, Object, Object)\n");
            }

            public Filter.Result filter(Logger logger, Level level, Marker marker, String string, Object object, Object object2, Object object3, Object object4, Object object5, Object object6, Object object7, Object object8, Object object9, Object object10) {
                throw new Error("Unresolved compilation problem: \n\tThe type new Filter(){} must implement the inherited abstract method Filter.filter(Logger, Level, Marker, String, Object, Object, Object, Object, Object, Object, Object, Object, Object, Object)\n");
            }
        });
    }
}
