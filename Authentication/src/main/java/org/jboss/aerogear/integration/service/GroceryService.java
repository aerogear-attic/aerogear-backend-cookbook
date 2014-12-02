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

package org.jboss.aerogear.integration.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Arrays;
import java.util.List;

@Path("/grocery")
public class GroceryService {

    @GET
    @Path("/bacons")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> bacons() {
        return Arrays.asList("Turkey", "Jowl", "Canadian", "Speck", "Pancetta");
    }

    @GET
    @Path("/beers")
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> beers() {
        return Arrays.asList("Belgium", "California", "Michigan", "Ireland", "British");
    }
}
