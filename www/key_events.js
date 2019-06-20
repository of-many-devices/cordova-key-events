var exec = require('cordova/exec');

var key_events =
{
	event_cb: null,

	init: function(p_event_cb)
	{
		key_events.event_cb = p_event_cb;

		exec(key_events.success, key_events.error, "service_key_events", "init", []);
	},

	success: function(arg)
	{
		switch(arg["event_type"])
		{
			case "event_type_key_event":
				console.log("key_events: ["+arg["key_string"]+"]");
				key_events.event_cb.key_event(arg["key_string"]);
				break;

			case "event_type_init":
				console.log("key_events: init SUCCESS");
				break;

			default:
				console.log("key_events: unexpected success event: "+arg["event_type"]);
				break;
		}
	},

	error: function(arg)
	{
		console.log("key_events: error: "+arg["error_string"]);
		//console.log("key_events: error: "+arg);
	},
};

module.exports = key_events;
