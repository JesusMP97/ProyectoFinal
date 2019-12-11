package org.izv.proyecto;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.model.repository.Repository;
import org.izv.proyecto.view.delay.AfterDelay;
import org.izv.proyecto.view.model.LoginViewModel;
import org.izv.proyecto.view.splash.OnSplash;
import org.izv.proyecto.view.splash.Splash;
import org.izv.proyecto.view.utils.IO;
import org.izv.proyecto.view.utils.Settings;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private static final long FIELD_VISIBILITY_DELAY = 100;
    private static final String FILE_LOGIN = "login";
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final String KEY_DEFAULT_VALUE = "informatica.ieszaidinvergeles.org:8043";
    private static final String KEY_LOGIN_ID = "id";
    private static final int KEY_LOGIN_INTENT = 0;
    private static final String KEY_URL = "url";
    private static final long POST_ANIM_ALPHA_DELAY = 1500;
    private static final String SAVED_PASSWORD = "savedPassword";
    private static final String SAVED_USER_NAME = "savedUserName";
    private Button btLogin;
    private Empleado current = new Empleado();
    private int currentPasswordSize;
    private List<Empleado> employees;
    private TextInputEditText etUserName, etPassword;
    private TextInputLayout ilUserName, ilPassword;
    private Animation initApp;
    private ImageView ivLoading;
    private TypedArray loadingBg;
    private AlertDialog loadingDialog;
    private Splash splash;
    private TextView tvUserName, tvPassword, tvLogin;
    private String url;
    private LoginViewModel viewModel;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == KEY_LOGIN_INTENT) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        String url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        if (!this.url.equalsIgnoreCase(url)) {
            viewModel.setUrl(url);
            initLoadingAlertDialogComponents();
            viewModel.getAll().observe(this, new Observer<List<Empleado>>() {
                @Override
                public void onChanged(List<Empleado> empleados) {
                    splash.setLoading(false);
                    employees = empleados;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents()
                .setSavedInstanceValues(savedInstanceState)
                .assignEvents()
                .initLoadingAlertDialogComponents();
    }

    @Override
    protected void onStop() {
        splash.setLoading(false);
        super.onStop();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(SAVED_USER_NAME, etUserName.getText().toString());
        outState.putString(SAVED_PASSWORD, etPassword.getText().toString());
    }

    private LoginActivity adjustUserNameValues(CharSequence s) {
        if (etPassword.hasFocus() && s.length() == 0) {
            tvUserName.setVisibility(View.INVISIBLE);
            etUserName.setHint(getString(R.string.ilUserName));
        }
        return this;
    }

    private LoginActivity afterSplash() {
        if (!this.isDestroyed()) {
            loadingDialog.cancel();
            initAnimations()
                    .showComponents();
        }
        return this;
    }

    private LoginActivity assignEvents() {
        viewModel.getAll().observe(this, new Observer<List<Empleado>>() {
            @Override
            public void onChanged(List<Empleado> empleados) {
                splash.setLoading(false);
                employees = empleados;
            }
        });
        viewModel.setOnFailureListener(new Repository.OnFailureListener() {
            @Override
            public void onConnectionFailure() {
                splash.setLoading(false);
                employees = null;
                showConexionError();
            }
        });
        etUserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                manageFocus(etUserName, tvUserName, hasFocus);
            }
        });
        etPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                manageFocus(etPassword, tvPassword, hasFocus);
            }
        });
        etUserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                quitError(ilUserName, s)
                        .adjustUserNameValues(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        etPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                managePasswordSize(s);
            }

            @Override
            public void afterTextChanged(Editable s) {
                currentPasswordSize = s.length();
            }
        });

        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manageCredentials();
            }
        });

        btLogin.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
        return this;
    }

    private LoginActivity findAdecuateError(TextInputEditText et, TextInputLayout il, String userError, String passwordError) {
        switch (et.getId()) {
            case R.id.etUserName:
                il.setError(userError);
                break;
            case R.id.etPassword:
                il.setError(passwordError);
                break;
        }
        return this;
    }

    private LoginActivity findAdecuateHint(TextInputEditText currentEt, String userHint, String passwordHint) {
        switch (currentEt.getId()) {
            case R.id.etUserName:
                currentEt.setHint(userHint);
                break;
            case R.id.etPassword:
                currentEt.setHint(passwordHint);
                break;
        }
        return this;
    }

    private boolean hasConexion() {
        boolean conexion = false;
        if (employees != null && !employees.isEmpty()) {
            conexion = true;
        }
        return conexion;
    }

    private boolean hasCredentials() {
        boolean value = false;
        if (employees != null) {
            for (Empleado current : employees) {
                if (etUserName.getText().toString().equals(current.getLogin()) && etPassword.getText().toString().equals(current.getClave())) {
                    this.current = current;
                    value = true;
                }
            }
        }
        return value;
    }

    private boolean hasIp() {
        boolean ip = false;
        if (!url.isEmpty()) {
            ip = true;
        }
        return ip;
    }

    private LoginActivity initAnimations() {
        btLogin.startAnimation(initApp);
        ilUserName.startAnimation(initApp);
        ilPassword.startAnimation(initApp);
        tvLogin.startAnimation(initApp);
        tvUserName.startAnimation(initApp);
        tvPassword.startAnimation(initApp);
        return this;
    }

    private LoginActivity initComponents() {
        url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        tvUserName = findViewById(R.id.tvUserName);
        etUserName = findViewById(R.id.etUserName);
        ilUserName = findViewById(R.id.ilUserName);
        tvPassword = findViewById(R.id.tvPassword);
        etPassword = findViewById(R.id.etPassword);
        ilPassword = findViewById(R.id.ilPassword);
        btLogin = findViewById(R.id.btLogin);
        tvLogin = findViewById(R.id.tvLogin);
        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        initApp = AnimationUtils.loadAnimation(this, R.anim.anim_alpha);
        return this;
    }

    private LoginActivity initLoadingAlertDialogComponents() {
        AlertDialog.Builder dialogBuilder = new androidx.appcompat.app.AlertDialog.Builder(this, R.style.loadingDialog);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.charging, null);
        dialogBuilder.setView(dialogView);
        loadingDialog = dialogBuilder.create();
        loadingDialog.show();
        loadingDialog.setCancelable(false);
        loadingBg = getResources().obtainTypedArray(R.array.loading_background);
        ivLoading = loadingDialog.findViewById(R.id.ivCharging);
        splash = new Splash(loadingBg, ivLoading, loadingDialog, new OnSplash() {
            @Override
            public void onFinished() {
                afterSplash();
            }
        });
        splash.execute();
        return this;
    }

    private LoginActivity manageCredentials() {
        if (hasIp()) {
            if (hasConexion()) {
                if (hasCredentials()) {
                    IO.savePreferences(this, FILE_LOGIN, KEY_LOGIN_ID, String.valueOf(current.getId()));
                    Toast.makeText(this, getString(R.string.welcome) + " " + current.getLogin(), Toast.LENGTH_SHORT).show();
                    startActivity();
                } else {
                    setErrorValues(etUserName, ilUserName, tvUserName, current.getLogin())
                            .setErrorValues(etPassword, ilPassword, tvPassword, current.getClave());
                }
            } else {
                etPassword.setError(null);
                etUserName.setError(null);
                showConexionError();
            }
        } else {
            splash.setLoading(false);
        }
        return this;
    }

    private LoginActivity manageFocus(final TextInputEditText currentEt, final TextView currentTv, boolean hasFocus) {
        if (hasFocus) {
            setFocusedValues(currentEt, currentTv);
        } else {
            setUnfocusedValues(currentEt, currentTv);
        }
        return this;
    }

    private LoginActivity managePasswordSize(CharSequence s) {
        if (s.length() >= ilPassword.getCounterMaxLength()) {
            String password = "";
            int maxPasswordSize = s.length() - 1;
            for (int i = 0; i < maxPasswordSize; i++) {
                password += s.charAt(i);
            }
            etPassword.setText(password);
            etPassword.setSelection(s.length() - 1);
        }
        if (currentPasswordSize != s.length()) {
            quitError(ilPassword, s);
        }
        return this;
    }

    private LoginActivity postShowComponentsDelay() {
        if (!etUserName.hasFocus()) {
            etUserName.setHint(getString(R.string.ilUserName));
        }
        if (!etPassword.hasFocus()) {
            etPassword.setHint(getString(R.string.ilPassword));
        }
        return this;
    }

    private LoginActivity preShowComponentsDelay() {
        manageFocus(etUserName, tvUserName, etUserName.hasFocus());
        manageFocus(etPassword, tvPassword, etPassword.hasFocus());
        setEmptyHint(etUserName)
                .setEmptyHint(etPassword);
        return this;
    }

    private LoginActivity quitError(TextInputLayout il, CharSequence s) {
        if (s.length() >= 0)
            il.setError(null);
        return this;
    }

    private LoginActivity setEmptyHint(EditText currentEt) {
        if (currentEt.getText().toString().isEmpty() && !currentEt.hasFocus()) {
            currentEt.setHint("");
        }
        return this;
    }

    private LoginActivity setErrorValues(TextInputEditText et, TextInputLayout il, TextView tv, String credential) {
        if (et.getText().toString().isEmpty()) {
            findAdecuateError(et, il, getString(R.string.etEmptyUserNameError), getString(R.string.etEmptyPasswordError));
        }
        if (!et.getText().toString().equals(credential) && !et.getText().toString().isEmpty()) {
            findAdecuateError(et, il, getString(R.string.etInvalidUserNameError), getString(R.string.etInvalidPasswordError));
            et.setHint("");
            tv.setVisibility(View.VISIBLE);
        }
        return this;
    }

    private LoginActivity setFocusedValues(final TextInputEditText currentEt, final TextView currentTv) {
        currentEt.setBackground(getDrawable(R.drawable.et_focused_background));
        currentTv.setTextColor(ContextCompat.getColor(this, R.color.white));
        AfterDelay afterDelay = new AfterDelay() {
            @Override
            public void doIt() {
                currentTv.post(new Runnable() {
                    @Override
                    public void run() {
                        currentTv.setVisibility(View.VISIBLE);
                        currentEt.setHint("");
                    }
                });
            }
        };
        Delay delay = new Delay(FIELD_VISIBILITY_DELAY, afterDelay);
        delay.start();
        return this;
    }

    private LoginActivity setSavedInstanceValues(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String savedUserName = savedInstanceState.getString(SAVED_USER_NAME);
            String savedPassword = savedInstanceState.getString(SAVED_PASSWORD);
            etUserName.setText(savedUserName);
            etPassword.setText(savedPassword);
        }
        return this;
    }

    private LoginActivity setUnfocusedValues(TextInputEditText currentEt, TextView currentTv) {
        currentEt.setBackground(getDrawable(R.drawable.et_background));
        if (!currentEt.getText().toString().isEmpty()) {
            currentEt.setHint("");
            currentTv.setVisibility(View.VISIBLE);
        } else {
            findAdecuateHint(currentEt, getString(R.string.ilUserName), getString(R.string.ilPassword));
            currentEt.setHintTextColor(getResources().getColor(R.color.grey));
            currentTv.setVisibility(View.INVISIBLE);
        }
        currentTv.setTextColor(ContextCompat.getColor(this, R.color.grey));
        return this;
    }

    public LoginActivity showComponents() {
        preShowComponentsDelay();
        AfterDelay afterDelay = new AfterDelay() {
            @Override
            public void doIt() {
                etPassword.post(new Runnable() {
                    @Override
                    public void run() {
                        postShowComponentsDelay();
                    }
                });
            }
        };
        Delay delay = new Delay(POST_ANIM_ALPHA_DELAY, afterDelay);
        delay.start();
        return this;
    }

    private LoginActivity showConexionError() {
        Toast.makeText(this, getString(R.string.conexionError), Toast.LENGTH_SHORT).show();
        return this;
    }

    private LoginActivity startActivity() {
        startActivityForResult(new Intent(LoginActivity.this, MainActivity.class), KEY_LOGIN_INTENT);
        return this;
    }

    public class Delay extends Thread {
        private volatile AfterDelay afterDelay;
        private volatile long delay;

        public Delay(long delay, AfterDelay afterDelay) {
            this.delay = delay;
            this.afterDelay = afterDelay;
        }

        @Override
        public synchronized void run() {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            afterDelay.doIt();
        }
    }
}
