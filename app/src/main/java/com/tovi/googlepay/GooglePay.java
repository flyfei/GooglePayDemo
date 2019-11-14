//package com.tovi.googlepay;
//
//import android.app.Activity;
//import android.util.Log;
//
//import com.android.billingclient.api.BillingClient;
//import com.android.billingclient.api.BillingClientStateListener;
//import com.android.billingclient.api.BillingFlowParams;
//import com.android.billingclient.api.BillingResult;
//import com.android.billingclient.api.Purchase;
//import com.android.billingclient.api.PurchasesUpdatedListener;
//import com.android.billingclient.api.SkuDetails;
//import com.android.billingclient.api.SkuDetailsParams;
//import com.android.billingclient.api.SkuDetailsResponseListener;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import androidx.annotation.Nullable;
//
///**
// * @author <a href='mailto:zhaotengfei9@gmail.com'>Tengfei Zhao</a>
// * @date 2019-11-14
// */
//public class GooglePay implements PurchasesUpdatedListener {
//    private static final String TAG = GooglePay.class.getSimpleName();
//    private static GooglePay googlePay;
//
//    private BillingClient mBillingClient;
//
//
//    public static GooglePay getInstance(Activity activity) {
//        if (googlePay == null) {
//            synchronized (GooglePay.class) {
//                if (googlePay == null) {
//                    googlePay = new GooglePay(activity);
//                }
//            }
//        }
//        return googlePay;
//    }
//
//    private GooglePay(Activity activity) {
//        mBillingClient = BillingClient.newBuilder(activity).setListener(this).build();
//        mBillingClient.startConnection(new BillingClientStateListener() {
//            @Override
//            public void onBillingSetupFinished(BillingResult billingResult) {
//                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
//                    // The BillingClient is ready. You can query purchases here.
//                }
//            }
//
//            @Override
//            public void onBillingServiceDisconnected() {
//
//            }
//        });
//    }
//
//
//    @Override
//    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
//        Log.e(TAG, "onPurchasesUpdated code = " + billingResult.getResponseCode() + " ,  msg = " + billingResult.getDebugMessage());
//        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK
//                && purchases != null) {
//            if (mListener != null) {
//                mListener.onSuccess();
//            }
//            for (Purchase purchase : purchases) {
//                handlePurchase(purchase, true);
//            }
//        } else if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.USER_CANCELED) {
//            // Handle an error caused by a user cancelling the purchase flow.
//            if (mListener != null) {
//                mListener.onCancel();
//            }
//        } else {
//            // Handle any other error codes.
//            if (mListener != null) {
//                mListener.onError();
//            }
//        }
//    }
//
//    private void queryPurchases(final String purchaseId) {
//        if (!isClientInit()) {
//            if (mListener != null) {
//                mListener.onError();
//            }
//            return;
//        }
//        List<String> skuList = new ArrayList<>();
//        skuList.add(purchaseId);
//        skuList.add("xxx");  // 这个参数不能为空，值随便传
//        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
//        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
//        mBillingClient.querySkuDetailsAsync(params.build(),
//                new SkuDetailsResponseListener() {
//                    @Override
//                    public void onSkuDetailsResponse(BillingResult billingResult,
//                                                     List<SkuDetails> skuDetailsList) {
//                        Log.e(TAG, "onSkuDetailsResponse code = " + billingResult.getResponseCode() + " ,  msg = " + billingResult.getDebugMessage() + " , skuDetailsList = " + skuDetailsList);
//                        // Process the result.
//                        if (skuDetailsList == null || skuDetailsList.isEmpty()) {
//                            if (mListener != null) {
//                                mListener.onError();
//                            }
//
//                            return;
//                        }
//                        SkuDetails skuDetails = null;
//                        for (SkuDetails details : skuDetailsList) {
//                            Log.e(TAG, "onSkuDetailsResponse skuDetails = " + details.toString());
//                            if (purchaseId.equals(details.getSku())) {
//                                skuDetails = details;
//                            }
//                        }
//                        if (skuDetails != null) {
//                            pay(skuDetails);
//                        } else {
//                            if (mListener != null) {
//                                mListener.onError();
//                            }
//                        }
//                    }
//                });
//    }
//
//    private void pay(SkuDetails skuDetails) {
//        BillingFlowParams flowParams = BillingFlowParams.newBuilder()
//                .setSkuDetails(skuDetails)
//                .build();
//        int code = mBillingClient.launchBillingFlow(mActivity, flowParams).getResponseCode();
//    }
//
//}
