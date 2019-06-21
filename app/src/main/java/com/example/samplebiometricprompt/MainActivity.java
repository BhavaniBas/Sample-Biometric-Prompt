package com.example.samplebiometricprompt;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity implements BiometricCallback {

    private BiometricManager mBiometricManager;
    FragmentActivity fragmentActivity = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        findViewById(R.id.btn_fringerprint_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mBiometricManager = new BiometricManager.BiometricBuilder(MainActivity.this)
                        .setTitle(getString(R.string.biometric_title))
                        .setLayout(R.layout.custom_view_bottom_sheet)
                        .setSubtitle(getString(R.string.biometric_subtitle))
                        .setDescription(getString(R.string.biometric_description))
                        .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                        .build();

                //start authentication
                mBiometricManager.authenticate(MainActivity.this);
            }
        });

        findViewById(R.id.btn_fringerprint_login1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!BiometricUtils.isSdkVersionSupported()) {
                    Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
                } else if (!BiometricUtils.isHardwareSupported(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
                } else if (!BiometricUtils.isPermissionGranted(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
                } else if (!BiometricUtils.isFingerprintAvailable(MainActivity.this)) {
                    Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
                } else {
                    Executor executor = Executors.newSingleThreadExecutor();

                    final BiometricPrompt biometricPrompt = new BiometricPrompt(fragmentActivity, executor,
                            new BiometricPrompt.AuthenticationCallback() {
                                @Override
                                public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                                    super.onAuthenticationError(errorCode, errString);
                                    if (errorCode == BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                                        // user clicked negative button
                                    } else {
                                        //TODO: Called when an unrecoverable error has been encountered and the operation is complete.
                                    }
                                }

                                @Override
                                public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                                    super.onAuthenticationSucceeded(result);
                                    //TODO: Called when a biometric is recognized.
                                }

                                @Override
                                public void onAuthenticationFailed() {
                                    super.onAuthenticationFailed();
                                    //TODO: Called when a biometric is valid but not recognized.
                                }
                            });
                    final BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                            .setTitle(getString(R.string.biometric_title))
                            .setSubtitle(getString(R.string.biometric_subtitle))
                            .setDescription(getString(R.string.biometric_description))
                            .setNegativeButtonText(getString(R.string.biometric_negative_button_text))
                            .build();
                    biometricPrompt.authenticate(promptInfo);
                }
            }
        });
    }

    @Override
    public void onSdkVersionNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_sdk_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotSupported() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_hardware_not_supported), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationNotAvailable() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_fingerprint_not_available), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBiometricAuthenticationPermissionNotGranted() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_error_permission_not_granted), Toast.LENGTH_LONG).show();
    }


    @Override
    public void onAuthenticationFailed() {
//        Toast.makeText(getApplicationContext(), getString(R.string.biometric_failure), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationCancelled() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_cancelled), Toast.LENGTH_LONG).show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            mBiometricManager.cancelAuthentication();
        }
    }

    @Override
    public void onAuthenticationSuccessful() {
        Toast.makeText(getApplicationContext(), getString(R.string.biometric_success), Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
//        Toast.makeText(getApplicationContext(), helpString, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
//        Toast.makeText(getApplicationContext(), errString, Toast.LENGTH_LONG).show();
    }
}
