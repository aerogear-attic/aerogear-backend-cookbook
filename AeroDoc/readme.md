# AeroDoc

This backend application is a showcase / quickstart part of Aerogear's UnifiedPush facilities.
This Application shows how a business specific App can interact with [aerogear-unifiedpush-server](https://github.com/aerogear/aerogear-unifiedpush-server) . It shows how it can use the  [aerogear-unifiedpush-java-client](https://github.com/aerogear/aerogear-unifiedpush-java-client) to send Unified Messages to the push server.

## Table of contents

* [What is this](what-is-this)
  * [The Client app](the-client-app)
  * [Admin console](admin-console)
  * [Workflow](workflow)
* [How do I run it](how-do-i-run-it)
  * [Prerequisites](prerequisites)
  * [Deploying the app](deploying-the-app)
  * [Admin console](admin-console-1)
     * [Creating a lead](creating-a-lead)
  *[Clients apps](clients-apps)
     * [Available aliases](available-aliases)
* [Rest API](rest-api)
   * [Login](post-restlogin)
   * [Logout](post-restlogout)
   * [List of leads](get--restleads)
   * [Update a SaleAgent](put-restsaleagentsid)
   * [Update a Lead](put-restleadsid)

## What is this?

AeroDoc is a company in the health care industry, selling a revolutionary tensiometer. Their clients are doctors. AeroDoc has several sales agents all over the United States. At the headquarters, they have their "first line" sales departement, doing cold calls all along the day. As soon they have a concrete lead, they use their AeroDoc Admin app to filter out available sales Agents available in the lead area. They can then send them a push notification.

The sales agent receives the notification on their mobile device that a new lead is available. The agent will handle the lead by "accepting" the notifcation, informing the other agents that the lead has been processed.

In this highly competitive market of the tensiometers, be able to process a lead directly is for sure a competitive advantage.

### The client app

1. Consist of a list of leads: a lead can be _open_ or _in process_, leads _in process_ of other sales are not visible.
   * optional: when the client tap a lead it appears on a map
1. Has a status that he can set: `STANDBY` | `WITH_CLIENT` | `PTO` 
1. Has a location
1. Has an alias

### Admin console

1. Can create a new lead (name and a location)
1. Can query for Sales Agents based on status and location
1. Can assign a lead to a selection (1..n) of sales agents, this will send out the notifications.
1. Manage the Sales Agents DB.

### Workflow 

1. Your are a Sale Agent (SA) and you log yourself
1. First screen displayed a list of all unassigned leads retrieved server side (Rest service by default send only unassigned leads)
1. An admin pushes a new lead to a chosen list of SA (including you)
1. You receive the push notification, alert is displayed
1. Your device refresh the list of unassigned leads (server side call). Potentially retrieving also unassigned leads not directly pushed to you. Nice to have feature: The one pushed to you should be highlighted.
1. You accept the lead. The lead is removed from unassigned list and go in Second tab: your accepted list which is stored locally on you device.
1. On acceptation of your lead, you send an update to server. Server side broadcast to all SA (except yourself) to refresh unassigned leads list.

## How do I run it?

### Prerequisites

Be sure to have a running instance of a UnifiedPush Server. Instructions can be found [here](https://github.com/aerogear/aerogear-unifiedpush-server).

### Deploying the app

```shell
mvn clean install -Pwildfly wildfly:deploy
```

### Admin console

Browse to [http://localhost:8080/aerodoc](http://localhost:8080/aerodoc)

> You should log in with _john/123_, he has admin rights and create leads and send notification.

#### Configure the Push Server Details

On the left menu you should have a link to _Push Configuration_. From there you can create and manage your push configs. You should have at least 1 config available.

You can have more than one config but only one can be active. This can be useful when you have to switch between localhost and an OpenShift hosted Push Server for instance. Switching from one config to another can be done at runtime and no restart is needed. Just select the config you want and make sure it has _active_ enabled. 

#### Creating a lead

Use the form to create a new Lead

#### Send a lead to Sale Agents

1. Select a lead from the lead's list
1. If needed enter the 2 available criterias _status_ and/or _location_ (you can also search without criteria).
1. Select the agents you want to send the lead to.
1. Push the button _Send Lead_

### Clients apps

You can use one of our client apps, to interact with this.

* [iOS](https://github.com/aerogear/aerogear-aerodoc-ios)
* [Android](https://github.com/aerogear/aerogear-aerodoc-android)
* [Web](https://github.com/aerogear/aerogear-aerodoc-web)

#### Available aliases

When registring the device on the Push Server make sure to use one of the existing aliases (or you should create a new user on the AeroDoc Admin Page):

* john
* jake
* maria
* bob

For all these aliases the password is _123_

## Rest API

The native clients can access the following REST based services:

### ```POST /rest/login``` 

Login service, mandatory for all further request.

```shell
curl -v -b cookies.txt \
        -c cookies.txt \
        -H "Accept: application/json" \
        -H "Content-type: application/json" \
        -X POST \
        -d '{"loginName": "john", "password":"123"}' http://http://localhost:8080/aerodoc/rest/login 
```

It will return the user info:

```json
 {
 	"id": "cb3c05aa-3fdd-4b4e-9b90-386fc7e5671a",
 	"enabled": true,
 	"createdDate": 1371215851063,
 	"expirationDate": null,
 	"partition": null,
 	"loginName": "john",
 	"firstName": null,
 	"lastName": null,
 	"email": null,
 	"status": "PTO",
 	"password": "123",
 	"location": "New York"
 }
```

### ```POST /rest/logout``` 

Logout service.

```shell
curl -v -b cookies.txt \
     -c cookies.txt \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -X POST \
     -d '{"loginName": "john", "password":"123"}' http://http://localhost:8080/aerodoc/rest/logout 
```

returns no data

### ```GET  /rest/leads```

Obtain a list of leads.

```shell
 curl -v -b cookies.txt \
      -c cookies.txt \
      -H "Accept: application/json" \
      -H "Content-type: application/json" \
      -X GET http://http://localhost:8080/aerodoc/rest/leads 
```

You will get a list of leads **which has not a saleAgent set yet** :

```json
[{
	"id": 39,
	"version": 0,
	"name": "Doctor No",
	"location": "New York",
	"phoneNumber": "0612412121"
}]
```

### ``` PUT /rest/saleagents/{id} ```

Update a SaleAgent, the service will only update the _status_ and the _location_ for now.

```shell
curl -v -b cookies.txt \
     -c cookies.txt \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -X PUT -d '{"id":"13bbaea3-9271-43f7-80aa-fb21360ff684","enabled":true,"createdDate":1371213256827,"expirationDate":null,"partition":null,"loginName":"john","firstName":null,"lastName":null,"email":null,"version":0,"status":"CHANGED","password":"123","location":"New York"}' http://localhost:8080/aerodoc/rest/saleagents/13bbaea3-9271-43f7-80aa-fb21360ff684
```

returns no data

### ```PUT /rest/leads/{id}```

Update a Lead, typically used if a Sale Agent wants to assign a lead to him.

```shell
curl -v -b cookies.txt \
     -c cookies.txt \
     -H "Accept: application/json" \
     -H "Content-type: application/json" \
     -X PUT -d '{"id":39,"version":0,"name":"Doctor No","location":"New York","phoneNumber":"121212121","saleAgent":"13bbaea3-9271-43f7-80aa-fb21360ff684"}' http://localhost:8080/aerodoc/rest/leads/39
```

returns no data
