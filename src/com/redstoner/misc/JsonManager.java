package com.redstoner.misc;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.redstoner.annotations.Version;

/** This class provides simple JSON handling, like storing and loading from and to files.
 * 
 * @author Pepich */
@Version(major = 1, minor = 0, revision = 1, compatible = -1)
public class JsonManager
{
	private JsonManager()
	{}
	
	/** Loads a JSONObject from a file.
	 * 
	 * @param source the file to load from.
	 * @return the JSONObject or null if the source does not contain a valid JSONObject. */
	public static JSONObject getObject(File source)
	{
		if (!source.exists())
			return null;
		JSONParser parser = new JSONParser();
		try
		{
			FileReader reader = new FileReader(source);
			Object rawObject = parser.parse(reader);
			reader.close();
			JSONObject jsonObject = (JSONObject) rawObject;
			return jsonObject;
		}
		catch (IOException | ParseException e)
		{}
		return null;
	}
	
	/** Saves a JSONObject to a file. Will create the necessary FileStructure like folders and the file itself.</br>
	 * Note that this operation will be run on a different thread and you do not need to take care of that yourself.
	 * 
	 * @param object the JSONObject to save.
	 * @param destination the file to write to. */
	public static void save(JSONObject object, File destination)
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (destination.exists())
					destination.delete();
				else if (!destination.getParentFile().exists())
					destination.getParentFile().mkdirs();
				try
				{
					destination.createNewFile();
					FileWriter writer = new FileWriter(destination);
					object.writeJSONString(writer);
					writer.flush();
					writer.close();
				}
				catch (IOException e)
				{}
			}
		});
		t.start();
	}
	
	/** Loads a JSONArray from a file.
	 * 
	 * @param source the file to load from.
	 * @return the JSONArray or null if the source does not contain a valid JSONArray. */
	public static JSONArray getArray(File source)
	{
		if (!source.exists())
			return null;
		JSONParser parser = new JSONParser();
		try
		{
			Object rawObject = parser.parse(new FileReader(source));
			JSONArray jsonArray = (JSONArray) rawObject;
			return jsonArray;
		}
		catch (IOException | ParseException e)
		{}
		return null;
	}
	
	/** Saves a JSONArray to a file. Will create the necessary FileStructure like folders and the file itself.</br>
	 * Note that this operation will be run on a different thread and you do not need to take care of that yourself.
	 * 
	 * @param object the JSONArray to save.
	 * @param destination the file to write to. */
	public static void save(JSONArray array, File destination)
	{
		Thread t = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				if (destination.exists())
					destination.delete();
				else if (!destination.getParentFile().exists())
					destination.getParentFile().mkdirs();
				try
				{
					destination.createNewFile();
					FileWriter writer = new FileWriter(destination);
					array.writeJSONString(writer);
					writer.flush();
					writer.close();
				}
				catch (IOException e)
				{}
			}
		});
		t.start();
	}
}
