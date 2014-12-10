## Authentication
A simple JAX-RS example that uses [Picketlink](http://picketlink.org) to demo HTTP Basic/Digest authentication. The demo was built to support the ```Authentication``` demo found in AeroGear [iOS](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Authentication) and [Android](https://github.com/aerogear/aerogear-android-cookbook/tree/master/src/org/jboss/aerogear/cookbook/authentication) cookbook's.

> **NOTE:**  It is advised that HTTPS should be used by default when performing authentication of this type. For convenience of deployment , in this example we use HTTP in the backend example but you should opt to enable HTTPS in your application server of choice.

## GET /rest/grocery/bacons  (HTTP Digest)

Retrieve grocery shopping list
curl -c cookie -b cookie -v --digest -u "agnes:123" http://localhost:8080/authentication/rest/grocery/bacons

```
curl -v \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -c cookie -b cookie \
     --digest -u "agnes:123" \
     -X GET http://localhost:8080/authentication/rest/grocery/bacons
```

## GET /rest/grocery/beers  (HTTP Basic)

Retrieve grocery shopping list
curl -c cookie -b cookie -v --digest -u "agnes:123" http://localhost:8080/authentication/rest/grocery/bacons

```
curl -v \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -c cookie -b cookie \
     -u "john:123" \
     -X GET http://localhost:8080/authentication/rest/grocery/beers
```

### Build and Deploy

     mvn clean install
     mvn wildfly:deploy