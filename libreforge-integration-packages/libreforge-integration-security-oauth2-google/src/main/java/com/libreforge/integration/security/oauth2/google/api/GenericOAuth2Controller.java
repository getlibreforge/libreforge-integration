package com.libreforge.integration.security.oauth2.google.api;

import com.libreforge.integration.common.ApplicationException;
import com.libreforge.integration.security.oauth2.google.api.dto.OAuth2IssueTokenRequest;
import com.libreforge.integration.security.oauth2.google.api.dto.OAuth2RefreshTokenRequest;
import com.libreforge.integration.security.oauth2.google.api.dto.OAuth2TokenResponse;
import com.libreforge.integration.security.oauth2.google.service.InMemoryRefreshTokenStorage;
import com.libreforge.integration.security.oauth2.google.utils.JwtParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@RestController
public class GenericOAuth2Controller {

    private static final Logger LOG = LoggerFactory.getLogger(GenericOAuth2Controller.class);
    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${security.oauth2.providers.google.endpoint.token}")
    public String tokenEndpoint;

    @Value("${security.oauth2.providers.google.clientSecret}")
    public String clientSecret;

    @Autowired
    private InMemoryRefreshTokenStorage refreshTokenStorage;

    @PostMapping(value = "/api/integration/security/oauth2/token", produces = "application/json")
    public ResponseEntity<OAuth2TokenResponse> issueToken(
            @Valid @RequestBody OAuth2IssueTokenRequest request
    ) throws Exception {

        String targetUrl = tokenEndpoint +
            "?code=" + URLDecoder.decode(request.getCode(), StandardCharsets.UTF_8) +
            "&client_id=" + request.getClientId() +
            "&client_secret=" + clientSecret +
            "&redirect_uri=" + request.getRedirectUri() +
            "&access_type=offline&grant_type=authorization_code";

        ResponseEntity<ProviderOAuth2TokenIssue> response =
                restTemplate.exchange(targetUrl,
                        HttpMethod.POST, null, ProviderOAuth2TokenIssue.class);

        if (response.getBody() == null) {
            throw new ApplicationException(response.getStatusCode().getReasonPhrase(), "error_cant_issue_token");
        }

        /* Parse ID token to get SUBJECT - as external user identifier */
        String sessionId = JwtParser.getSubClaim(response.getBody().getIdToken());
//        LOG.info("SessionId - {}, refresh token is {}", sessionId, response.getBody().getRefreshToken());

        if (response.getBody().getRefreshToken() != null) {
            refreshTokenStorage.put(sessionId, response.getBody().getRefreshToken());
        }

        return ResponseEntity.ok()
            .body(new OAuth2TokenResponse(response.getBody(), sessionId));
    }

    @PostMapping(value = "/api/integration/security/oauth2/refresh", produces = "application/json")
    public ResponseEntity<OAuth2TokenResponse> refreshToken(
            @Valid @RequestBody OAuth2RefreshTokenRequest request
    ) throws Exception {

        /* Get refresh token */
        String refreshToken = refreshTokenStorage.get(request.getSessionId());
        if (refreshToken == null) {
            throw new ApplicationException("Refresh token not found", "error_no_refresh_token");
        }

        String targetUrl = tokenEndpoint +
                "?refresh_token=" + refreshToken +
                "&client_id=" + request.getClientId() +
                "&client_secret=" + clientSecret +
                "&access_type=offline&grant_type=refresh_token";

        ResponseEntity<ProviderOAuth2TokenIssue> response =
                restTemplate.exchange(targetUrl,
                        HttpMethod.POST, null, ProviderOAuth2TokenIssue.class);

        if (response.getBody() == null) {
            throw new ApplicationException(response.getStatusCode().getReasonPhrase(), "error_cant_refresh_token");
        }

        return ResponseEntity.ok()
                .body(new OAuth2TokenResponse(response.getBody(), null));
    }
}
