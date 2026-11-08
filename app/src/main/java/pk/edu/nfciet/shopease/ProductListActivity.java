package pk.edu.nfciet.shopease;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
public class ProductListActivity extends AppCompatActivity {
  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    AppUi.lightBars(this);
    setContentView(R.layout.activity_product_list);
  }
}
