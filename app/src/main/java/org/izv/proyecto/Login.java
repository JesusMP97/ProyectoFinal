package org.izv.proyecto;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.izv.circularfloatingbutton.FloatingActionButton;
import org.izv.circularfloatingbutton.FloatingActionMenu;
import org.izv.circularfloatingbutton.SubActionButton;
import org.izv.proyecto.model.data.Empleado;
import org.izv.proyecto.view.delay.AfterDelay;
import org.izv.proyecto.view.model.LoginViewModel;
import org.izv.proyecto.view.model.MainViewModel;
import org.izv.proyecto.view.utils.IO;

import java.util.List;

public class Login extends AppCompatActivity {
    public static final String FILE_LOGIN = "login";
    public static final String KEY_LOGIN_ID = "id";
    private static final long FIELD_VISIBILITY_DELAY = 100;
    private static final String FILE_SETTINGS = "org.izv.proyecto_preferences";
    private static final String KEY_DEFAULT_VALUE = "0";
    private static final int KEY_LOGIN_INTENT = 0;
    private static final String KEY_URL = "url";
    private static final long POST_ANIM_ALPHA_DELAY = 1500;
    private static final String SAVED_PASSWORD = "savedPassword";
    private static final String SAVED_USER_NAME = "savedUserName";
    private Button btLogin;
    private SubActionButton btSettings;
    private Empleado current = new Empleado();
    private int currentPasswordSize;
    private List<Empleado> employees;
    private TextInputEditText etUserName, etPassword;
    private FloatingActionButton fab;
    private TextInputLayout ilUserName, ilPassword;
    private Animation initApp;
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
            viewModel.getLiveEmployeeList().observe(this, new Observer<List<Empleado>>() {
                @Override
                public void onChanged(List<Empleado> empleados) {
                    employees = empleados;
                }
            });
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getApplication().setTheme(R.style.Theme_MaterialComponents_NoActionBar);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initComponents()
                .initFabComponents()
                .setSavedInstanceValues(savedInstanceState)
                .initAnimations()
                .showComponents()
                .assignEvents();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(SAVED_USER_NAME, etUserName.getText().toString());
        outState.putString(SAVED_PASSWORD, etPassword.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private Login adjustUserNameValues(CharSequence s) {
        if (etPassword.hasFocus() && s.length() == 0) {
            tvUserName.setVisibility(View.INVISIBLE);
            etUserName.setHint(getString(R.string.ilUserName));
        }
        return this;
    }

    private Login assignEvents() {
        btSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SettingsActivity.class);
                startActivity(intent);
            }
        });
        viewModel.getLiveEmployeeList().observe(this, new Observer<List<Empleado>>() {
            @Override
            public void onChanged(List<Empleado> empleados) {
                employees = empleados;
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
        return this;
    }

    private Login findAdecuateError(TextInputEditText et, TextInputLayout il, String userError, String passwordError) {
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

    private Login findAdecuateHint(TextInputEditText currentEt, String userHint, String passwordHint) {
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

    private Login hideKeyboard(TextInputEditText current) {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(current.getWindowToken(), 0);
        return this;
    }

    private Login initAnimations() {
        btLogin.startAnimation(initApp);
        ilUserName.startAnimation(initApp);
        ilPassword.startAnimation(initApp);
        tvLogin.startAnimation(initApp);
        tvUserName.startAnimation(initApp);
        tvPassword.startAnimation(initApp);
        //btLogin.setForeground(getDrawable(R.drawable.ripple));
        return this;
    }

    private Login initComponents() {
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
        url = IO.readPreferences(this, FILE_SETTINGS, KEY_URL, KEY_DEFAULT_VALUE);
        return this;
    }

    private Login initFabComponents() {
        ImageView ivFab = new ImageView(this);
        ivFab.setImageDrawable(getDrawable(R.drawable.ic_add_black_36dp));
        ImageView ivSettings = new ImageView(this);
        ivSettings.setImageDrawable(getDrawable(R.drawable.ic_settings_black_24dp));
        fab = new FloatingActionButton.Builder(this)
                .setContentView(ivFab)
                .build();
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        btSettings = itemBuilder.setContentView(ivSettings).build();
        new FloatingActionMenu.Builder(this)
                .addSubActionView(btSettings)
                .attachTo(fab)
                .build();
        return this;
    }

    private Login manageCredentials() {
        if (hasCredentials()) {
            IO.savePreferences(this, FILE_LOGIN, KEY_LOGIN_ID, String.valueOf(current.getId()));
            Toast.makeText(this, getString(R.string.welcome) + " " + current.getLogin(), Toast.LENGTH_SHORT).show();
            startActivity();
        } else {
            if (employees != null) {
                setErrorValues(etUserName, ilUserName, tvUserName, current.getLogin())
                        .setErrorValues(etPassword, ilPassword, tvPassword, current.getClave());
            } else {
                Toast.makeText(this, getString(R.string.conexionError), Toast.LENGTH_SHORT).show();
                ilPassword.setError(null);
                ilUserName.setError(null);
            }
        }
        return this;
    }

    private Login manageFocus(final TextInputEditText currentEt, final TextView currentTv, boolean hasFocus) {
        if (hasFocus) {
            setFocusedValues(currentEt, currentTv);
        } else {
            setUnfocusedValues(currentEt, currentTv);
        }
        return this;
    }

    private Login managePasswordSize(CharSequence s) {
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

    private Login postShowComponentsDelay() {
        if (!etUserName.hasFocus()) {
            etUserName.setHint(getString(R.string.ilUserName));
        }
        if (!etPassword.hasFocus()) {
            etPassword.setHint(getString(R.string.ilPassword));
        }
        return this;
    }

    private Login preShowComponentsDelay() {
        manageFocus(etUserName, tvUserName, etUserName.hasFocus());
        manageFocus(etPassword, tvPassword, etPassword.hasFocus());
        setEmptyHint(etUserName)
                .setEmptyHint(etPassword);
        return this;
    }

    private Login quitError(TextInputLayout il, CharSequence s) {
        if (s.length() >= 0)
            il.setError(null);
        return this;
    }

    private Login setEmptyHint(EditText currentEt) {
        if (currentEt.getText().toString().isEmpty() && !currentEt.hasFocus()) {
            currentEt.setHint("");
        }
        return this;
    }

    private Login setErrorValues(TextInputEditText et, TextInputLayout il, TextView tv, String credential) {
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

    private Login setFocusedValues(final TextInputEditText currentEt, final TextView currentTv) {
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

    private Login setSavedInstanceValues(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            String savedUserName = savedInstanceState.getString(SAVED_USER_NAME);
            String savedPassword = savedInstanceState.getString(SAVED_PASSWORD);
            etUserName.setText(savedUserName);
            etPassword.setText(savedPassword);
        }
        return this;
    }

    private Login setUnfocusedValues(TextInputEditText currentEt, TextView currentTv) {
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

    private Login showComponents() {
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

    private Login startActivity() {
        startActivityForResult(new Intent(Login.this, MainActivity.class), KEY_LOGIN_INTENT);
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
