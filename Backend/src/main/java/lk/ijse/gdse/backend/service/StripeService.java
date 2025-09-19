package lk.ijse.gdse.backend.service;

import com.stripe.Stripe;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StripeService {

    public StripeService(@Value("${stripe.api.key}") String stripeSecretKey) {
        Stripe.apiKey = stripeSecretKey; // set Stripe secret key
    }

    // Now you can create payment intents or checkout sessions securely
}
