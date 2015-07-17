package com.huizhuang.zxsq.http;

import android.app.DownloadManager.Request;

public abstract class AbstractHttpResponseHandler<T> {
	/** Inform when start to handle this Request. */
	public void onStart() {
	}

	/** Inform when the {@link Request} is truly cancelled. */
	public void onCancel() {
	}

	/** Called when response success. */
	public abstract void onSuccess(T t);

	/**
	 * Callback method that an error has been occurred with the provided error
	 * code and optional user-readable message.
	 */
	public abstract void onFailure(int code, String error);

	/**
	 * Inform When the {@link Request} cache non-exist or expired, this callback
	 * method is opposite by the onUsedCache(), means the http retrieving will
	 * happen soon.
	 */
	public void onNetworking() {
	}

	/**
	 * Inform when the cache already use, it means http networking won't
	 * execute.
	 */
	public void onUsedCache(T t) {
	}

	/** Inform when {@link Request} execute is going to retry. */
	public void onRetry() {
	}

	/**
	 * Inform when download progress change, this callback method only available
	 * when request was
	 * {@link com.lgmshare.component.network.request.FileDownloadRequest}.
	 */
	public void onProgress(long fileSize, long downloadedSize) {
	}

	/**
	 * Inform when {@link Request} execute is finish, whatever success or error
	 * or cancel, this callback method always invoke if request is done.
	 */
	public void onFinish() {
	}
}