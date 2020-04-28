package cn.dapchina.newsupper.oninterface;

import cn.dapchina.newsupper.view.ObservableScrollView;

public interface ScrollViewListener {
	 void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy);  
}
