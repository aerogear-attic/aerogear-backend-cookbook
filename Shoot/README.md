## Shoot'nShare backend

A simple example to demo OAuth2 authorization code grant with Keycloak.

### Pre-requisites

Shoot'nShare backend uses [Keycloak](http://keycloak.jboss.org/) to authenticate our client apps, so we need to deploy and configure the [Keycloak](http://keycloak.jboss.org/) server.

1. Download [Keycloak Appliance Distribution (version 1.0.4.Final)](http://docs.jboss.org/keycloak/docs/1.0.4.Final/userguide/html/server-installation.html#Appliance_install)
1. Start the server `KEYCLOAK_APPLIANCE_HOME/Keycload/bin/standalone.sh -b 0.0.0.0`
1. Open [http://localhost:8080/auth/admin/index.html](http://localhost:8080/auth/admin/index.html)
1. Login using _admin_ / _admin_
1. Click on `Add realm`
1. import this our [realm configuration file](configuration/shoot-realm.json)

### Build and Deploy

```
mvn clean install
mvn wildfly:deploy
```

### Using

Upload an image using one of our Shoot and Share example app

| Plataform  | Repository |
|:----------:|:--------------------------------------------|
| Android    | https://github.com/aerogear/aerogear-android-cookbook/tree/master/ShootAndShare |
| iOS        | https://github.com/aerogear/aerogear-ios-cookbook/tree/master/Shoot             |
| Cordova    | https://github.com/aerogear/aerogear-cordova-cookbook/tree/master/Shoot         |

### Application flow

1. Go to [http://localhost:8080/shoot/photos/](http://localhost:8080/shoot/photos/)
1. Login using _user_ / _password_
1. See the picutres uploaded

![Shoot'nShare web-app](https://github.com/aerogear/aerogear-backend-cookbook/raw/master/Shoot/Shoot_web-app.png "Shoot web-app")

### How does it work?

#### Upload from your phone with OAuth2 client

In the Keycloak realm `configuration/shoot-realm.json`, create an application for your rest endpoint (your services)
Here we define `bearerOnly` as this end-point will not be used for login, it does not offer any redirect urls, it is just a plain OAuth2 service.

```
"applications" : [ {
    "name" : "shoot-services",
    "enabled" : true,
    "bearerOnly" : true,
    "publicClient" : true
},
```

You should also define a OAuht2 client, the name `shoot-third-party` should match your `client_id` in your client app and the `redirectUris` is the `redirect_uri`.

```
"oauthClients" : [ {
    "name" : "shoot-third-party",
    "redirectUris" : [ "org.aerogear.Shoot://oauth2Callback" ],
    "webOrigins" : [ ],
    "enabled" : true,
    "publicClient" : true
} ]
```

Define your services to list, upload, get images. (Check `src/main/java/org/jboss/aerogear/shoot/PhotoService.java`)

To secure your endpoints, user wildfly Keycloak adapter. This simple demo uses a "per war approach". Refer to [Keycloak adapters chapter for more details](http://docs.jboss.org/keycloak/docs/1.0-final/userguide/html/ch07.html)
Go in `src/main/webapp/WEB-INF/web.xml`

```
<security-constraint>
    <web-resource-collection>
        <url-pattern>/rest/*</url-pattern>
    </web-resource-collection>
    <auth-constraint>
        <role-name>user</role-name>
    </auth-constraint>
</security-constraint>

<login-config>
    <auth-method>KEYCLOAK</auth-method>
    <realm-name>shoot-realm</realm-name>
</login-config>

<security-role>
    <role-name>user</role-name>
</security-role>
```

NOTE: For simplicity our demo does not use https BUT all OAuth2 protected REST points should be using SSL. If you are using WildFly or EAP follow this [link to enable HTTPS](https://docs.jboss.org/author/pages/viewpage.action?pageId=66322705) or consult your application server's documentation page.

and use `src/main/webapp/WEB-INF/keycloak.json`, you link your java endpoint with Keycloak realm application. Here the application is names `shoot-services`

```
{
    "realm": "shoot-realm",
    "realm-public-key": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUgrGF7rNIYRSWCZlT+JXGtjZtnn8/ZObzW12YSoRBUJ0mm6wzO6p8+aQYMXvtvB88zeWBD9+uZh8gWj+iOqByWCfX0Wez+mVK8ofhAsGniv631u+wmDESLrLvROX12r1fzmmVJYWOzEGW4v2Xmahl/6gHnzV0mHZfmJXEOniHqwIDAQAB",
    "bearer-only": true,
    "ssl-required": "external",
    "resource": "shoot-services"
}
```

#### View all users images in web-app

The web-app is using angular, the `webapp\photos\js\app.js` loads the configuration file `webapp\config\keycloak.json`.

```
{
    "realm": "shoot-realm",
    "realm-public-key": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCUgrGF7rNIYRSWCZlT+JXGtjZtnn8/ZObzW12YSoRBUJ0mm6wzO6p8+aQYMXvtvB88zeWBD9+uZh8gWj+iOqByWCfX0Wez+mVK8ofhAsGniv631u+wmDESLrLvROX12r1fzmmVJYWOzEGW4v2Xmahl/6gHnzV0mHZfmJXEOniHqwIDAQAB",
    "auth-server-url": "/auth",
    "ssl-required": "external",
    "resource": "shoot-web",
    "public-client": true
}
```

where `shoot-web` matches an application define in realm `configuration/shoot-realm.json`.

`webapp\photos\js\app.js` configures an auth intercepter. Each http calls will be intercepted and added relevant authorization headers. 

```
module.factory('authInterceptor', function($q, Auth) {
    return {
        request: function (config) {
            var deferred = $q.defer();
            if (Auth.authz.token) {
                Auth.authz.updateToken(5).success(function() {
                    config.headers = config.headers || {};
                    config.headers.Authorization = 'Bearer ' + Auth.authz.token;

                    deferred.resolve(config);
                }).error(function() {
                        deferred.reject('Failed to refresh token');
                    });
            }
            return deferred.promise;
        }
    };
});
```


#### Shoot-realm

In your realm you have one web app `shoot-web`, secure services endpoints `shoot-services` and one OAuth2 client `shoot-third-party`.

![Shoot-realm](https://github.com/corinnekrych/aerogear-backend-cookbook/raw/master/Shoot/shoot-ream.png "Shoot-realm")

