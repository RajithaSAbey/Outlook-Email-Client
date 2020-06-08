package com.reesby.Config;

import com.microsoft.aad.msal4j.*;
import com.microsoft.graph.logger.DefaultLogger;
import com.microsoft.graph.logger.LoggerLevel;
import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.requests.extensions.GraphServiceClient;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.reesby.OutlookClientApplication;
import net.minidev.json.JSONObject;



import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MicrosoftConfig {

    private static final String CREDENTIALS_FILE_PATH = "/OutlookClient.properties";
    public static  String userCode;
    private static  String verificationUrl;
    private static String applicationID;
    private static IGraphServiceClient graphClient = null;
    private static SimpleAuthProvider authProvider = null;

    // Set authority to allow only organizational accounts
    // Device code flow only supports organizational accounts
    private final static String authority = "https://login.microsoftonline.com/common/";

    public static String getAccessToken() throws MalformedURLException {
        System.out.println("Java Graph Tutorial");
        System.out.println();

        // Load OAuth settings
        final Properties oAuthProperties = new Properties();
        try {
            oAuthProperties.load(OutlookClientApplication.class.getResourceAsStream(CREDENTIALS_FILE_PATH));

        } catch (IOException e) {
            System.out.println("Unable to read OAuth configuration. Make sure you have a properly formatted oAuth.properties file. See README for details.");
            //return;
        }

        final String appId = oAuthProperties.getProperty("app.id");

        final String[] appScopes = oAuthProperties.getProperty("app.scopes").split(",");


        // Get an access token
        initialize(appId);

        final String accessToken = getUserAccessToken(appScopes);
       // final String accessToken = getDeviceCode(appScopes);

        //System.out.println(Authentication.getDeviceCode());


        // Greet the user
        User user = getUser(accessToken);
        System.out.println("Welcome " + user.displayName);
        System.out.println();

        return userCode;
    }

    public static void initialize(String applicationId) {
        applicationID = applicationId;
    }

    public static String getUserAccessToken(String[] scopes) {
        if (applicationID == null) {
            System.out.println("You must initialize Authentication before calling getUserAccessToken");
            return null;
        }

        Set<String> scopeSet = Set.of(scopes);

        PublicClientApplication app;
        try {
            // Build the MSAL application object with
            // app ID and authority
            app = PublicClientApplication.builder(applicationID)
                    .authority(authority)
                    .build();
        } catch (MalformedURLException e) {
            return null;
        }

        // Create consumer to receive the DeviceCode object
        // This method gets executed during the flow and provides
        // the URL the user logs into and the device code to enter
        Consumer<DeviceCode> deviceCodeConsumer = (DeviceCode deviceCode) -> {
            // Print the login information to the console
            System.out.println(deviceCode.message());
            userCode=deviceCode.userCode();
            verificationUrl=deviceCode.verificationUri();



        };


        // Request a token, passing the requested permission scopes
        IAuthenticationResult result = app.acquireToken(
                DeviceCodeFlowParameters
                        .builder(scopeSet, deviceCodeConsumer)
                        .build()
        ).exceptionally(ex -> {
            System.out.println("Unable to authenticate - " + ex.getMessage());
            return null;
        }).join();


        if (result != null) {
            return result.accessToken();
        }

        return null;
    }
    public static String getDeviceCode(String[] scopes) throws IOException {
       /* String urlString = "https://graph.windows.net/mycompany.com/users?api-version=2013-04-05";

        URL url = new URL(urlString, tenant, accessToken));

        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        // Set the appropriate header fields in the request header.

        conn.setRequestProperty("api-version", "2013-04-05");

        conn.setRequestProperty("Authorization", accessToken);

        conn.setRequestProperty("Accept", "application/json;odata=minimalmetadata");

        String goodRespStr = HttpClientHelper.getResponseStringFromConn(conn, true);

        // logger.info("goodRespStr ->" + goodRespStr);

        int responseCode = conn.getResponseCode();

        JSONObject response = HttpClientHelper.processGoodRespStr(responseCode, goodRespStr);

        JSONArray users;

        users = JSONHelper.fetchDirectoryObjectJSONArray(response);*/
       return null;
    }

    private static void ensureGraphClient(String accessToken) {
        if (graphClient == null) {
            // Create the auth provider
            authProvider = new SimpleAuthProvider(accessToken);

            // Create default logger to only log errors
            DefaultLogger logger = new DefaultLogger();
            logger.setLoggingLevel(LoggerLevel.ERROR);

            // Build a Graph client
            graphClient = GraphServiceClient.builder()
                    .authenticationProvider(authProvider)
                    .logger(logger)
                    .buildClient();
        }
    }

    public static User getUser(String accessToken) {
        ensureGraphClient(accessToken);

        // GET /me to get authenticated user
        User me = graphClient
                .me()
                .buildRequest()
                .get();

        return me;
    }

   /* public static void sendEmail(String accessToken){

        // Create the auth provider
        authProvider = new SimpleAuthProvider(accessToken);
        Message message = new Message();
        message.subject = "Meet for lunch?";
        ItemBody body = new ItemBody();
        body.contentType = BodyType.TEXT;
        body.content = "The new cafeteria is open.";
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();
        emailAddress.address = "fannyd@contoso.onmicrosoft.com";
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        Recipient ccRecipients = new Recipient();
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.address = "danas@contoso.onmicrosoft.com";
        ccRecipients.emailAddress = emailAddress1;
        ccRecipientsList.add(ccRecipients);
        message.ccRecipients = ccRecipientsList;

        boolean saveToSentItems = true;

        graphClient.me()
                .sendMail(message,saveToSentItems)
                .buildRequest()
                .post();
    }*/

    public static void sendMail(Message emailMessage){

        boolean saveToSentItems = true;
        graphClient.me()
                .sendMail(emailMessage,saveToSentItems)
                .buildRequest()
                .post();
    }

    public static void sendWithAttachment(Message emailMessag, FileAttachment attachment){

    }

    //gets the default, top 10 messages
    public static IMessageCollectionPage getTopMessages(){
        IMessageCollectionPage messages = graphClient.me().messages()
                .buildRequest()
                .select("sender,subject")
                .get();
        return messages;
    }


}

