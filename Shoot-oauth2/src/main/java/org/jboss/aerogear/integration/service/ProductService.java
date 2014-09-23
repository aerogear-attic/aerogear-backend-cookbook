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

import org.jboss.aerogear.integration.model.Product;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

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
    private static final String SERVER_UPLOAD_LOCATION_FOLDER = "/Users/corinne/aerogear/keycloak/aerogear-backend-cookbook/Shoot-oauth2/";

    @GET
    @Path("/{name}")
    @Produces("image/jpeg")
    public byte[] get(@PathParam("name") String name) {

        File file = new File(SERVER_UPLOAD_LOCATION_FOLDER + name);
        try {
            return Files.readAllBytes(Paths.get(file.toURI()));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        String fileName = "";

        Map<String, List<InputPart>> formParts = input.getFormDataMap();

        List<InputPart> inPart = formParts.get("data");

        for (InputPart inputPart : inPart) {

            try {

                // Retrieve headers, read the Content-Disposition header to obtain the original name of the file
                MultivaluedMap<String, String> headers = inputPart.getHeaders();
                fileName = parseFileName(headers);

                // Handle the body of that part with an InputStream
                InputStream istream = inputPart.getBody(InputStream.class,null);

                fileName = SERVER_UPLOAD_LOCATION_FOLDER + fileName;

                saveFile(istream, fileName);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        String output = "File saved to server location : " + fileName;

        return Response.status(200).entity(output).build();
    }

    // Parse Content-Disposition header to get the original file name
    private String parseFileName(MultivaluedMap<String, String> headers) {

        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

        for (String name : contentDispositionHeader) {

            if ((name.trim().startsWith("filename"))) {

                String[] tmp = name.split("=");

                String fileName = tmp[1].trim().replaceAll("\"","");

                return fileName;
            }
        }
        return "randomName";
    }

    // save uploaded file to a defined location on the server
    private void saveFile(InputStream uploadedInputStream,
                          String serverLocation) {

        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            outpuStream = new FileOutputStream(new File(serverLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                outpuStream.write(bytes, 0, read);
            }
            outpuStream.flush();
            outpuStream.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

}
