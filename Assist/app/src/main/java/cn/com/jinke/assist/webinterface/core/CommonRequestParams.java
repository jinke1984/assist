package cn.com.jinke.assist.webinterface.core;

import org.json.JSONObject;
import org.xutils.http.RequestParams;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import cn.com.jinke.assist.utils.JsonConstans;

public class CommonRequestParams extends RequestParams implements JsonConstans {

	public CommonRequestParams(Builder builder) {
		super(builder.uri);
		JSONObject jsonObj = new JSONObject();
		try {
			Iterator iterator = builder.map.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry entry = (Map.Entry) iterator.next();
				Object key = entry.getKey();
				Object value = entry.getValue();
				if (key != null && value != null) {
					// addParameter(key.toString(), value.toString());
					//addBodyParameter(key.toString(), value.toString());
					jsonObj.put(key.toString(), value.toString());
				}
			}
			String s_value = jsonObj.toString();
			addBodyParameter(JSON, s_value);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
//		Log.e("jinke", this.toString());

		//AppLogger.d("request", "request url: " + this.toString(), false);
		//AppLogger.d("request", "request params: " + jsonObj.toString(), false);
		// addParameter("params", jsonObj.toString());
	}

	public static class Builder {

		private String uri;
		private Map<String, Object> map = new HashMap<>();

		public Builder(String uri) {
			this.uri = uri;
		}

		public void putParams(String key, Object value) {
			map.put(key, value);
		}

		public CommonRequestParams build() {
			return new CommonRequestParams(this);
		}
	}
}
