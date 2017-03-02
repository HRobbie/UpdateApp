package com.hrobbie.updateapp.util.inteface;

import org.xutils.common.Callback;

public abstract class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType>{

	@Override
	public void onSuccess(ResultType result) {
		//可以根据公司的需求进行统一的请求成功的逻辑处理
	}




	@Override
	public void onError(Throwable ex, boolean isOnCallback) {
		//可以根据公司的需求进行统一的请求网络失败的逻辑处理
	}

	@Override
	public void onCancelled(CancelledException cex) {
		
	}

	@Override
	public void onFinished() {
		
	}

}
