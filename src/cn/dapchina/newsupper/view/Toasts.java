package cn.dapchina.newsupper.view;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import cn.dapchina.newsupper.R;

public class Toasts {
	private static View view;

	public static Toast makeText(Context context, String content, int duration) {
		view = LayoutInflater.from(context).inflate(R.layout.toast_item, null);
		TextView textView = (TextView) view.findViewById(R.id.toast_text);
		textView.setText(content);
		Toast toast = new Toast(context);
		toast.setDuration(duration);
//		toast.setGravity(Gravity.BOTTOM, Gravity.CENTER, Gravity.CENTER);
		toast.setMargin(0, 0.05f);
		toast.setView(view);
		return toast;
	}
	
	public static Toast makeText(Context context, int id, int duration) {
		view = LayoutInflater.from(context).inflate(R.layout.toast_item, null);
		TextView textView = (TextView) view.findViewById(R.id.toast_text);
		textView.setText(context.getString(id));
		Toast toast = new Toast(context);
		toast.setDuration(duration);
//		toast.setGravity(Gravity.BOTTOM, Gravity.CENTER, Gravity.CENTER);
		toast.setMargin(0, 0.05f);
		toast.setView(view);
		return toast;
	}

}
