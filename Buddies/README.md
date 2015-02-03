## Buddies: simple REST endpoint

### GET /rest/team/developers

Retrieve list of team developers

```
curl -v \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -X GET http://localhost:8080/aerogear-integration-tests-server/rest/team/developers
```


### Build and Deploy

	mvn clean install
	mvn wildfly:deploy

To test iOS client go to [iOS cookbook Buddies recipe](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Buddies)
To test Android client go to [Android cookbook](https://github.com/aerogear/aerogear-android-cookbook/tree/master/src/org/jboss/aerogear/cookbook/pipeline).
To test Web client go to [TODO]().
To test Cordova client go to [TODO]().
