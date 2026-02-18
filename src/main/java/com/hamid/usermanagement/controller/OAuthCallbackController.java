// src/main/java/com/hamid/usermanagement/controller/OAuthCallbackController.java
package com.hamid.usermanagement.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OAuthCallbackController {

    @GetMapping("/oauth/callback")
    public String callback(
            @RequestParam(required = false) String code,
            @RequestParam(required = false) String error,
            @RequestParam(required = false) String error_description) {

        if (error != null) {
            return "<html><body>" +
                    "<h1>❌ Errore OAuth</h1>" +
                    "<p><strong>Error:</strong> " + error + "</p>" +
                    "<p><strong>Description:</strong> " + error_description + "</p>" +
                    "</body></html>";
        }

        return "<html><body>" +
                "<h1>✅ Authorization Code Ricevuto!</h1>" +
                "<h2>Copia questo code:</h2>" +
                "<textarea rows='3' cols='100' style='font-size: 16px;'>" + code + "</textarea>" +
                "<hr>" +
                "<h3>Prossimo Step - Esegui questo comando:</h3>" +
                "<pre style='background: #f4f4f4; padding: 15px;'>" +
                "curl -X POST \"https://idpgw.test4mind.com/realms/demointerview/protocol/openid-connect/token\" ^\n" +
                "  -H \"Content-Type: application/x-www-form-urlencoded\" ^\n" +
                "  -d \"grant_type=authorization_code\" ^\n" +
                "  -d \"client_id=demo-task\" ^\n" +
                "  -d \"client_secret=8PDH3fflpbzJyx2rAy39SPB60OuSjeX6\" ^\n" +
                "  -d \"code=" + code + "\" ^\n" +
                "  -d \"redirect_uri=http://localhost:8080/oauth/callback\"" +
                "</pre>" +
                "</body></html>";
    }
}