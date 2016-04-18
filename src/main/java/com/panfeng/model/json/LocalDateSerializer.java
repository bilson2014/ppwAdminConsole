package com.panfeng.model.json;

import java.lang.reflect.Type;
import org.joda.time.LocalDate;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class LocalDateSerializer implements JsonSerializer<LocalDate> {

	@Override
	public JsonElement serialize(final LocalDate date, final Type type, final JsonSerializationContext ctx) {
		return new JsonPrimitive(date.toString("yyyy-MM-dd"));
	}

}
