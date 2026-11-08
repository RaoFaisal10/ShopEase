package pk.edu.nfciet.shopease;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.Toast;
import com.google.firebase.FirebaseApp;

/** Tiny shared utilities: intentionally simple enough for an assignment viva. */
public final class AppUi {
    private AppUi() { }

    public static void lightBars(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.rgb(250, 248, 244));
        activity.getWindow().setNavigationBarColor(Color.rgb(250, 248, 244));
        activity.getWindow().getDecorView().setSystemUiVisibility(
                android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR |
                android.view.View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
    }

    public static void darkBars(Activity activity) {
        activity.getWindow().setStatusBarColor(Color.rgb(20, 34, 61));
        activity.getWindow().setNavigationBarColor(Color.rgb(20, 34, 61));
        activity.getWindow().getDecorView().setSystemUiVisibility(0);
    }

    /** Null when google-services.json is not configured; never fake a successful login. */
    public static boolean isFirebaseReady(Context context) {
        return FirebaseApp.initializeApp(context) != null;
    }

    public static void goAndClear(Activity from, Class<?> destination) {
        Intent intent = new Intent(from, destination);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        from.startActivity(intent);
        from.finish();
    }

    public static void message(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
