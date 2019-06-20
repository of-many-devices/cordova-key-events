package uk.co.nightsnowgames.cordova.plugins.key_events;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;

import org.apache.cordova.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class key_events extends CordovaPlugin implements OnKeyListener
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
		boolean ret_val = true;
		Activity activity = this.cordova.getActivity();
		JSONObject result = new JSONObject();

		if("init".equals(action))
		{
			this.webView.getView().setOnKeyListener(this);

			cordova_cb_context = callback_context;
			cordova_ready = true;
			result.put("event_type", "event_type_init");
			cordova_cb_json(result, true);
		}
		else
		{
			result.put("error_string", action + " is not a supported action");
			cordova_cb_json(result, false);
			ret_val = false;
		}

		return ret_val;
	}

	public void onDestroy()
	{
		this.webView.getView().setOnKeyListener(null);
	}

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event)
	{
		if(event.getAction() == KeyEvent.ACTION_DOWN)
		{
			boolean shift_pressed = (keyCode & KeyEvent.META_SHIFT_MASK) != 0;
			boolean ctrl_pressed = (keyCode & KeyEvent.META_CTRL_MASK) != 0;

			String key_code_string = (KeyEvent.keyCodeToString(~(KeyEvent.getModifierMetaStateMask()) & keyCode)).substring(8);

			JSONObject result = new JSONObject();

			if(shift_pressed)
			{
				key_code_string = "S+"+key_code_string;
			}
			if(ctrl_pressed)
			{
				key_code_string = "C+"+key_code_string;
			}

			Log.d(TAG, String.format("KEY EVENT :: %d :: [%s]\n", keyCode, key_code_string));

			try
			{
				result.put("event_type", "event_type_key_event");
				result.put("key_string", key_code_string);
			}
			catch (JSONException e)
			{
				Log.d(TAG, "failed to json encode key event!\n");
			}

			cordova_cb_json(result, true);
		}
		return true;
	}
}
