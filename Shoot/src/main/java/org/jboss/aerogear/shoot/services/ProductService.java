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
package org.jboss.aerogear.shoot.services;

import org.jboss.aerogear.shoot.model.Product;
import org.jboss.aerogear.shoot.utils.Utils;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Products Service
 */
@Stateless
@Path("/products")
public class ProductService {

    // currently we use system 'temp' directory to store files
    private static final String
            SERVER_UPLOAD_LOCATION_FOLDER = System.getProperty("java.io.tmpdir");

    @PersistenceContext(unitName = "shoot")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAll() {
        TypedQuery<Product> query = em.createNamedQuery("Product.findAllProducts", Product.class);
        return query.getResultList();
    }

    @GET
    @Path("{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Product get(@PathParam("id") long id) {
        Product product = em.createNamedQuery("Product.findByPrimaryKey", Product.class)
                .setParameter("id", id)
                .getSingleResult();

        if (product == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return product;
    }

    @GET
    @Path("/images/{filename}")
    @Produces("image/jpeg")
    public Response getImage(@PathParam("filename") String filename) {
        File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + filename);

        if (!file.exists()) { // if the file doesn't exist
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder response = Response.ok(file);
        response.header("Content-Disposition", "attachment; filename=" + filename);

        return response.build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Product product) {
        em.persist(product);

        return Response.created(
                UriBuilder.fromResource(ProductService.class)
                        .path(String.valueOf(product.getId())).build()).build();
    }

    @POST
    @Consumes("multipart/form-data")
    public Response upload(MultipartFormDataInput input) {
        List<InputPart> parts = input.getParts();

        String name = null, filename = null;

        // extract multipart parts and save uploaded file
        for (InputPart part : parts) {
            try {
                MultivaluedMap<String, String> headers = part.getHeaders();
                filename = Utils.parseFileName(headers);

                if (filename != null) {
                    InputStream is = part.getBody(InputStream.class, null);

                    Utils.saveFile(is, SERVER_UPLOAD_LOCATION_FOLDER + filename);
                } else {
                    name = part.getBodyAsString();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Product product = new Product(name, filename);
        em.persist(product);
        return Response.created(
                UriBuilder.fromResource(ProductService.class)
                        .path(String.valueOf(product.getId())).build()).build();
    }

    @PUT
    @Path("{id : \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Product product) {
        Product existing = em.createNamedQuery("Product.findByPrimaryKey", Product.class)
                .setParameter("id", id)
                .getSingleResult();

        if (existing == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        em.merge(product);
        return Response.ok(
                UriBuilder.fromResource(ProductService.class)
                        .path(String.valueOf(product.getId())).build()).build();
    }

    @DELETE
    @Path("{id : \\d+}")
    public Response delete(@PathParam("id") long id) {
        Product product = em.createNamedQuery("Product.findByPrimaryKey", Product.class)
                .setParameter("id", id)
                .getSingleResult();

        if (product == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        em.remove(product);
        return Response.noContent().build();
    }
}