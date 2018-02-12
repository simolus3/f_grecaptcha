package de.simolus3.fgrecaptcha;

import android.support.annotation.NonNull;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class FGrecaptchaPlugin implements MethodCallHandler {

	private Registrar registrar;

	private FGrecaptchaPlugin(Registrar registrar) {
		this.registrar = registrar;
	}

	public static void registerWith(Registrar registrar) {
		final MethodChannel channel = new MethodChannel(registrar.messenger(), "f_grecaptcha");
		channel.setMethodCallHandler(new FGrecaptchaPlugin(registrar));
	}

	@Override
	public void onMethodCall(MethodCall call, final Result result) {
		if (call.method.equals("isSupported")) {
			result.success(true);
		} else if (call.method.equals("verify")) {
			String siteKey = call.argument("key");

			SafetyNet.getClient(registrar.activity()).verifyWithRecaptcha(siteKey)
				.addOnSuccessListener(new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>() {
					@Override
					public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response) {
						result.success(response.getTokenResult());
					}
				})
				.addOnFailureListener(new OnFailureListener() {
					@Override
					public void onFailure(@NonNull Exception e) {
						e.printStackTrace();
						result.error("f_grecaptcha",
								"Verification using reCaptcha has failed", null);
					}
				});
		} else {
			result.notImplemented();
		}
	}
}
