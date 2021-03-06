package com.redstoner.modules.warn;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.nemez.cmdmgr.Command;
import com.redstoner.annotations.Version;
import com.redstoner.misc.Utils;
import com.redstoner.modules.Module;

@Version(major = 1, minor = 0, revision = 1, compatible = 1)
public class Warn implements Module
{
	private boolean enabled = false;
	
	@Command(hook = "warn")
	public void warn_normal(CommandSender sender)
	{
		String name = ((Player) sender).getDisplayName();
		Utils.broadcast(null, "§2Lag incoming! - §9" + name, null);
	}
	
	@Command(hook = "warnp")
	public void warn_possible(CommandSender sender)
	{
		String name = ((Player) sender).getDisplayName();
		Utils.broadcast(null, "§2Possible lag incoming! - §9" + name, null);
	}
	
	@Override
	public void onEnable()
	{
		enabled = true;
	}
	
	@Override
	public void onDisable()
	{
		enabled = false;
	}
	
	@Override
	public boolean enabled()
	{
		return enabled;
	}
	
	// @noformat
	@Override
	public String getCommandString()
	{
		return "command warn {\n" + 
				"	[empty] {\n" + 
				"		run warn;\n" + 
				"		type player;\n" + 
				"		help Warns other players about definite lag;\n" + 
				"		perm utils.warn;\n" + 
				"	}\n" + 
				"}\n" + 
				"\n" + 
				"command warnp {\n" + 
				"	[empty] {\n" + 
				"		run warnp;\n" + 
				"		type player;\n" + 
				"		help Warns other players about possible lag;\n" + 
				"		perm utils.warn;\n" + 
				"	}\n" + 
				"}";
	}
	//@format
}
