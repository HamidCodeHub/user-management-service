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
            return String.format("""
                <html>
                <head>
                    <title>OAuth Error</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 50px; }
                        .error { color: red; }
                    </style>
                </head>
                <body>
                    <h1 class="error">❌ OAuth Error</h1>
                    <p><strong>Error:</strong> %s</p>
                    <p><strong>Description:</strong> %s</p>
                </body>
                </html>
                """, error, error_description);
        }

        if (code == null || code.isEmpty()) {
            return """
                <html>
                <head>
                    <title>OAuth Error</title>
                    <style>
                        body { font-family: Arial, sans-serif; margin: 50px; }
                        .error { color: red; }
                    </style>
                </head>
                <body>
                    <h1 class="error">❌ Error</h1>
                    <p>No authorization code received</p>
                </body>
                </html>
                """;
        }

        String curlCommand = String.format(
                "curl -X POST \"https://idpgw.test4mind.com/realms/demo-interview/protocol/openid-connect/token\" ^\n" +
                        "  -H \"Content-Type: application/x-www-form-urlencoded\" ^\n" +
                        "  -d \"grant_type=authorization_code\" ^\n" +
                        "  -d \"client_id=demo-task\" ^\n" +
                        "  -d \"client_secret=8PDH3fflpbzJyx2rAy39SPB60OuSjeX6\" ^\n" +
                        "  -d \"code=%s\" ^\n" +
                        "  -d \"redirect_uri=http://localhost:8080/oauth/callback\"",
                code
        );

        return String.format("""
            <html>
            <head>
                <title>Authorization Code Received</title>
                <style>
                    body { font-family: Arial, sans-serif; margin: 50px; }
                    .success { color: green; }
                    pre { background: #f4f4f4; padding: 15px; border-radius: 5px; overflow-x: auto; }
                    textarea { width: 100%%; height: 80px; font-family: monospace; padding: 10px; }
                    button { background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; font-size: 16px; }
                    button:hover { background: #0056b3; }
                </style>
                <script>
                    function copyCode() {
                        const code = document.getElementById('code');
                        code.select();
                        document.execCommand('copy');
                        alert('Code copied to clipboard!');
                    }
                    function copyCurl() {
                        const curl = document.getElementById('curl');
                        curl.select();
                        document.execCommand('copy');
                        alert('cURL command copied to clipboard!');
                    }
                </script>
            </head>
            <body>
                <h1 class="success">✅ Authorization Code Received!</h1>
                
                <h2>Step 1: Copy the Authorization Code</h2>
                <textarea id="code" readonly onclick="this.select()">%s</textarea>
                <button onclick="copyCode()">📋 Copy Code</button>
                
                <h2>Step 2: Exchange Code for Token</h2>
                <p>Copy and run this command in PowerShell or CMD:</p>
                <textarea id="curl" readonly onclick="this.select()">%s</textarea>
                <button onclick="copyCurl()">📋 Copy cURL Command</button>
                
                <h2>Step 3: Use the Access Token</h2>
                <ol>
                    <li>Run the cURL command above</li>
                    <li>Copy the <code>access_token</code> from the response</li>
                    <li>Go to <a href="http://localhost:8080/swagger-ui/index.html" target="_blank">Swagger UI</a></li>
                    <li>Click "Authorize" and paste the token</li>
                    <li>Test the API endpoints!</li>
                </ol>
                
                <p><em>Note: The authorization code expires in a few minutes. Use it quickly!</em></p>
            </body>
            </html>
            """, code, curlCommand);
    }
}
