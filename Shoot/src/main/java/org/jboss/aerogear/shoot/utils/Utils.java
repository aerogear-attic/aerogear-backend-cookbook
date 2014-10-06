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

package org.jboss.aerogear.shoot.utils;

import javax.ws.rs.core.MultivaluedMap;
import java.io.*;

/**
 * Some useful utility methods
 */
public class Utils {

    // Parse Content-Disposition header to get the original file name
    public static String parseFileName(MultivaluedMap<String, String> headers) {
        String[] contentDispositionHeader = headers.getFirst("Content-Disposition").split(";");

        for (String name : contentDispositionHeader)
            if ((name.trim().startsWith("filename"))) {

                String[] tmp = name.split("=");

                return tmp[1].trim().replaceAll("\"", "");
            }

        return null;
    }

    // save uploaded file to a defined location on the server
    public static void saveFile(InputStream uploadedInputStream,
                                String serverLocation) {
        try {
            OutputStream outpuStream = new FileOutputStream(new File(serverLocation));
            byte[] bytes = new byte[1024];

            int read;
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