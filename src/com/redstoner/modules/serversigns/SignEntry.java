package com.redstoner.modules.serversigns;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.redstoner.misc.persistence.JsonLoadable;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.*;

public class SignEntry implements JsonLoadable {
	private UUID owner;
	private List<String> messages;
	
	public SignEntry() {
	}
	
	public SignEntry(UUID owner, String... messages) {
		setOwner(owner);
		this.messages = new ArrayList<>(Arrays.asList(messages));
	}
	
	public UUID getOwner() {
		return owner;
	}
	
	public void setOwner(UUID owner) {
		this.owner = Objects.requireNonNull(owner);
	}
	
	public List<String> getMessages() {
		return messages;
	}
	
	public boolean canEdit(Player player) {
		return owner.equals(player.getUniqueId()) || player.hasPermission("utils.serversigns.admin");
	}
	
	@Override
	public void writeTo(JsonWriter writer) throws IOException {
		writer.beginObject();
		writer.name("owner").value(owner.toString());
		writer.name("messages");
		writer.beginArray();
		for (String msg : messages) {
			writer.value(msg);
		}
		writer.endArray();
		writer.endObject();
	}
	
	@Override
	public void loadFrom(JsonReader reader) throws IOException {
		messages = new ArrayList<>();
		reader.beginObject();
		while (reader.hasNext()) {
			switch (reader.nextName()) {
				case "owner": {
					String input = reader.nextString();
					try {
						owner = UUID.fromString(input);
					} catch (IllegalArgumentException ignored) {
					}
					break;
				}
				case "messages": {
					reader.beginArray();
					while (reader.hasNext()) {
						messages.add(reader.nextString());
					}
					reader.endArray();
					break;
				}
				default:
					reader.skipValue();
			}
		}
		reader.endObject();
		Objects.requireNonNull(owner);
	}
	
}
