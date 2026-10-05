package ws.ai.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.restclient.RestClientCustomizer;
import org.springframework.boot.restclient.autoconfigure.RestClientAutoConfiguration;
import org.springframework.boot.webclient.WebClientCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.reactive.function.client.*;
import reactor.core.publisher.Mono;

import java.io.IOException;

/**
 * @author WindShadow
 * @version 2026-10-04
 */

@Configuration(proxyBeanMethods = false)
@AutoConfigureBefore(RestClientAutoConfiguration.class)
public class RestClientConfiguration {

    @Bean
    public static RestClientCustomizer logRestClientCustomizer() {

        return builder -> builder.requestInterceptor(new WebRequestLogger());
    }

    @Bean
    public static WebClientCustomizer logWebClientCustomizer() {

        return builder -> builder.filter(new WebRequestLogger());
    }

    @Slf4j
    private static class WebRequestLogger implements ClientHttpRequestInterceptor, ExchangeFilterFunction {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

            log.info("URI: {}", request.getURI());
            return execution.execute(request, body);
        }


        @Override
        public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {

            log.info("URL: {}", request.url());
            return next.exchange(request);
        }
    }
}
