package me.killstorm103.Rebug.Command.Handler.Paper;

import java.util.Collection;
import io.papermc.paper.command.brigadier.BasicCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

public class PaperShortCutBasic implements BasicCommand
{
	@Override
	public Collection<String> suggest (CommandSourceStack sender, String[] args)
	{
		return BasicCommand.super.suggest(sender, args);
	}
	@Override
	public void execute (CommandSourceStack sender, String[] args) {}
}
