package com.mertensapi.mertensapi;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import mu.prevoir.sdk.APIContext;
import mu.prevoir.sdk.APIMethodType;
import mu.prevoir.sdk.APIRequest;
import mu.prevoir.sdk.APIResponse;

import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
public class Controller {

    @GetMapping("/welcome")
    public String getHome() {
        return "Welcome Mertens";
    }

    @CrossOrigin
    @GetMapping("/payment")
    public String setPayment(@RequestParam("senderMSISDN") String senderMSISDN,
            @RequestParam("receiverMSISDN") String receiverMSISDN, @RequestParam("amount") String amount) throws InterruptedException {

        // Public key on the API listener used to encrypt keys
        String publicKey = "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEArv9yxA69XQKBo24BaF/D+fvlqmGdYjqLQ5WtNBb5tquqGvAvG3WMFETVUSow/LizQalxj2ElMVrUmzu5mGGkxK08bWEXF7a1DEvtVJs6nppIlFJc2SnrU14AOrIrB28ogm58JjAl5BOQawOXD5dfSk7MaAA82pVHoIqEu0FxA8BOKU+RGTihRU+ptw1j4bsAJYiPbSX6i71gfPvwHPYamM0bfI4CmlsUUR3KvCG24rB6FNPcRBhM3jDuv8ae2kC33w9hEq8qNB55uw51vK7hyXoAa+U7IqP1y6nBdlN25gkxEA8yrsl1678cspeXr+3ciRyqoRgj9RD/ONbJhhxFvt1cLBh+qwK2eqISfBb06eRnNeC71oBokDm3zyCnkOtMDGl7IvnMfZfEPFCfg5QgJVk1msPpRvQxmEsrX9MQRyFVzgy2CWNIb7c+jPapyrNwoUbANlN8adU1m6yOuoX7F49x+OjiG2se0EJ6nafeKUXw/+hiJZvELUYgzKUtMAZVTNZfT8jjb58j8GVtuS+6TM2AutbejaCV84ZK58E2CRJqhmjQibEUO6KPdD7oTlEkFy52Y1uOOBXgYpqMzufNPmfdqqqSM4dU70PO8ogyKGiLAIxCetMjjm6FCMEA3Kc8K0Ig7/XtFm9By6VxTJK1Mg36TlHaZKP6VzVLXMtesJECAwEAAQ==";
        // String publicKey =
        // "MIICIjANBgkqhkiG9w0BAQEFAAOCAg8AMIICCgKCAgEAietPTdEyyoV/wvxRjS5pSn3ZBQH9hnVtQC9SFLgM9IkomEX9Vu9fBg2MzWSSqkQlaYIGFGH3d69Q5NOWkRo+Y8p5a61sc9hZ+ItAiEL9KIbZzhnMwi12jUYCTff0bVTsTGSNUePQ2V42sToOIKCeBpUtwWKhhW3CSpK7S1iJhS9H22/BT/pk21Jd8btwMLUHfVD95iXbHNM8u6vFaYuHczx966T7gpa9RGGXRtiOr3ScJq1515tzOSOsHTPHLTun59nxxJiEjKoI4Lb9h6IlauvcGAQHp5q6/2XmxuqZdGzh39uLac8tMSmY3vC3fiHYC3iMyTb7eXqATIhDUOf9mOSbgZMS19iiVZvz8igDl950IMcelJwcj0qCLoufLE5y8ud5WIw47OCVkD7tcAEPmVWlCQ744SIM5afw+Jg50T1SEtu3q3GiL0UQ6KTLDyDEt5BL9HWXAIXsjFdPDpX1jtxZavVQV+Jd7FXhuPQuDbh12liTROREdzatYWRnrhzeOJ5Se9xeXLvYSj8DmAI4iFf2cVtWCzj/02uK4+iIGXlX7lHP1W+tycLS7Pe2RdtC2+oz5RSSqb5jI4+3iEY/vZjSMBVk69pCDzZy4ZE8LBgyEvSabJ/cddwWmShcRS+21XvGQ1uXYLv0FCTEHHobCfmn2y8bJBb/Hct53BaojWUCAwEAAQ==";

        // Create Context with API to request a Session ID
        APIContext context = APIContext.builder()
                .apiKey("qPqhim1UhGpTIuslduITTcYLX0wPgEnH")
                .publicKey(publicKey)
                .ssl(true)
                // Method type (can be GET/POST/PUT)
                .apiMethodType(APIMethodType.GET)
                .address("openapi.m-pesa.com")
                .port(443)
                .path("/sandbox/ipg/v2/vodacomDRC/getSession/")
                // .path("/openapi/ipg/v2/vodacomDRC/getSession/")
                .build();

        // Create a request object
        APIRequest request = new APIRequest(context);

        // Add/update headers
        context.addHeader("Origin", "*");

        // Do the API call and put result in a response packet
        APIResponse response = null;

        try {
            response = request.execute();
        } catch (Exception e) {
            System.out.println("Call failed: " + e.getMessage());
        }

        // Display results
        if (response != null) {
            System.out.println(response.getStatusCode() + " - " + response.getReason());
            System.out.println(response.getResult());

            for (Map.Entry<String, String> entry : response.getBody().entrySet()) {
                System.out.println(entry.getKey() + ":" + response.getBody().get(entry.getKey()));
            }
        } else {
            // throw new Exception("SessionKey call failed to get result. Please check.");
            return "SessionKey call failed to get result. Please check.";
        }

        // The above call issued a sessionID will be used as the API key in calls that
        // needs the sessionID
        APIContext contextC2B = APIContext.builder()
                .apiKey(response.getBody().get("output_SessionID"))
                .publicKey(publicKey)
                .ssl(true)
                .apiMethodType(APIMethodType.POST)
                .address("openapi.m-pesa.com")
                .port(443)
                .path("/sandbox/ipg/v2/vodacomDRC/c2bPayment/singleStage/") // For C2B Requests
                .build(); 

        APIContext contextB2B = APIContext.builder()
                .apiKey(response.getBody().get("output_SessionID"))
                .publicKey(publicKey)
                .ssl(true)
                .apiMethodType(APIMethodType.POST)
                .address("openapi.m-pesa.com")
                .port(443)
                .path("/sandbox/ipg/v2/vodacomDRC/b2bPayment/") // For B2B Requests
                .build();

        /* For C2B Requests */
        contextC2B.addParameter("input_Amount", amount);
        contextC2B.addParameter("input_Country", "DRC");
        contextC2B.addParameter("input_Currency", "USD");
        contextC2B.addParameter("input_CustomerMSISDN", senderMSISDN);
        contextC2B.addParameter("input_ServiceProviderCode", "000000");
        contextC2B.addParameter("input_ThirdPartyConversationID", "prs02e5958774f7ba228d83d0d689520");
        contextC2B.addParameter("input_TransactionReference", "S123pj5A");
        contextC2B.addParameter("input_PurchasedItemsDesc", "Clothes"); 

        contextB2B.addParameter("input_Amount", amount);
        contextB2B.addParameter("input_Country", "DRC");
        contextB2B.addParameter("input_Currency", "USD");
        contextB2B.addParameter("input_ReceiverPartyCode", receiverMSISDN);
        contextB2B.addParameter("input_PrimaryPartyCode", "000000");
        contextB2B.addParameter("input_ThirdPartyConversationID", "asv02e5958774f7ba228d83d0d689761");
        contextB2B.addParameter("input_TransactionReference", "T1234C");
        contextB2B.addParameter("input_PurchasedItemsDesc", "Shoes");

        APIRequest requestC2B = new APIRequest(contextC2B);
        APIRequest requestB2B = new APIRequest(contextB2B);

        contextC2B.addHeader("Origin", "*");
        contextB2B.addHeader("Origin", "*");

        // SessionID can take up to 30 seconds to become 'live' in the system and will
        // be invalid until it is
        Thread.sleep(TimeUnit.SECONDS.toMillis(30));

        APIResponse responseC2B = null;
        APIResponse responseB2B = null;

        try {
            responseC2B = requestC2B.execute();
            responseB2B = requestB2B.execute();
        } catch (Exception e) {
            System.out.println("Call failed: " + e.getMessage());
        }

        System.out.println("responseC2B : " + responseC2B);
        System.out.println("responseB2B : " + responseB2B);

        if (responseC2B != null && responseB2B != null) {
            System.out.println("C2B : " + responseC2B.getStatusCode() + " - " + responseC2B.getReason());
            System.out.println("C2B : " + responseC2B.getResult()); 

            System.out.println("B2B : " + responseB2B.getStatusCode() + " - " + responseB2B.getReason());
            System.out.println("B2B : " + responseB2B.getResult());

            // needs the sessionID
            /*context = APIContext.builder()
                    .apiKey(response.getBody().get("output_SessionID"))
                    .publicKey(publicKey)
                    .ssl(true)
                    .apiMethodType(APIMethodType.POST)
                    .address("openapi.m-pesa.com")
                    .port(443)
                    .path("/sandbox/ipg/v2/vodacomDRC/b2bPayment/") // For B2B Requests
                    .build();*/

            /* For B2B Requests */
            /*context.addParameter("input_Amount", amount);
            context.addParameter("input_Country", "DRC");
            context.addParameter("input_Currency", "USD");
            context.addParameter("input_ReceiverPartyCode", receiverMSISDN);
            context.addParameter("input_PrimaryPartyCode", "000000");
            context.addParameter("input_ThirdPartyConversationID", "asv02e5958774f7ba228d83d0d689761");
            context.addParameter("input_TransactionReference", "T1234C");
            context.addParameter("input_PurchasedItemsDesc", "Shoes");

            request = new APIRequest(context);

            context.addHeader("Origin", "*");

            response = null;
            try {
                System.out.println("Try check");
                response = request.execute();
            } catch (Exception e) {
                System.out.println("Call failed: " + e.getMessage());
            }

            if (response != null) {
                System.out.println(response.getStatusCode() + " - " + response.getReason());
                System.out.println(response.getResult());

                for (Map.Entry<String, String> entry : response.getBody().entrySet()) {
                    return entry.getKey() + ":" + response.getBody().get(entry.getKey());
                }
            } else {
                // throw new Exception("API call failed to get result. Please check.");
                return "API call failed to get result. Please check.";
            } */
        
        } else {
            // throw new Exception("API call failed to get result. Please check.");
            return "API call failed to get result. Please check.";
        }

        return response.getResult();
    }

}
