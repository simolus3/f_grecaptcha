# f_grecaptcha

This simple [flutter](http://flutter.io/) plugin allows using the 
[SafetyNet API](https://developer.android.com/training/safetynet/recaptcha.html)
on Android devices to verify that the user is human.

## Using the plugin
### Preparations

First, include the plugin in your project's dependencies by including it in
the relevant section of your `pubspec.yaml`:
```yaml
dependencies:
  f_grecaptcha: ^1.0.0
```
Next, you wil need to register your app in the reCAPTCHA admin console. Go
to https://www.google.com/recaptcha/admin#list and register by filling out
the form. Be sure to select "reCAPTCHA-Android" as the type. A form field
asking for your Android package name will appear. You can copy it from the
manifest file located under `android/app/src/main/AndroidManifest.xml` in
your project directory. It is the value of the `package` attribute of the
root XML-tag. You can also visit the [instructions](https://developer.android.com/training/safetynet/recaptcha.html#register)
page from the android documentation for a more detailed guide, only the
section "Adding a SafetyNet API dependency" is relevant, the plugin will do
the rest.

### Verifying users

After having your app registered with the reCAPTCHA API, you can invoke the
following method anywhere in your dart code, most commonly after a button
has been pressed. Replace `SITE_KEY` with the site key the admin interface
shows after registering your app.
```dart
 FGrecaptcha.verifyWithRecaptcha(SITE_KEY).then((result) {
    // You can send the result token, along with some form fields, to your
    // server, which can verify the token using an endpoint proved by the
    // reCAPTCHA API for servers, see https://developers.google.com/recaptcha/docs/verify
    }, onError: (e, s) {
    // An error doesn't have to mean that the user is not a human. Errors
    // can also occur when the sitekey is invalid or does not match your
    // application, when the device is not supported or when a network
    // error occurs.
    // You should inform the user of errors, explaining why they can't
    // proceed. As the plugin is not available for iOS, you might consider
    // skipping the reCAPTCHA step when FGrecaptcha.isAvailable is false.
    print("Could not verify:\n $e at $s");
    }
);
```

### Server-side verification
Simply checking that `FGrecaptcha.verifyWithRecaptcha` returned a value is
not enough to be sure that the user is a human. Instead, you would have to
verify the token returned in your applications backend server. You can
accomplish that by following the instructions at https://developers.google.com/recaptcha/docs/verify.