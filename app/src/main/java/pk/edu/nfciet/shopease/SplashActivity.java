package pk.edu.nfciet.shopease;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/** First screen: 1.5 seconds, check Firebase session, then discard splash from back stack. */
public class SplashActivity extends AppCompatActivity {
    private final Handler handler = new Handler(Looper.getMainLooper());
    private final Runnable navigate = () -> {
        if (isFinishing() || isDestroyed()) return;
        FirebaseUser user = AppUi.isFirebaseReady(this)
                ? FirebaseAuth.getInstance().getCurrentUser() : null;
        Class<?> next = user != null ? ProductListActivity.class : LoginActivity.class;
        startActivity(new Intent(this, next));
        finish();
    };

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUi.darkBars(this);
        setContentView(R.layout.activity_splash);
        handler.postDelayed(navigate, 1500);
    }

    @Override protected void onDestroy() {
        handler.removeCallbacks(navigate);
        super.onDestroy();
    }
}
