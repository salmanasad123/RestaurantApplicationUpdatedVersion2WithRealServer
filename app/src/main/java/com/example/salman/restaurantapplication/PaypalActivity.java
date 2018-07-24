package com.example.salman.restaurantapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

import java.math.BigDecimal;

public class PaypalActivity extends AppCompatActivity {

    private static final String TAG = "MTAG";


    String n_paypalClienresponse = "AWoCw3OK8yxN79ybPTTkUMY79YuGS2pHcHf7T7FKrzeBeAsEVCcyGYl1X3KvcgErk6p0fEkmLLxzw_Z3";
    com.paypal.android.sdk.payments.PayPalConfiguration payPalConfiguration;
    Intent servicel;
    int paypalrequesrcode = 999;

    double dollarrate = 121.60;

    double cartTotalFromCartActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal);

        cartTotalFromCartActivity = getIntent().getDoubleExtra("cartTotalAmount", 0.00);
        Log.d(TAG, "onCreate: " + cartTotalFromCartActivity);


        payPalConfiguration = new com.paypal.android.sdk.payments.PayPalConfiguration()
                .environment(com.paypal.android.sdk.payments.PayPalConfiguration.ENVIRONMENT_SANDBOX)
                .clientId(n_paypalClienresponse);
        servicel = new Intent(this, PayPalService.class);
        servicel.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        startService(servicel);
        PayPalPayment payment = new PayPalPayment(new BigDecimal(cartTotalFromCartActivity / dollarrate), "USD", "GrocesStore Recived payment from : ", PayPalPayment.PAYMENT_INTENT_SALE);
        Intent intent = new Intent(PaypalActivity.this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, payPalConfiguration);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);
        startActivityForResult(intent, paypalrequesrcode);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == paypalrequesrcode) {
            if (resultCode == RESULT_OK) {
               /* DropInResult result= data.getParcelableExtra(DropInResult.EXTRA_DROP_IN_RESULT);
                PaymentMethodNonce nonce = result.getPaymentMethodNonce();
                String strnone = nonce.getNonce();
*/
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null) {
                    String state = confirmation.getProofOfPayment().getState();
                    if (state.equals("approved")) {


                        Toast.makeText(this, "Transaction Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PaypalActivity.this, PlaceOrderThankYouActivity.class);
                        startActivity(intent);

                    } else {


                    }

                } else {

                }

            } else if (resultCode == RESULT_CANCELED) {


            } else {


            }
        }

    }
}

