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
package org.jboss.aerogear.shoot.model;

import javax.persistence.*;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Models a Photo
 */
@Entity
@XmlRootElement
@NamedQueries({
        @NamedQuery(name = "Photo.findAllPhotos",  query = "SELECT p FROM Photo p"),
        @NamedQuery(name = "Photo.findByPrimaryKey", query = "SELECT p FROM Photo p WHERE p.id = :id")
})
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column
    private String filename;

    public Photo() {}

    public Photo(String filename) {
        this.filename = filename;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }
}