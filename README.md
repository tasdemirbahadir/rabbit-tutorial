# Rabbit Tutorial

This app shows how to configure Spring to use RabbitMQ for only message consuming.
Also a rest template configuration is present inside the integration test.
The integration test uses a test rabbit container.
Awaitility used to synchronize the message calls on the test.

## Setup And Run

- To run rabbit-tutorial at your local environment, please run the commands below;

```docker run -d -p 5671-5672:5671-5672 -p 15671-15672:15671-15672 --name rabbitmq rabbitmq:3-management```