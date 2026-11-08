package pk.edu.nfciet.shopease;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private TextInputEditText nameField, emailField, passwordField, confirmField;
    private TextInputLayout nameLayout, emailLayout, passwordLayout, confirmLayout;
    private MaterialButton signUpButton;
    private ProgressBar progress;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUi.lightBars(this);
        setContentView(R.layout.activity_sign_up);
        nameField = findViewById(R.id.etName);
        emailField = findViewById(R.id.etEmail);
        passwordField = findViewById(R.id.etPassword);
        confirmField = findViewById(R.id.etConfirm);
        nameLayout = findViewById(R.id.tilName);
        emailLayout = findViewById(R.id.tilEmail);
        passwordLayout = findViewById(R.id.tilPassword);
        confirmLayout = findViewById(R.id.tilConfirm);
        signUpButton = findViewById(R.id.btnSignUp);
        progress = findViewById(R.id.progress);
        signUpButton.setOnClickListener(v -> signUp());
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
        findViewById(R.id.tvGoToLogin).setOnClickListener(v -> finish());
    }
    private String entered(TextInputEditText field) {
        return field.getText() == null ? "" : field.getText().toString().trim();
    }
    private void signUp() {
        String name = entered(nameField);
        String email = entered(emailField);
        String password = passwordField.getText() == null ? "" : passwordField.getText().toString();
        String confirm = confirmField.getText() == null ? "" : confirmField.getText().toString();
        nameLayout.setError(null);emailLayout.setError(null);
        passwordLayout.setError(null);confirmLayout.setError(null);
        if(name.isEmpty()) { nameLayout.setError(getString(R.string.err_name));nameField.requestFocus();return; }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailLayout.setError(getString(R.string.err_email));emailField.requestFocus();return;
        }
        if(password.length() < 6) {
            passwordLayout.setError(getString(R.string.err_password));passwordField.requestFocus();return;
        }
        if(!password.equals(confirm)) {
            confirmLayout.setError(getString(R.string.err_match));confirmField.requestFocus();return;
        }
        if(!AppUi.isFirebaseReady(this)) {
            AppUi.message(this, getString(R.string.config_missing));return;
        }
        setLoading(true);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener(this, result -> {
                    if(result.getUser() != null) saveProfile(result.getUser().getUid(), name, email);
                    else showError(new IllegalStateException("No user returned by Firebase."));
                })
                .addOnFailureListener(this, this::showError);
    }
    private void saveProfile(String uid,String name,String email) {
        Map<String,Object> profile = new HashMap<>();
        profile.put("name",name);
        profile.put("email",email);
        profile.put("createdAt",FieldValue.serverTimestamp());
        FirebaseFirestore.getInstance().collection("users").document(uid).set(profile)
                .addOnSuccessListener(this, unused -> AppUi.goAndClear(this,ProductListActivity.class))
                .addOnFailureListener(this, error -> {
                    // Do not navigate to Products until the required profile was saved.
                    showError(error);
                });
    }
    private void showError(Exception error) {
        setLoading(false);
        AppUi.message(this, error.getLocalizedMessage() != null
                ? error.getLocalizedMessage() : "Could not create account. Please try again.");
    }
    private void setLoading(boolean loading) {
        signUpButton.setEnabled(!loading);
        progress.setVisibility(loading ? View.VISIBLE : View.GONE);
    }
}
