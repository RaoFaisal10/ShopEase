package pk.edu.nfciet.shopease;

import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

/** Assignment 1 intentionally contains NO real product cards, cart or checkout. */
public class ProductListActivity extends AppCompatActivity {
    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppUi.lightBars(this);
        setContentView(R.layout.activity_product_list);
        if (!AppUi.isFirebaseReady(this)) {
            AppUi.goAndClear(this, LoginActivity.class); return;
        }
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null) {
            AppUi.goAndClear(this, LoginActivity.class); return;
        }
        TextView hello = findViewById(R.id.tvHello);
        FirebaseFirestore.getInstance().collection("users").document(user.getUid()).get()
                .addOnSuccessListener(this, document -> {
                    String name = document.getString("name");
                    if (name != null && !name.trim().isEmpty()) hello.setText("Hi, " + name.trim() + "!");
                })
                .addOnFailureListener(this, error -> {
                    // Network errors must not crash a signed-in user's screen.
                    AppUi.message(this, "Could not load your name. Try again later.");
                });
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            AppUi.goAndClear(this, LoginActivity.class);
        });
    }
}
