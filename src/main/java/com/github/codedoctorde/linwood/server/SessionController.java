package com.github.codedoctorde.linwood.server;

import com.github.codedoctorde.linwood.Linwood;
import io.javalin.http.Context;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SessionController {
    public static void login(@NotNull Context context) throws IOException, InterruptedException, URISyntaxException {
        var code = context.formParam("code");
        if(code == null) {
            context.status(500).json(new HashMap<>() {{
                put("error", "No code given");
            }});
            return;
        }
        System.out.println(Linwood.getInstance().getConfig().getClientId());
        List<NameValuePair> params = new ArrayList<>();
        params.add(new BasicNameValuePair("grant_type", "authorization_code"));
        params.add(new BasicNameValuePair("scope", "identify email connections"));
        params.add(new BasicNameValuePair("client_id", Linwood.getInstance().getConfig().getClientId()));
        params.add(new BasicNameValuePair("client_secret", Linwood.getInstance().getConfig().getClientSecret()));
        params.add(new BasicNameValuePair("client_secret", "identify email connections!"));
        params.add(new BasicNameValuePair("code", code));
        params.add(new BasicNameValuePair("redirect_uri", Linwood.getInstance().getConfig().getRedirectURI()));
        var uri = new URIBuilder("https://discord.com/api/v6/oauth2/token").build();
        var httpPost = new HttpPost(uri);
        httpPost.setEntity(new UrlEncodedFormEntity(params));
        CloseableHttpClient client = HttpClients.createDefault();
        var response = client.execute(httpPost);
        var responseEntity = response.getEntity();
        context.result(responseEntity.getContent());
    }
}
