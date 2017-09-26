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

import org.jboss.aerogear.shoot.model.Photo;
import org.jboss.aerogear.shoot.utils.Utils;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;
import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

/**
 * Photos Service
 */
@ApplicationScoped
@Path("/photos")
public class PhotoService {

    // currently we use system 'temp' directory to store files
    private static final String
            SERVER_UPLOAD_LOCATION_FOLDER = System.getProperty("java.io.tmpdir") + File.separator;

    @PersistenceContext(unitName = "shoot")
    private EntityManager em;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Photo> getAll() {
        TypedQuery<Photo> query = em.createNamedQuery("Photo.findAllPhotos", Photo.class);
        if (query.getResultList().isEmpty()) {
            return new ArrayList<Photo>();
        }
        return query.getResultList();
    }

    @GET
    @Path("{id : \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public Photo get(@PathParam("id") long id) {
        Photo photo = em.createNamedQuery("Photo.findByPrimaryKey", Photo.class)
                .setParameter("id", id)
                .getSingleResult();

        if (photo == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        return photo;
    }

    @GET
    @Path("/images/{filename}")
    @Produces("image/jpeg")
    public Response getImage(@PathParam("filename") String filename) {
        File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + filename);

        if (!file.exists()) { // if the file doesn't exist
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        Response.ResponseBuilder response = null;
        try {
            response = Response.ok(Base64.getEncoder().encode(Files.readAllBytes(Paths.get(SERVER_UPLOAD_LOCATION_FOLDER + filename))));
        } catch (IOException e) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        response.header("Content-Disposition", "attachment; filename=" + filename);

        return response.build();
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Photo photo) {
        em.persist(photo);

        return Response.created(
                UriBuilder.fromResource(PhotoService.class)
                        .path(String.valueOf(photo.getId())).build()).build();
    }

    @POST
    @Transactional
    @Consumes("multipart/form-data")
    @Produces(MediaType.APPLICATION_JSON)
    public Photo upload(MultipartFormDataInput input) {
        List<InputPart> parts = input.getParts();
        String filename = null;
        for (InputPart part : parts) {
            try {
                filename = UUID.randomUUID() + ".jpg";
                InputStream is = part.getBody(InputStream.class, null);
                Utils.saveFile(is, SERVER_UPLOAD_LOCATION_FOLDER  + filename);
            } catch (IOException e) {
                throw new WebApplicationException(e);
            }
        }

        Photo photo = new Photo(filename);
        em.persist(photo);
        return photo;
    }

    @PUT
    @Path("{id : \\d+}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") long id, Photo photo) {
        Photo existing = em.createNamedQuery("Photo.findByPrimaryKey", Photo.class)
                .setParameter("id", id)
                .getSingleResult();

        if (existing == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        em.merge(photo);
        return Response.ok(
                UriBuilder.fromResource(PhotoService.class)
                        .path(String.valueOf(photo.getId())).build()).build();
    }

    @DELETE
    @Path("{id : \\d+}")
    @Transactional
    public Response delete(@PathParam("id") long id) {
        Photo photo = em.createNamedQuery("Photo.findByPrimaryKey", Photo.class)
                .setParameter("id", id)
                .getSingleResult();

        if (photo == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }

        em.remove(photo);
        return Response.noContent().build();
    }
}