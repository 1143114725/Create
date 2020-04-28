package cn.dapchina.newsupper.xhttp;

/**
 xutils 网络请求管理类
 Created by EEH on 2018/6/15.
 */

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.image.ImageOptions;
import org.xutils.x;



import java.io.File;
import java.util.Map;

/**
 *
 */

public class Xutils {

    private volatile static Xutils instance;
    private Handler handler;
    private ImageOptions options;

    private Xutils() {

        handler = new Handler(Looper.getMainLooper());
    }

    /**
     * 单例模式
     *
     * @return
     */
    public static Xutils getInstance() {

        if (instance == null) {
            synchronized (Xutils.class) {
                if (instance == null) {
                    instance = new Xutils();
                }
            }
        }
        return instance;
    }

    /**
     * 异步get请求
     *
     * @param url
     * @param maps
     * @param callBack
     */
    public void get(String url, Map<String, String> maps, final XCallBack callBack) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //成功
                onSuccessResponse(result, callBack);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
                ex.printStackTrace();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //取消
            }

            @Override
            public void onFinished() {
                //最后结果
            }
        });

    }

    
    /**
     * 异步get请求
     *
     * @param url
     * @param maps
     * @param callBack
     */
    public void get2(String url, Map<String, String> maps, final XCallBack2 callBack) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().get(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {
                //成功
//                onSuccessResponse(result, callBack);
            	callBack.onResponse(result);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                //失败
            	callBack.onError();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                //取消
            }

            @Override
            public void onFinished() {
                //最后结果
            }
        });

    }
    /**
     * 异步get请求返回结果,json字符串
     *
     * @param result
     * @param callBack
     */
    private void onSuccessResponse(final String result, final XCallBack callBack) {

        handler.post(new Runnable() {
            @Override
            public void run() {

                if (callBack != null) {
                    callBack.onResponse(result);
                }
            }
        });
    }

    /**
     * 异步post请求
     *
     * @param url
     * @param maps
     * @param callback
     */
    public void post(String url, Map<String, String> maps, final XCallBack callback) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setReadTimeout(50000);
        x.http().post(params, new Callback.CommonCallback<String>() {

            @Override
            public void onSuccess(String result) {

                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    /**
     * 带缓存数据的异步 get请求
     *
     * @param url
     * @param maps
     * @param pnewCache
     * @param callback
     */
    public void getCache(String url, Map<String, String> maps, final boolean pnewCache, final XCallBack callback) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addQueryStringParameter(entry.getKey(), entry.getValue());
            }
        }
        x.http().get(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {

                boolean newCache = pnewCache;
                if (newCache) {
                    newCache = ! newCache;
                }
                if (! newCache) {
                    newCache = ! newCache;
                    onSuccessResponse(result, callback);
                }
                return newCache;
            }            @Override
            public void onSuccess(String result) {

                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }


        });
    }


    /**
     * 正常图片显示
     *
     * @param iv
     * @param url
     * @param option
     */
    //public void bindCommonImage(ImageView iv, String url, boolean option) {
    //    if (option) {
    //	options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.icon_stub)
    //		.setFailureDrawableId(R.mipmap.icon_error).build();
    //	x.image().bind(iv, url, options);
    //    } else {
    //	x.image().bind(iv, url);
    //    }
    //}

    /**
     * 圆形图片显示
     *
     * @param iv
     * @param url
     * @param option
     */
    //public void bindCircularImage(ImageView iv, String url, boolean option) {
    //    if (option) {
    //	options = new ImageOptions.Builder().setLoadingDrawableId(R.mipmap.icon_stub).setFailureDrawableId(R.mipmap.icon_error).setCircular(true).build();
    //	x.image().bind(iv, url, options);
    //    } else {
    //	x.image().bind(iv, url);
    //    }
    //}

    /**
     * 带缓存数据的异步 post请求
     *
     * @param url
     * @param maps
     * @param pnewCache
     * @param callback
     */
    public void postCache(String url, Map<String, String> maps, final boolean pnewCache, final XCallBack callback) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public void onSuccess(String result) {

                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }


            @Override
            public void onFinished() {

            }

            @Override
            public boolean onCache(String result) {

                boolean newCache = pnewCache;
                if (newCache) {
                    newCache = ! newCache;
                }
                if (! newCache) {
                    newCache = ! newCache;
                    onSuccessResponse(result, callback);
                }
                return newCache;
            }
        });
    }

    /**
     * 文件上传
     *
     * @param url
     * @param maps
     * @param file
     * @param callback
     */
    public void upLoadFile(String url, Map<String, String> maps, File file, final XCallBack callback) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        if (file != null) {
//            for (Map.Entry<String, File> entry : file.entrySet()) {
//                params.addBodyParameter(entry.getKey(), entry.getValue().getAbsoluteFile());
//            }
        	 params.addBodyParameter(file.getName(), file);
        }
        // 有上传文件时使用multipart表单, 否则上传原始文件流.
        params.setMultipart(true);
        x.http().post(params, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {

                onSuccessResponse(result, callback);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });

    }

    /**
     * 文件下载
     * @param SAVE_FILE_PATH 文件保存位置
     * @param url
     * @param maps
     * @param callBack
     */
    public void downLoadFile(String SAVE_FILE_PATH, String url, Map<String, String> maps, final XDownLoadCallBack callBack) {

        RequestParams params = new RequestParams(url);
        if (maps != null && ! maps.isEmpty()) {
            for (Map.Entry<String, String> entry : maps.entrySet()) {
                params.addBodyParameter(entry.getKey(), entry.getValue());
            }
        }
        params.setAutoRename(true);// 断点续传
        params.setSaveFilePath(SAVE_FILE_PATH);
        x.http().post(params, new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {

            }            @Override
            public void onSuccess(final File result) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (callBack != null) {
                            callBack.onResponse(result);
                        }
                    }
                });
            }

            @Override
            public void onStarted() {

            }            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onLoading(final long total, final long current, final boolean isDownloading) {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (callBack != null) {
                            callBack.onLoading(total, current, isDownloading);
                        }
                    }
                });
            }            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (callBack != null) {
                            callBack.onFinished();
                        }
                    }
                });
            }






        });

    }


    public interface XCallBack {

        void onResponse(String result);
    }


    public interface XCallBack2 {

        void onResponse(String result);
        void onError();
    }
    
    public interface XDownLoadCallBack extends XCallBack {

        void onResponse(File result);

        void onLoading(long total, long current, boolean isDownloading);

        void onFinished();
    }
    
    
    
    


}