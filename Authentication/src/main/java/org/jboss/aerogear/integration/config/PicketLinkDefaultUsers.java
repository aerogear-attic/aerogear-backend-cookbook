/**
 * JBoss, Home of Professional Open Source
 * Copyright Red Hat, Inc., and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * 	http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.integration.config;

import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.PartitionManager;
import org.picketlink.idm.RelationshipManager;
import org.picketlink.idm.credential.Digest;
import org.picketlink.idm.credential.Password;
import org.picketlink.idm.model.basic.User;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.inject.Inject;

@Singleton
@Startup
public class PicketLinkDefaultUsers {

    @Inject
    private PartitionManager partitionManager;

    private IdentityManager identityManager;
    private RelationshipManager relationshipManager;

    @PostConstruct
    public void create() {

        this.identityManager = partitionManager.createIdentityManager();
        this.relationshipManager = partitionManager.createRelationshipManager();

        User john = newUser("john", "john@doe.com", "John", "Doe");
        this.identityManager.updateCredential(john, new Password("123"));

        User agnes = newUser("agnes", "agnes@doe.com", "Agnes", "Doe");
        Digest digest = new Digest();
        digest.setRealm("default");
        digest.setUsername(agnes.getLoginName());
        digest.setPassword("123");
        this.identityManager.updateCredential(agnes, digest);
    }

    private User newUser(String john, String email, String firstName, String lastName) {
        User user = new User(john);
        user.setEmail(email);
        user.setFirstName(firstName);
        user.setLastName(lastName);

        /*
         * Note: Password will be encoded in SHA-512 with SecureRandom-1024 salt
         * See http://lists.jboss.org/pipermail/security-dev/2013-January/000650.html for more information
         */
        this.identityManager.add(user);
        return user;
    }
}
