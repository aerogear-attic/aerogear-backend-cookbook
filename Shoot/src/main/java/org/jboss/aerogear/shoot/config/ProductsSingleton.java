package org.jboss.aerogear.shoot.config;

import org.jboss.aerogear.shoot.model.Product;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

@Singleton
@Startup
public class ProductsSingleton {

    @PersistenceContext(unitName = "shoot")
    private EntityManager em;

    /**
     * Loads some test data during the initialization of the app
     */
    @PostConstruct
    public void create() {
        em.persist(new Product("iPhone"));
        em.persist(new Product("iPad"));
        em.persist(new Product("iPad"));
    }
}

