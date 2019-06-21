# Sample-Biometric-Prompt

1. As of API level 28, FingerprintManager was deprecated and BiometricPrompt was introduced. It brings standard experience for fingerprint authentication and potentially less bugs when you implement it from scratch. However, minimum supported version of 'android.hardware.biometrics.BiometricPrompt' is Android P.

### Does it mean developers still have to implement FingerprintManager for users who have Android devices running versions below Android P?
The answer is no.

2. Google released first alpha version of androidx.biometric library which allows developers to use only 'androidx.biometrics.BiometricsPrompt'and support all devices without writing boilerplate code.

Now, I am using a two ways,

1.FingerPrintManagerCompact with Biometric Prompt Authentication Callback API ('android.hardware.biometrics.BiometricPrompt' that one is deprecated),custom Dialog.
2.Biometric Prompt Authentication Callback API (BiometricPrompt.PromptInfo),Default Dialog.

### Here are the steps to implement BiometricPrompt Compat:

### 1. Add androidx.biometric dependency to app level build.gradle file:
dependencies {
    // ...
    implementation 'androidx.biometric:biometric:1.0.0-alpha03'
}
### 2. Create BiometricPrompt instance:

Before writing the code, let’s see the responsibility of BiometricPrompt class from documentation:
A class that manages a system-provided biometric prompt. On devices running P and above, this will show a system-provided authentication prompt, using a device’s supported biometric (fingerprint, iris, face, etc). On devices before P, this will show a dialog prompting for fingerprint authentication. The prompt will persist across orientation changes unless explicitly canceled by the client. For security reasons, the prompt will automatically dismiss when the activity is no longer in the foreground.
As you can see, on devices running Android P and above, BiometricPrompt is not only limited to fingerprint authentication, which is great!
### BiometricPrompt class has only one public constructor which accepts following parameters:

public BiometricPrompt (FragmentActivity fragmentActivity, 
                Executor executor, 
                BiometricPrompt.AuthenticationCallback callback)
As a required parameter, we need executor to handle callback events. So, let’s create one:
val executor = Executors.newSingleThreadExecutor()
Next, we can create instance of BiometricPrompt class:
val biometricPrompt = BiometricPrompt(activity, executor, object : BiometricPrompt.AuthenticationCallback() {

    override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
        super.onAuthenticationError(errorCode, errString)
        TODO("Called when an unrecoverable error has been encountered and the operation is complete.")
    }

    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
        super.onAuthenticationSucceeded(result)
        TODO("Called when a biometric is recognized.")
    }

    override fun onAuthenticationFailed() {
        super.onAuthenticationFailed()
        TODO("Called when a biometric is valid but not recognized.")
    }
})
### 3. Create BiometricPrompt.PromptInfo instance:

When we call biometricPrompt.authenticate() method, we need to pass instance of BiometricPrompt.PromptInfo. We can create instance of BiometricPrompt.PromptInfo using BiometricPrompt.PromptInfo.Builder.
val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Set the title to display.")
        .setSubtitle("Set the subtitle to display.")
        .setDescription("Set the description to display")
        .setNegativeButtonText("Negative Button")
        .build()
Subtitle and description are optional parameters, so, you can skip those parameters. You might have question what’s the purpose of negative button text?
According to documentation, text for the negative button would typically be used as a “Cancel” button, but may be also used to show an alternative method for authentication, such as screen that asks for a backup password.
To detect if user clicked negative button, you can handle proper error code on onAuthenticationError() method of AuthenticationCallback:
override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
    super.onAuthenticationError(errorCode, errString)
    //...
    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
        // user clicked negative button
    }
}
You can find more about error codes from here.
### 4. Start authentication
As a last step, you can call authenticate() method of BiometricPrompt instance and pass BiometricPrompt.PromptInfo instance we built in previous step:
biometricPrompt.authenticate(promptInfo)
If you want to cancel the authentication, you can call:
biometricPrompt.cancelAuthentication().
