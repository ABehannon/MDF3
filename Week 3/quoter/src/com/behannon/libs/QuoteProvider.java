//Alex Behannon
//08-29-2013
//Java Week 4

package com.behannon.libs;

import org.json.JSONException;
import org.json.JSONObject;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

public class QuoteProvider extends ContentProvider {

	public static final String AUTHORITY = "com.behannon.libs.QuoteProvider";

	public static class QuoteData implements  BaseColumns {

		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/items");

		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/behannon.quoter.item";
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/behannon.quoter.item";

		public static final String QUOTE_COLUMN = "quote";
		public static final String AUTHOR_COLUMN = "author";

		public static final String[] PROJECTION = {"_Id", QUOTE_COLUMN, AUTHOR_COLUMN};

		private QuoteData() {};
	}

	public static final int ITEMS = 1;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, "items/", ITEMS);
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public String getType(Uri uri) {
		String type = null;

		switch (uriMatcher.match(uri)) {
			case ITEMS:
				type = QuoteData.CONTENT_TYPE;
				break;
			default:
				break;
		}
		return type;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		return false;
	}

	@SuppressWarnings("unused")
	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		MatrixCursor result = new MatrixCursor(QuoteData.PROJECTION);

		String read = FileSaving.readStringFile(getContext(), "quoteData", false);

		JSONObject json;
		JSONObject data = null;

		try {
			json = new JSONObject(read);
			String quote = json.get("quote").toString();
			String author = json.get("author").toString();
			
		} catch (JSONException e) {
			e.printStackTrace();
		}

		if (data == null) {
			return result;
		}

		switch (uriMatcher.match(uri)) {
			case ITEMS:
			try {
				json = new JSONObject(read);
				String quote = json.get("quote").toString();
				String author = json.get("author").toString();
				
				result.addRow(new Object[] {0, quote, author});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				break;
			default:
				Log.e("QUERY", "INVALID URI = " + uri.toString());
		}

		return result;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		throw new UnsupportedOperationException();
	}

}