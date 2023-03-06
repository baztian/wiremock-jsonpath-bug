package com.example.wiremock;

import static com.github.tomakehurst.wiremock.client.WireMock.containing;
import static com.github.tomakehurst.wiremock.client.WireMock.matchingJsonPath;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;


@WireMockTest
public class AppTest {

  private String baseUrl;

  private HttpClient httpClient = HttpClient.newBuilder().build();

  @BeforeEach
  void setUp(WireMockRuntimeInfo wmRuntimeInfo) {
    WireMock wireMock = wmRuntimeInfo.getWireMock();
    baseUrl = wmRuntimeInfo.getHttpBaseUrl();
  }

  @ParameterizedTest
  @ValueSource(strings = {"{}", ""})
  void shouldAnswerWithTrue(String jsonBodyResponse) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .POST(HttpRequest.BodyPublishers.ofString(jsonBodyResponse))
        .uri(URI.create(baseUrl + "/example"))
        .build();
    httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    assertThrows(AssertionError.class, () ->
        verify(postRequestedFor(
            urlPathMatching("/example"))
            .withRequestBody(
                matchingJsonPath("$.foo", containing("test.pdf")))
        )
    );
  }
}
