package cn.dapchina.newsupper.xhttp;



/**
 * Created by EEH on 2018/1/17.
 */

public interface HttpCallBack<String> {

    void onNext(String result);

    void onError();
}
