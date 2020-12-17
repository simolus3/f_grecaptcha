package de.simolus3.fgrecaptcha;

import android.app.Activity;

import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import androidx.annotation.NonNull;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

public class FGrecaptchaPlugin implements FlutterPlugin, ActivityAware, MethodCallHandler {
    /// This local reference serves to register the plugin with the Flutter Engine and unregister it
    /// when the Flutter Engine is detached from the Activity
    private MethodChannel channel;

    // activity required to call SafetyNet API
    private Activity activity;

    // default constructor required by Flutter
    FGrecaptchaPlugin() {
    }

    // this constructor required for static registerWith
    FGrecaptchaPlugin(Activity activity) {
        this.activity = activity;
    }

    // static registerWith required to support older v1 Android Embeddings
    public static void registerWith(Registrar registrar) {
        final MethodChannel channel = new MethodChannel(registrar.messenger(), "f_grecaptcha");
        channel.setMethodCallHandler(new FGrecaptchaPlugin(registrar.activity()));
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        channel = new MethodChannel(binding.getBinaryMessenger(), "f_grecaptcha");
        channel.setMethodCallHandler(this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
        channel.setMethodCallHandler(null);
    }

    @Override
    public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivityForConfigChanges() {
        activity = null;
    }

    @Override
    public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {
        activity = binding.getActivity();
    }

    @Override
    public void onDetachedFromActivity() {
        activity = null;
    }

    @Override
    public void onMethodCall(MethodCall call, final Result result) {
        if (call.method.equals("isSupported")) {
            result.success(true);
        } else if (call.method.equals("verify")) {
            if (activity == null) {
                result.error("f_grecaptcha", "No activity found, this plugin works with Activity only.", null);
                return;
            }
            String siteKey = call.argument("key");

            SafetyNet.getClient(activity).verifyWithRecaptcha(siteKey)
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
