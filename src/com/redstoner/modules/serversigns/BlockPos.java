package com.redstoner.modules.serversigns;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.redstoner.misc.persistence.JsonLoadable;
import org.bukkit.block.Block;

import java.io.IOException;

// I'd normally use a class from my lib for this but let's keep it simple today.
public class BlockPos implements JsonLoadable {
	private String worldName;
	private int x, y, z;
	
	public BlockPos() {
	}
	
	public BlockPos(String worldName, int x, int y, int z) {
		this.worldName = worldName;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public BlockPos(Block block) {
		this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
	}
	
	public String getWorldName() {
		return worldName;
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getZ() {
		return z;
	}
	
	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		
		BlockPos blockPos = (BlockPos) o;
		
		if (x != blockPos.x) return false;
		if (y != blockPos.y) return false;
		if (z != blockPos.z) return false;
		return worldName != null ? worldName.equals(blockPos.worldName) : blockPos.worldName == null;
	}
	
	@Override
	public int hashCode() {
		int result = worldName != null ? worldName.hashCode() : 0;
		result = 31 * result + x;
		result = 31 * result + y;
		result = 31 * result + z;
		return result;
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		if (worldName != null) {
			writer.name("w").value(worldName);
		}
		if (x != 0) {
			writer.name("x").value(x);
		}
		if (y != 0) {
			writer.name("y").value(y);
		}
		if (z != 0) {
			writer.name("z").value(z);
		}
		writer.endObject();
	}
	
	@Override
	public void loadFrom(JsonReader reader) throws IOException {
		reader.beginObject();
		while (reader.hasNext()) {
			String key = reader.nextName();
			if (!key.isEmpty()) switch (key.charAt(0)) {
				case 'w':
					worldName = reader.nextString();
					break;
				case 'x':
					x = reader.nextInt();
					break;
				case 'y':
					y = reader.nextInt();
					break;
				case 'z':
					z = reader.nextInt();
					break;
				default:
					reader.skipValue();
					break;
			}
		}
		reader.endObject();
	}
	
}
