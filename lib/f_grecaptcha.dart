import 'dart:async';

import 'package:flutter/services.dart';

/// Provides an API to access the SafetyNet API for Android, which allows
/// verifying that the user is a human.
class FGrecaptcha {
    static const MethodChannel _channel = const MethodChannel('f_grecaptcha');

    static bool _checkedAvailability = false;
    static bool _isAvailable = false;

    /// Returns true when the API is available, which should be the case if the
    /// app is running on Android. The plugin will throw an exception when used
    /// on platforms where it's not available.
    static Future<bool> get isAvailable async {
        if (_checkedAvailability) {
            return _isAvailable;
        }

        var awailable = await _channel.invokeMethod("isSupported");
        _isAvailable = awailable;
        _checkedAvailability = true;

        return _isAvailable;
    }

    /// Verifies that the user is human and returns a token that your backend
    /// server can verify by making a call to the [reCaptcha API](https://developers.google.com/recaptcha/docs/verify).
    static Future<String> verifyWithRecaptcha(String siteKey) async {
        if (!await isAvailable) {
            throw new ReCaptchaNotAvailableException();
        }

        return await _channel.invokeMethod("verify", {"key": siteKey});
    }
}

class ReCaptchaNotAvailableException implements Exception {
    const ReCaptchaNotAvailableException();
}