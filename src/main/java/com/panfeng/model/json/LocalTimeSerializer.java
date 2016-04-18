package com.panfeng.model.json;

import java.lang.reflect.Type;
import org.joda.time.LocalTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalTimeSerializer implements JsonSerializer<LocalTime> {

	@Override
	public JsonElement serialize(final LocalTime time, final Type type, final JsonSerializationContext ctx) {
		return new JsonPrimitive(time.toString("HH:mm:ss"));
	}

}
