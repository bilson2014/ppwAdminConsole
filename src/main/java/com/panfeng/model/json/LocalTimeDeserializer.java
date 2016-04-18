package com.panfeng.model.json;

import java.lang.reflect.Type;
import org.joda.time.LocalTime;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LocalTimeDeserializer implements JsonDeserializer<LocalTime> {

	@Override
	public LocalTime deserialize(final JsonElement json, final Type type, final JsonDeserializationContext ctx) throws JsonParseException {
		return new LocalTime(json.getAsString());
	}
}
