package com.redstoner.modules.serversigns;

import com.google.common.collect.ImmutableSet;
import com.redstoner.misc.Main;
import com.redstoner.misc.persistence.FileAdapter;
import com.redstoner.misc.persistence.JsonFileAdapter;
import com.redstoner.modules.Module;

import java.io.*;
import java.util.Set;

public class ServerSigns implements Module {
	private final Set<String> commandWhitelist;
	private static final int maxLineLength = 256;
	private static final int maxLines = 20;
	private static final int helpPageSize = 12;
	private final String signsFile = new File(Main.plugin.getDataFolder(), "serversigns.json").getAbsolutePath();
	private final FileAdapter<SignMap> adapter = JsonFileAdapter.create(SignMap::new, ex -> {
		if (!(ex instanceof FileNotFoundException)) {
			Main.plugin.getLogger().warning("Error occurred while loading/saving signs for serversigns module:");
			ex.printStackTrace();
		}
	});
	private SignMap signs;
	private boolean enabled;
	
	public ServerSigns() {
		commandWhitelist = ImmutableSet.copyOf(new String[]{
				"mail", "email", "memo",
				"echo", "ping",
				"cg join",
				"cg info",
				"chatgroup join",
				"chatgroup info",
				"i",
				"item",
				"p h", "plot home", "plot h", "p home", "plotme home", "plotme h",
				"up",
				"tppos",
				"seen"});
	}
	
	@Override
	public void onEnable() {
		enabled = true;
		signs = adapter.load(signsFile);
	}
	
	@Override
	public void onDisable() {
		enabled = false;
	}
	
	private static String representSignPos(BlockPos pos) {
		return String.format("sign at (%d, %d, %d) in %s", pos.getX(), pos.getY(), pos.getZ(), pos.getWorldName());
	}
	
	@Override
	public boolean enabled() {
		return enabled;
	}
	
	@Override
	public String getCommandString() {
		try (Reader reader = new InputStreamReader(ServerSigns.class.getResourceAsStream("serversigns.cmd"));
			 BufferedReader buf = new BufferedReader(reader)) {
			StringBuilder builder = new StringBuilder();
			String line;
			while ((line = buf.readLine()) != null) {
				builder.append(line).append('\n');
			}
			return builder.toString();
		} catch (IOException | NullPointerException ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
}
