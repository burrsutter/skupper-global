# transaction project

Performs some work and adds records to a Postgres database

Check application.properties for database connection


## Dev Mode

```
mvn quarkus:dev
```


## Production

```
mvn clean compile package
```

```
docker build -f src/main/docker/Dockerfile.jvm -t docker.io/burrsutter/transaction:1.0.0 .
```

```
docker push docker.io/burrsutter/transaction:1.0.0
```

```

```