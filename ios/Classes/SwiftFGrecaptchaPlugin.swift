import Flutter
import UIKit
    
public class SwiftFGrecaptchaPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "f_grecaptcha", binaryMessenger: registrar.messenger())
    let instance = SwiftFGrecaptchaPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    result(false)
  }
}
