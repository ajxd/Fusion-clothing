package com.example.ajai_kamaraj_application2;

import android.app.Application;
import com.stripe.android.PaymentConfiguration;

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        PaymentConfiguration.init(
                getApplicationContext(),
                "pk_test_51Nb96uINWXfLdDEdEiIrOoN0awlgKICINAFJiczSdoWBst3gqpZP06kpKmccg69WgDZoTZLhvMwyAaUHvl1QSjcA00kM0GhHF8"
        );
    }
}
