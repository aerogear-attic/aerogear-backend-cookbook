## Authentication
A simple JAX-RS example that utilizes [Picketlink](http://picketlink.org) to demo HTTP Basic/Digest authentication. The demo was build to support the ```Authentication``` demo found in AeroGear [iOS](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Authentication) and [Android](https://github.com/aerogear/aerogear-android-cookbook/tree/master/src/org/jboss/aerogear/cookbook/authentication) cookbook's.

## GET /rest/grocery/bacons  (HTTP Digest)

Retrieve list of team developers
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

Retrieve list of team developers
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