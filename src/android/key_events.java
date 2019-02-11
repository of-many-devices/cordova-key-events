package uk.co.nightsnowgames.cordova.key_events;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class key_events extends CordovaPlugin
{
	private static final String TAG = "key_events";

    private CallbackContext cordova_cb_context;
	private boolean cordova_ready = false;

	private void cordova_cb_json(JSONObject arg, boolean success)
	{
		if(cordova_ready == true)
		{
			if(cordova_cb_context != null)
			{
				PluginResult result = new PluginResult(success ? PluginResult.Status.OK : PluginResult.Status.ERROR, arg);
				result.setKeepCallback(true);
				cordova_cb_context.sendPluginResult(result);
			}
		}
		else
		{
			Log.d(TAG, "java dropping event, cordova not ready");
		}
	}

	@Override
	public boolean execute(String action, JSONArray args, CallbackContext callback_context) throws JSONException
	{
		Activity activity = this.cordova.getActivity();
		JSONObject result = new JSONObject();

		if("init".equals(action))
		{
			cordova_ready = true;
			result.put("event_type", "event_type_init");
			cordova_cb_json(result, true);
		}
		else
		{
			result.put("error_string", action + " is not a supported action");
			cordova_cb_json(result, false);
		}

		return false;
	}
}
