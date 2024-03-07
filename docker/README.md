# Containerized Development

The suggested method for development is to run a container with required JVM,
JDK, and Clojure tooling. To do this, build a custom docker image suitable
for eventflow engine REPL interaction.

## Dockerfile

The Dockerfile contains a minimal set of parameters required to begin
containerized engine development.

## Build
```
  docker \
    build \
      --rm \
      --force-rm \
      -t clojure-dev:0.0 \
    .
```

## Run

Start the configured containers:

```
  env \
    PROJECT_HOME=/path/to/cloned-repo \
    REPL_PORT=9876
  docker-compose up -d
```

In a new terminal, connect to the running container. This will allow
you to confirm the volume mapping is successful as well as starting the
Clojure REPL process.

```
  docker exec -it clj-test-containers-1 /bin/bash
```

Start the Clojure REPL process:

```
  clj -M:socket-repl
```

## Connect

```
  docker exec -it clj-test-containers-1 /bin/bash
  cd project && nvim
```
