package com.adropofliquid.magiclinkauth.email;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    //email sender service
    //Using resend.com API to send emails because it's convenient
    //the variables were intentionally hardcoded
    //They do not have a java library
    //I am using Unirest to perform a request on the api
    //
    public void sendMagicLink(String email, String link) {

        try {
            // Create a JSON object with the request body
            JSONObject json = new JSONObject();
            json.put("from", "onboarding@resend.dev");
            json.put("to", email);
            json.put("subject", "Login with magic link");
            json.put("html", "<div>magic login link: " + link + "</div>");

            // Make a POST request to the resend endpoint
            HttpResponse<JsonNode> response = Unirest.post("https://api.resend.com/emails")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer re_LR6k3AFT_7xoVbHN8x8ZZv8eig67UTptV") // add this line
                    .body(json)
                    .asJson();

            // Check the status code and the response body
            int status = response.getStatus();
            JsonNode body = response.getBody();

            //print logs in console//need better log management
            System.out.println("Status: " + status);
            System.out.println("Body: " + body);

        } catch (Exception e) {
            //I should handle email sending errors
            //and perform appropriate actions based on the errors
            //but........
            e.printStackTrace();
        }
    }

}
