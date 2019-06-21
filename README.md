# Sample-Biometric-Prompt

1. As of API level 28, FingerprintManager was deprecated and BiometricPrompt was introduced. It brings standard experience for fingerprint authentication and potentially less bugs when you implement it from scratch. However, minimum supported version of android.hardware.biometrics.BiometricPrompt is Android P.

### Does it mean developers still have to implement FingerprintManager for users who have Android devices running versions below Android P?
The answer is no.

2. Google released first alpha version of androidx.biometric library which allows developers to use only ### androidx.biometrics.BiometricsPrompt### and support all devices without writing boilerplate code.

Now, I am using a two ways,

### 1.FingerPrintManagerCompact Deprected with Biometric Prompt Authentication Callback API.
### 2. 
