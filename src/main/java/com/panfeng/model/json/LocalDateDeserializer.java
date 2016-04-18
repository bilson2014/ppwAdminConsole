package com.panfeng.model.json;

import java.lang.reflect.Type;
import org.joda.time.LocalDate;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class LocalDateDeserializer implements JsonDeserializer<LocalDate> {

	@Override
	public LocalDate deserialize(final JsonElement json, final Type type, final JsonDeserializationContext ctx) throws JsonParseException {
		return new LocalDate(json.getAsString());
	}
}
