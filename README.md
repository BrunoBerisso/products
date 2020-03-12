# Products
Spring Boot Products API

## Instructions

To run the API locally first we need to start the database. This test use MongoDB as DBMS so to start a light instance locally
run the following in the root of this repository:

```
$ docker-compose up -d
```

Once the MongoDB instanc is running we can start the API, to do so run the following command:
```
mvn spring-boot:run
```

When the boot process finish you can navigate to the Swagger UI to play with the endpoints and check the docs
here: http://localhost:8080/roche/swagger-ui.html

Thanks
