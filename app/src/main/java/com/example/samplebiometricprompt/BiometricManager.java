package com.example.samplebiometricprompt;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;

import androidx.annotation.NonNull;

class BiometricManager extends BiometricManagerV23 {


    private CancellationSignal mCancellationSignal = new CancellationSignal();

    private BiometricManager(final BiometricBuilder biometricBuilder) {
        this.context = biometricBuilder.context;
        this.title = biometricBuilder.title;
        this.subtitle = biometricBuilder.subtitle;
        this.description = biometricBuilder.description;
        this.negativeButtonText = biometricBuilder.negativeButtonText;
    }


    void authenticate(@NonNull final BiometricCallback biometricCallback) {


        if(!BiometricUtils.isSdkVersionSupported()) {
            biometricCallback.onSdkVersionNotSupported();
            return;
        }

        if(!BiometricUtils.isPermissionGranted(context)) {
            biometricCallback.onBiometricAuthenticationPermissionNotGranted();
            return;
        }

        if(!BiometricUtils.isHardwareSupported(context)) {
            biometricCallback.onBiometricAuthenticationNotSupported();
            return;
        }

        if(!BiometricUtils.isFingerprintAvailable(context)) {
            biometricCallback.onBiometricAuthenticationNotAvailable();
            return;
        }

        displayBiometricDialog(biometricCallback);
    }

    void cancelAuthentication(){
        if(BiometricUtils.isBiometricPromptEnabled()) {
            if (!mCancellationSignal.isCanceled())
                mCancellationSignal.cancel();
        }else{
            if (!mCancellationSignalV23.isCanceled())
                mCancellationSignalV23.cancel();
        }
    }



    private void displayBiometricDialog(BiometricCallback biometricCallback) {
        if(BiometricUtils.isBiometricPromptEnabled()) {
            displayBiometricPrompt(biometricCallback);
        } else {
            displayBiometricPromptV23(biometricCallback);
        }
    }



    @TargetApi(Build.VERSION_CODES.P)
    private void displayBiometricPrompt(final BiometricCallback biometricCallback) {
        new BiometricPrompt.Builder(context)
                .setTitle(title)
                .setSubtitle(subtitle)
                .setDescription(description)
                .setNegativeButton(negativeButtonText, context.getMainExecutor(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        biometricCallback.onAuthenticationCancelled();
                    }
                })
                .build()
                .authenticate(mCancellationSignal, context.getMainExecutor(),
                        new BiometricCallbackV28(biometricCallback));
    }


    public static class BiometricBuilder {

        private String title;
        private String subtitle;
        private String description;
        private String negativeButtonText;
        private Integer layout;

        private Context context;
        public BiometricBuilder(Context context) {
            this.context = context;
        }

        public BiometricBuilder setTitle(@NonNull final String title) {
            this.title = title;
            return this;
        }

        public BiometricBuilder setSubtitle(@NonNull final String subtitle) {
            this.subtitle = subtitle;
            return this;
        }

        public BiometricBuilder setDescription(@NonNull final String description) {
            this.description = description;
            return this;
        }


        public BiometricBuilder setNegativeButtonText(@NonNull final String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
            return this;
        }

        public BiometricManager build() {
            return new BiometricManager(this);
        }

        public BiometricBuilder setLayout(@NonNull final Integer layout) {
            this.layout = layout;
            return this;
        }

    }
}