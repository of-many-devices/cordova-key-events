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
				key_events.event_cb.key_event(parseInt(arg["key_event"]));
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
