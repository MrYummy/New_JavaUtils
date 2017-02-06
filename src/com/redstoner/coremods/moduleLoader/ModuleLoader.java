package com.redstoner.coremods.moduleLoader;

import com.nemez.cmdmgr.Command;
import com.nemez.cmdmgr.Command.AsyncType;
import com.nemez.cmdmgr.CommandManager;
import com.redstoner.annotations.AutoRegisterListener;
import com.redstoner.annotations.Debugable;
import com.redstoner.annotations.Version;
import com.redstoner.coremods.debugger.Debugger;
import com.redstoner.misc.Main;
import com.redstoner.misc.Utils;
import com.redstoner.modules.CoreModule;
import com.redstoner.modules.Module;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

import java.util.ArrayList;

/** The module loader, mother of all modules. Responsible for loading and taking care of all modules.
 * 
 * @author Pepich */
@Version(major = 1, minor = 3, revision = 2, compatible = -1)
public final class ModuleLoader implements CoreModule
{
	private static ModuleLoader instance;
	private static final ArrayList<Module> modules = new ArrayList<Module>();
	
	private ModuleLoader()
	{}
	
	public static void init()
	{
		if (instance == null)
			instance = new ModuleLoader();
		CommandManager.registerCommand(instance.getCommandString(), instance, Main.plugin);
	}
	
	/** This method will add a module to the module list, without enabling it
	 * 
	 * @param clazz The class of the module to be added. */
	@Debugable
	public static final void addModule(Class<? extends Module> clazz)
	{
		Debugger.notifyMethod(clazz);
		try
		{
			Module module = clazz.newInstance();
			modules.add(module);
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			Utils.error("Could not add " + clazz.getName() + " to the list, constructor not accessible.");
		}
	}
	
	/** Call this to enable all not-yet enabled modules that are known to the loader. */
	@Debugable
	public static final void enableModules()
	{
		Debugger.notifyMethod();
		for (Module module : modules)
		{
			if (module.enabled())
				continue;
			try
			{
				if (module.enable())
				{
					CommandManager.registerCommand(module.getCommandString(), module, Main.plugin);
					if (module.getClass().isAnnotationPresent(AutoRegisterListener.class)
							&& (module instanceof Listener))
					{
						Bukkit.getPluginManager().registerEvents((Listener) module, Main.plugin);
					}
					Utils.log("Loaded module " + module.getClass().getName());
				}
				else
					Utils.error("Failed to load module " + module.getClass().getName());
			}
			catch (Exception e)
			{
				Utils.error("Failed to load module " + module.getClass().getName());
			}
		}
	}
	
	/** This method enables a specific module. If no module with that name is known to the loader yet it will be added to the list.
	 * 
	 * @param clazz The class of the module to be enabled.
	 * @return true, when the module was successfully enabled. */
	@Debugable
	public static final boolean enableModule(Class<? extends Module> clazz)
	{
		Debugger.notifyMethod(clazz);
		for (Module m : modules)
		{
			if (m.getClass().equals(clazz))
			{
				if (m.enable())
				{
					if (m.getClass().isAnnotationPresent(AutoRegisterListener.class) && (m instanceof Listener))
					{
						Bukkit.getPluginManager().registerEvents((Listener) m, Main.plugin);
					}
					Utils.log("Loaded module " + m.getClass().getName());
					return true;
				}
				else
				{
					Utils.error("Failed to load module " + m.getClass().getName());
					return false;
				}
			}
		}
		try
		{
			Module m = clazz.newInstance();
			modules.add(m);
			m.onEnable();
			if (m.enabled())
			{
				if (m.getClass().isAnnotationPresent(AutoRegisterListener.class) && (m instanceof Listener))
				{
					Bukkit.getPluginManager().registerEvents((Listener) m, Main.plugin);
				}
				Utils.log("Loaded module " + m.getClass().getName());
				return true;
			}
			else
			{
				Utils.error("Failed to load module " + m.getClass().getName());
				return false;
			}
		}
		catch (InstantiationException | IllegalAccessException e)
		{
			Utils.error("Could not add " + clazz.getName() + " to the list, constructor not accessible.");
			return false;
		}
	}
	
	// @noformat
	@Override
	public String getCommandString()
	{
		return "command modules {\n" + 
				"	list{\n" + 
				"		help Lists all modules. Color indicates status: §aENABLED §cDISABLED;\n" + 
				"		perm jutils.admin;\n" + 
				"		run list;\n" + 
				"	}\n" + 
				"}";
	}
	// @format
	
	/** This method lists all modules to the specified CommandSender. The modules will be color coded correspondingly to their enabled status.
	 * 
	 * @param sender The person to send the info to, usually the issuer of the command or the console sender.
	 * @return true. */
	@Command(hook = "list", async = AsyncType.ALWAYS)
	public boolean listModulesCommand(CommandSender sender)
	{
		Utils.sendModuleHeader(sender);
		StringBuilder sb = new StringBuilder("Modules:\n");
		for (Module m : modules)
		{
			String[] classPath = m.getClass().getName().split("\\.");
			String classname = classPath[classPath.length - 1];
			sb.append(m.enabled() ? "&a" : "&c");
			sb.append(classname);
			sb.append(", ");
		}
		sb.delete(sb.length() - 2, sb.length());
		Utils.sendMessage(sender, " §e", sb.toString(), '&');
		Utils.sendMessage(sender, " §7", "For more detailed information, consult the debugger.");
		return true;
	}
	
	public static void disableModules()
	{
		for (Module module : modules)
		{
			if (module.enabled())
			{
				module.onDisable();
			}
		}
	}
}
