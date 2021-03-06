package ca.ualberta.cs.providers;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Type;
import java.util.Date;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Manages a JsonSerializer and JsonDeserializer
 * @author wyatt
 *
 */
abstract public class GeoChanGson {
	protected Gson gson;

	// https://gist.github.com/orip/3635246
	// Using Android's base64 libraries. This can be replaced with any base64
	// library.
	private static class ByteArrayToBase64TypeAdapter implements
			JsonSerializer<byte[]>, JsonDeserializer<byte[]> {
		@Override
		public byte[] deserialize(JsonElement json, Type typeOfT,
				JsonDeserializationContext context) throws JsonParseException {
			Log.w("GeoChanGson", "Byte Deserialization in progress.");
			return Base64.decode(json.getAsString(), Base64.NO_WRAP);
		}

		@Override
		public JsonElement serialize(byte[] src, Type typeOfSrc,
				JsonSerializationContext context) {
			Log.w("GeoChanGson", "Byte Serialization in progress. Length is: "
					+ src.length);
			return new JsonPrimitive(Base64.encodeToString(src, Base64.NO_WRAP));
		}
	};

	private static class BitmapToBase64TypeAdapyer implements
			JsonSerializer<Bitmap>, JsonDeserializer<Bitmap> {

		@Override
		public Bitmap deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			byte[] theBitmapBytes = Base64.decode(arg0.getAsString(),
					Base64.NO_WRAP);

			return BitmapFactory.decodeByteArray(theBitmapBytes, 0,
					theBitmapBytes.length);
		}

		@Override
		public JsonElement serialize(Bitmap arg0, Type arg1,
				JsonSerializationContext arg2) {
			ByteArrayOutputStream blob = new ByteArrayOutputStream();

			arg0.compress(CompressFormat.WEBP, 100, blob);

			byte[] byteArray = blob.toByteArray();

			Log.w("GeoChanGson",
					"Bitmap Serialization in progress. Length is: "
							+ byteArray.length);

			return new JsonPrimitive(Base64.encodeToString(byteArray,
					Base64.NO_WRAP));
		}

	}

	/*
	 * JsonSerializer<Date>, JsonDeserializer<Date>
	 */
	private static class DateToUnixTimestampTypeAdapter implements
			JsonSerializer<Date>, JsonDeserializer<Date> {

		@Override
		public Date deserialize(JsonElement arg0, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {
			return new Date(arg0.getAsJsonPrimitive().getAsLong());
		}

		@Override
		public JsonElement serialize(Date arg0, Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0.getTime());
		}

	}

	/*
	 * JsonSerializer<Location>, JsonDeserializer<Location>
	 */
	private static class LocationTypeAdapter implements
			JsonSerializer<Location>, JsonDeserializer<Location> {

		@Override
		public Location deserialize(JsonElement json, Type arg1,
				JsonDeserializationContext arg2) throws JsonParseException {

			// Load the object
			final JsonObject jsonObject = json.getAsJsonObject();

			// Get the lat element
			final JsonElement jsonLat = jsonObject.get("lat");

			// Check that it's there
			if (jsonLat == null) {
				// Else, freak out
				throw new JsonParseException("No lat field found on object: "
						+ jsonObject.toString());
			}

			// Get the double for the lat
			final double lat = jsonLat.getAsDouble();

			// Get the lon element
			final JsonElement jsonLon = jsonObject.get("lon");

			// Check that it's there
			if (jsonLon == null) {
				// Else, freak out
				throw new JsonParseException("No lon field found on object: "
						+ jsonObject.toString());
			}

			// Get the lon double
			final double lon = jsonLon.getAsDouble();

			// Construct a new location from the data and return
			Location newLocation = new Location("");
			newLocation.setLatitude(lat);
			newLocation.setLongitude(lon);
			return newLocation;
		}

		@Override
		public JsonElement serialize(Location arg0, Type arg1,
				JsonSerializationContext arg2) {
			// Construct a raw Json object from the location data
			JsonObject result = new JsonObject();
			result.add("lat", new JsonPrimitive(arg0.getLatitude()));
			result.add("lon", new JsonPrimitive(arg0.getLongitude()));

			return result;
		}

	}

	protected GeoChanGson() {
		GsonBuilder gsonBuilder = createGsonBuilder();

		// Generate gson object
		gson = gsonBuilder.create();
	}

	protected GsonBuilder createGsonBuilder() {
		// Get builder
		GsonBuilder gsonBuilder = new GsonBuilder();

		// Date objects
		gsonBuilder.registerTypeHierarchyAdapter(Date.class,
				new DateToUnixTimestampTypeAdapter());

		// Byte objects
		gsonBuilder.registerTypeHierarchyAdapter(byte[].class,
				new ByteArrayToBase64TypeAdapter());

		// Bitmap objects
		gsonBuilder.registerTypeHierarchyAdapter(Bitmap.class,
				new BitmapToBase64TypeAdapyer());

		// Location objects
		gsonBuilder.registerTypeHierarchyAdapter(Location.class,
				new LocationTypeAdapter());

		return gsonBuilder;
	}
}
