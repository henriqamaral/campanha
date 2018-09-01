package br.com.time.api.gateways.http;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.nest;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class TeamRouter {

  @Bean
  public RouterFunction<ServerResponse> routerFunction(TeamHandler handler) {
    return nest(
        path("/teams"),
        nest(
                accept(APPLICATION_JSON),
                route(GET("/{id}"), handler::get).andRoute(GET(""), handler::getAll))
            .andRoute(POST("/").and(contentType(APPLICATION_JSON)), handler::post));
  }
}
