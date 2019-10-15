Axon and Zeebe
===

Proof-of-concept integration of [Zeebe](https://docs.zeebe.io/introduction/quickstart.html) workflow
engine from Camunda with [Axon](https://axoniq.io/) CQRS engine.

Running
---

Build with `mvn package`.
Run `docker-compose build`.
Run `docker-compose up` and wait until the workflow is deployed.

- http://localhost:8080/views/workflows should have the deployed workflow in the Zeebe's simple monitor
- http://localhost:8081/swagger-ui.html should have Swagger API
- http://localhost:8081/h2-console should have access to H2 database

Copyright disclaimer
---

Some code was copied/modified from these (open) sources:

- https://github.com/gushakov/axon-simple
- https://github.com/zeebe-io/spring-zeebe/blob/master/examples/worker
- https://github.com/zeebe-io/zeebe-simple-monitor/
- https://zeebe.io/blog/2018/08/bpmn-microservices-orchestration-part-2-graphical-models/
