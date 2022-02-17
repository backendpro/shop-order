# Spring Boot + RabbitMQ

## Objetivo
- API de criação de pedidos de compra que tem seus status atualizados;

## Tecnologias
- Este projeto é feito com Java 11, Spring Boot, RabbitMQ no Docker;

## Informações Importantes
- Porta: 8080 <br/>
- API: /v1 nas opções Post e Get<br/>

## Fluxo
- Após pedido ser criado, uma mensage é postada na rabbit onde é consumida e tem seu status trocado;

## Observações
- Utilizar o arquivo docker-compose.yml pra subir um container com a Rabbit;

