import 'package:flutter/material.dart';
import 'package:f_grecaptcha/f_grecaptcha.dart';

void main() => runApp(new MyApp());

// Outside of this example app, you need to provide your own site key. You can
// generate them on https://www.google.com/recaptcha/admin#list by selecting
// reCAPTCHA Android. The readme of this plugin contains a more detailed
// explanation.
const String SITE_KEY = "6LdE10UUAAAAAD5Mw7XeDU2VUgMchgAI_qk3sos8";

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => new _MyAppState();
}

enum _VerificationStep {
  SHOWING_BUTTON, WORKING, ERROR, VERIFIED
}

class _MyAppState extends State<MyApp> {

  // Start by showing the button inviting the user to use the example
  _VerificationStep _step = _VerificationStep.SHOWING_BUTTON;

  void _startVerification() {
    setState(() => _step = _VerificationStep.WORKING);

    FGrecaptcha.verifyWithRecaptcha(SITE_KEY).then((result) {
      /* When using reCaptcha in a production app, you would now send the $result
         to your backend server, so that it can verify it as well. In most
         cases, an ideal way to do this is sending it together with some form
         fields, for instance when creating a new account. Your backend server
         would then take the result field and make a request to the reCaptcha
         API to verify that the user of the device where the registration
         request is from is a human. It could then continue processing the
         request and complete the registration. */
      setState(() => _step = _VerificationStep.VERIFIED);
    }, onError: (e, s) {
      print("Could not verify:\n$e at $s");
      setState(() => _step = _VerificationStep.ERROR);
    });
  }

  @override
  Widget build(BuildContext context) {
    Widget content;

    switch (_step) {
      case _VerificationStep.SHOWING_BUTTON:
        content = new Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            new Text("This example will use the reCaptcha API to verify that you're human"),
            new RaisedButton(
              onPressed: _startVerification,
              child: const Text("VERIFY"),
            )
          ]
        );
        break;
      case _VerificationStep.WORKING:
        content = new Column(
          mainAxisSize: MainAxisSize.min,
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            new CircularProgressIndicator(),
            new Text("Trying to figure out whether you're human"),
          ]
        );
        break;
      case _VerificationStep.VERIFIED:
        content = new Text(
          "The reCaptcha API returned a token, indicating that you're a human. "
          "In real world use case, you would send use the token returned to "
          "your backend-server so that it can verify it as well."
        );
        break;
      case _VerificationStep.ERROR:
        content = new Text(
          "We could not verify that you're a human :( This can occur if you "
          "have no internet connection (or if you really are a a bot)."
        );
    }

    return new MaterialApp(
      home: new Scaffold(
        appBar: new AppBar(
          title: new Text('reCaptcha example'),
        ),
        body: new Center(
          child: content,
        ),
      ),
    );
  }
}
