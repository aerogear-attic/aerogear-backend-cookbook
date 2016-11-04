# Authentication

## What is it?

A simple JAX-RS example that uses [Picketlink](http://picketlink.org) to demo HTTP Basic/Digest authentication. 

> **NOTE:**  It is advised that HTTPS should be used by default when performing authentication of this type. For convenience of deployment, in this example we use HTTP in the backend example but you should opt to enable HTTPS in your application server of choice.

## How do I run it?

### Build and run locally

```shell
mvn clean install
mvn wildfly:deploy
```

### Deploy on OpenShift

> To follow the instructions below, you need [rhc](https://developers.openshift.com/en/managing-client-tools.html) installed.

```shell
rhc app create bacon jboss-wildfly-8
```

## Client apps

The demo was built to support the _Authentication_ cookbook client apps:

* [iOS](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Authentication) 
* [Android](https://github.com/aerogear/aerogear-android-cookbook/tree/master/src/org/jboss/aerogear/cookbook/authentication)

## API

### HTTP Digest - GET /rest/grocery/bacons

Retrieve grocery bacon shopping list

```shell
curl -v \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -c cookie -b cookie \
     --digest -u "agnes:123" \
     -X GET http://localhost:8080/authentication/rest/grocery/bacons
```

### HTTP Basic - GET /rest/grocery/beers

Retrieve grocery beers shopping list

```shell
curl -v \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -c cookie -b cookie \
     -u "john:123" \
     -X GET http://localhost:8080/authentication/rest/grocery/beers
```
