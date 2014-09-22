## OAuth2 with Keycloak 
A simple example to demo OAUth2 authorization code grant with Keycloak.

### Pre-requisites

* use [Keycloak Appliance Distribution](http://docs.jboss.org/keycloak/docs/1.0-final/userguide/html/server-installation.html#Appliance_install) comes with a preconfigured Keycloak server (based on Wildfly). 

* go to KEYCLOAK_APPLIANCE_HOME/Keycload/bin and start the server
	
	standalone.sh -b0.0.0.0

* open a browser to http://localhost:8080/auth/admin/index.html. You

* import realm [configuration file](configuration/testrealm.json)

### Build and Deploy

	mvn clean install
	mvn wildfly:deploy

To test iOS client go to [iOS cookbook ProductInventory recipe](https://github.com/aerogear/aerogear-ios-cookbook/tree/master/ProductInventory)
To test Android client go to [TODO]().
To test Web client go to [TODO]().
To test Cordova client go to [TODO]().

### How does it work?

With resteasy, a simple endpoint is defined: 

	@Path("/portal")
	public class ProductService {

	    @GET
	    @Produces(MediaType.APPLICATION_JSON)
	    @Path("/products")
	    public List<Product> product() {
	        List<Product> products = new ArrayList<Product>();

	        Product iPhone = new Product("iPhone", "2");
	        products.add(iPhone);

	        Product iPad = new Product("iPad", "6");
	        products.add(iPad);

	        return products;
	    }

	}
To secure the endpoint, this simple demo uses a "per war approach". Refer to [Keycloak adapters chapter for more details](http://docs.jboss.org/keycloak/docs/1.0-final/userguide/html/ch07.html)
Go in ```src/main/webapp/WEB-INF/web.xml```

```
   <security-constraint>
        <web-resource-collection>
            <url-pattern>/rest/portal/*</url-pattern>
        </web-resource-collection>
        <auth-constraint>
            <role-name>user</role-name>
        </auth-constraint>
    </security-constraint>

    <login-config>
        <auth-method>KEYCLOAK</auth-method>
        <realm-name>product-inventory</realm-name>
    </login-config>

    <security-role>
        <role-name>user</role-name>
    </security-role>
```

and use keycloak.json

	{
	    "realm": "product-inventory",
	    "realm-public-key": "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCrVrCuTtArbgaZzL1hvh0xtL5mc7o0NqPVnYXkLvgcwiC3BjLGw1tGEGoJaXDuSaRllobm53JBhjx33UNv+5z/UMG4kytBWxheNVKnL6GgqlNabMaFfPLPCF8kAgKnsi79NMo+n6KnSY8YeUmec/p2vjO2NjsSAVcWEQMVhJ31LwIDAQAB",
	    "auth-server-url": "http://localhost:8080/auth",
	    "ssl-required": "external",
	    "resource": "product-inventory",
	    "public-client": true
	}
