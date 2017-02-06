package com.redstoner.modules.serversigns;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.redstoner.misc.persistence.JsonLoadable;

import java.io.IOException;
import java.util.HashMap;

public class SignMap extends HashMap<BlockPos, SignEntry> implements JsonLoadable {
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginArray();
		for (Entry<BlockPos, SignEntry> entry : this.entrySet()) {
			writer.beginObject();
			writer.name("pos");
			entry.getKey().writeTo(writer);
			writer.name("data");
			entry.getValue().writeTo(writer);
			writer.endObject();
		}
		writer.endArray();
	}
	
	@Override
	public void loadFrom(JsonReader reader) throws IOException {
		clear();
		reader.beginArray();
		while (reader.hasNext()) {
			BlockPos pos = null;
			SignEntry data = null;
			reader.beginObject();
			while (reader.hasNext()) {
				switch (reader.nextName()) {
					case "pos":
						pos = new BlockPos();
						pos.loadFrom(reader);
						break;
					case "data":
						data = new SignEntry();
						data.loadFrom(reader);
						break;
					default:
						reader.skipValue();
				}
			}
			reader.endObject();
			if (pos != null && data != null) {
				put(pos, data);
			}
		}
		reader.endArray();
	}
	
}
