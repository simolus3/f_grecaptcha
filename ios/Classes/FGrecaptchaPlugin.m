#import "FGrecaptchaPlugin.h"
#import <f_grecaptcha/f_grecaptcha-Swift.h>

@implementation FGrecaptchaPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFGrecaptchaPlugin registerWithRegistrar:registrar];
}
@end
