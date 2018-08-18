[![Build Status](https://travis-ci.org/henriqamaral/campanha.svg?branch=master)](https://travis-ci.org/henriqamaral/campanha)

# Campanha Micro Services

**Simples cadastro de campanha usando micro serviços**

## Serviços

#### Time service
Simples CRUD para o cadastro do time

Metodo	| Path	| Descrição	
------------- | ------------------------- | ------------- |
GET	| /times/{id}	| Retorna o time pelo Id
GET	| /times	| Retorna todos os time
PUT	| /times/	| Altera o time
POST | /times/	| Registra novo time

#### Campanha service
Simples CRUD para a campanha

Metodo	| Path	| Descrição	
------------- | ------------------------- | ------------- |
GET	| /campanhas/{id}	| Retorna a campanha pelo Id
GET	| /campanhas	| Retorna todas as campanhas ativas pela data da vigencia
PUT	| /campanhas/	| Altera a campanha
POST | /campanhas/	| Registra nova campanha

#### Torcedor service
Serviço que controla as interaçoes do torcedor com campanha

Metodo	| Path	| Descrição	
------------- | ------------------------- | ------------- |
GET	| /torcedores/{id}	| Retorna o torcedor pelo Id
GET	| /torcedores	| Retorna todos os torcedores
GET	| /torcedores{id}/associar/{idCampanha}	| Associa o torcedor pela campanha
PUT	| /torcedores/	| Altera o torcedor
POST | /torcedores/	| Registra novo torcedor

#### Notes
- Cada microserviço tem o seu próprio banco de dados usando a tecnologia MONGODB
- A comunicação entre os microserviços está sendo utilizada apenas API REST

## Infrastructure services
Usando os frameworks disponibilizados via [Spring cloud](http://projects.spring.io/spring-cloud/) para a construção dos serviços.
<img width="880" alt="Infrastructure services" src="https://user-images.githubusercontent.com/6852760/27207678-2e513724-5217-11e7-8698-4b151b9895d4.png">

### API Gateway
Esta é uma única entrada para todo o sistema, usada para redirecionar todas requisições para cada microserviço.
Utilizando o framework ZUUL parte no [Netflix OSS](http://techblog.netflix.com/2013/06/announcing-zuul-edge-service-in-cloud.html), e com o Spring Cloud com uma simples annotation se pode a habilitar o Zuul `@EnableZuulProxy`. Aqui uma simples configuração para o serviço de Campanha:
```yml
zuul:
  routes:
    campanha-service:
        path: /campanhas/**
        serviceId: campanha-service
        stripPrefix: false
```

Isso significa que todas as chamadas iniciando `/campanhas` irão ser redirecionados para o serviço de Campanha.

### Serviço de descoberta
Outra parte da arquitetura é o serviço de descoberta. Estou usando o framework Netflix Eureka. Esse framework é um bom exemplo do client-side discovery pattern, onde o cliente é responsável em derterminar a sua localização.

Com Spring Cloud para habilitar o Eureka Registry apenas precisar adicionar a annotation `@EnableDiscoveryClient` e adicionar simples configurações ao projeto.

Exemplo do serviço discovery habilitado com `@EnableDiscoveryClient` annotation e com a configuração no arquivo `bootstrap.yml`:
``` yml
spring:
  application:
    name: campanha-service
```

### Load balancer, Circuit breaker and Http client

No Netflix OSS possui boas ferramentas para isso.

#### Ribbon
Ribbon é um client side load balancer.

Ao usar o Spring Cloud e o Serviço de Descoberta, o Ribbon já está integrado.

#### Hystrix
Hystrix é a implementação [Circuit Breaker pattern](http://martinfowler.com/bliki/CircuitBreaker.html), que controla a latencia  e falha de dependencias.

Com o Hystrix é possível adicionar metodos de fallback, caso falhe a chamada de algum serviço.

#### Feign
Feign é um cliente HTTP, que integra direto com Ribbon e Hystrix. Com a annotation `@EnableFeignClients`, já fica configurado Load Balancer, Circuit Breaker e HTTP Client.

Aqui um exemplo do Serviço de Campanha, com comando para Fallback:

``` java
@FeignClient(name = "time-service", fallback = TimeServiceClientImpl.class )
public interface TimeServiceClient {

    @RequestMapping(method = RequestMethod.GET, value = "/times/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    String findTime(@PathVariable("id") String id);

}
```

- Tudo que precisa é uma interface e implementar um classe com o metodo de fallback

### Monitor dashboard
Nas configurações do projetos, cada microserviço com Hystrix envia metricas para o  Turbine via Spring Cloud Bus (with AMQP broker). O projeto de monitoração é apenas um projeto usando Spring Boot com [Turbine](https://github.com/Netflix/Turbine) e [Hystrix Dashboard](https://github.com/Netflix/Hystrix/tree/master/hystrix-dashboard).

### RabbiMQ
Usando Serviço de Mensagens para enviar as mensagens dos serviços para o Monitor Dashboar e também utilizado para avisar possíveis serviços quando uma Campanha no Serviço de Campanha é alterada.
Segue o exemplo de chamada e configuração no Serviço de Campanha:
``` java
@EnableBinding(CampanhaMessage.class)
public class Application {
    public static void main(String[] args) {
		SpringApplication.run(Application.class, args);

	}
}
private void enviaCampanhasAlteradas(Collection<Campanha> campanhas) {
        
    campanhas.stream().forEach(c -> {
        Message<Campanha> m = MessageBuilder.withPayload(c).build();
        channel.output().send(m);
    });

}
```

## Como rodar?
Para rodar tudo você precisa de no mínimo `4 Gb` de RAM, uma vez que tem 8 aplicações Spring Boot, 4 instancias de MongoDB e RabbitMQ.

#### Antes de Começar
- Instalar Java 8
- Instalar Maven 3.3 ou acima
- Instlar Docker e Docker Compose

#### Compilar todos os projetos
```
./maven-all.sh
```
#### Rodar os testes
```
./maven-test-all.sh
```
#### Subir or projetos
```
docker-compose up -d
```

#### Endpoints Importantes
- http://localhost:80 - Gateway
- http://localhost:8761 - Eureka Dashboard
- http://localhost:9000/hystrix - Hystrix Dashboard (cole o link do turbine)
- http://localhost:8989 - Turbine stream (source para o  Hystrix Dashboard)
- http://localhost:15672 - RabbitMq  (login/password: guest/guest)