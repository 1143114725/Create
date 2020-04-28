package cn.dapchina.newsupper.util;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by EraJieZhang
 * on 18-3-7.
 * 全局Toast工具类 唯一的toast
 */
public class BaseToast {
    private static Toast mToast = null;

    private BaseToast() {
    }

    /**
     * 显示一个长时间的toast
     * @param ctx   上下文
     * @param tips  显示文字
     */
    public static void showLongToast(final Context ctx, final String tips) {
        if (mToast != null) {
            mToast.setText(tips);
        } else {
            mToast = Toast.makeText(ctx, tips, Toast.LENGTH_LONG);
        }

        mToast.show();
    }
    /**
     * 显示一个长时间的toast
     * @param ctx   上下文
     * @param stringid  资源id
     */
    public static void showLongToast(final Activity ctx, final int stringid) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                    mToast.setText(stringid);
                } else {
                    mToast = Toast.makeText(ctx, stringid,Toast.LENGTH_LONG);
                }

                mToast.show();
            }
        });
    }
    /**
     * 显示一个短时间的toast
     * @param ctx   上下文
     * @param tips  显示文字
     */
    public static void showShortToast(final Activity ctx, final String tips) {
        if (null == ctx) {
            return;
        }
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                    mToast.setText(tips);
                } else {
                    mToast = Toast.makeText(ctx, tips,Toast.LENGTH_SHORT);
                }
                mToast.show();
            }
        });
    }

    /**
     * 显示一个短时间的toast
     * @param ctx   上下文
     * @param stringid  资源id
     */
    public static void showShortToast(final Activity ctx, final int stringid) {
        ctx.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (mToast != null) {
                    mToast.setText(stringid);
                } else {
                    mToast = Toast.makeText(ctx, stringid,Toast.LENGTH_SHORT);
                }

                mToast.show();
            }
        });
    }
    public static TextView textView;
   
    /**
     * 清除toast
     */
    public static void cancel() {
        if (mToast != null) {
            mToast.cancel();
        }
    }



    /**
     * 显示一个短时间的toast
     * @param ctx   上下文
     * @param tips  显示文字
     */
    public static void showShortToast(final Context ctx, final String tips) {
        if (null == ctx) {
            return;
        }
                if (mToast != null) {
                    mToast.setText(tips);
                } else {
                    mToast = Toast.makeText(ctx, tips,Toast.LENGTH_SHORT);
                }
                mToast.show();
    }



}
