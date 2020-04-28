package cn.dapchina.newsupper.xhttp;

import java.util.HashMap;
import java.util.Map;

import org.xutils.x;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

public class SendHttp {

	/**
	 * xutils 发送get请求
	 * 
	 * @param url 地址
	 * @param maps	参数集合
	 * @param call	回掉方法
	 */
	public static void gethttp(String url,Map<String, String> maps,final HttpCallBack<String> call) {
		
		
		
		
		RequestParams params = new RequestParams(url);
	    if (maps != null && !maps.isEmpty()) {
			for (Map.Entry<String, String> entry : maps.entrySet()) {
			    params.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
	    }
	    System.out.println("xutils url::"+params.getUri());
		x.http().get(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				call.onNext(result);
			}

			// 请求异常后的回调方法
			@Override
			public void onError(Throwable ex, boolean isOnCallback) {
				call.onError();
			}

			// 主动调用取消请求的回调方法
			@Override
			public void onCancelled(CancelledException cex) {
			}

			@Override
			public void onFinished() {
			}
		});
	}
}


