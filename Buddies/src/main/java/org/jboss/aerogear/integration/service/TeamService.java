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

import org.jboss.aerogear.integration.model.Developer;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

@Path("/team")
public class TeamService {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/developers")
    public List<Developer> team() {
        final int photoSize = 300;

        List<Developer> team = new ArrayList<Developer>();

        Developer abstractj = new Developer();
        abstractj.setName("Bruno Oliveira");
        abstractj.setTwitter("abstractj");
        abstractj.setPhotoURL("http://www.gravatar.com/avatar/a730f764d54b20861e5e90bfc6b5d1d6?s=" + photoSize);
        team.add(abstractj);

        Developer balunasj = new Developer();
        balunasj.setName("Jay Balunas");
        balunasj.setTwitter("tech4j");
        balunasj.setPhotoURL("http://www.gravatar.com/avatar/083df806b14f0df14278ad7072d2bcfb?s=" + photoSize);
        team.add(balunasj);

        Developer corinnekrych = new Developer();
        corinnekrych.setName("Corinne Krych");
        corinnekrych.setTwitter("corinnekrych");
        corinnekrych.setPhotoURL("http://www.gravatar.com/avatar/20a907ee0ab0e4756e19727209d0ac64?s=" + photoSize);
        team.add(corinnekrych);

        Developer cvasilak = new Developer();
        cvasilak.setName("Christos Vasilakis");
        cvasilak.setTwitter("cvasilak");
        cvasilak.setPhotoURL("http://www.gravatar.com/avatar/ebdacc9d66cf94bab5cbbcecc98c5082?s=" + photoSize);
        team.add(cvasilak);

        Developer dbevenius = new Developer();
        dbevenius.setName("Daniel Bevenius");
        dbevenius.setTwitter("dbevenius");
        dbevenius.setPhotoURL("http://www.gravatar.com/avatar/1be2cab21d939dc1abb2ed47b46877c6?s=" + photoSize);
        team.add(dbevenius);

        Developer edewit = new Developer();
        edewit.setName("Erik Jan de Wit");
        edewit.setTwitter("edewit");
        edewit.setPhotoURL("http://www.gravatar.com/avatar/420a03dc70653e8b571382be483f2ce7?s=" + photoSize);
        team.add(edewit);

        Developer lfryc = new Developer();
        lfryc.setName("Lukáš Fryč");
        lfryc.setTwitter("lfryc");
        lfryc.setPhotoURL("http://www.gravatar.com/avatar/739e72e6563e5a221b69c19c31ce6228?s=" + photoSize);
        team.add(lfryc);

        Developer lholmquist = new Developer();
        lholmquist.setName("Lucas Holmquist");
        lholmquist.setTwitter("sienaluke");
        lholmquist.setPhotoURL("http://www.gravatar.com/avatar/c14ef01065d7349d452fa4f5fcd01c6c?s=" + photoSize);
        team.add(lholmquist);

        Developer matzew = new Developer();
        matzew.setName("Matthias Wessendorf");
        matzew.setTwitter("mwessendorf");
        matzew.setPhotoURL("http://www.gravatar.com/avatar/df135e9a2604ec2ce5d12ad049a8c99b?s=" + photoSize);
        team.add(matzew);

        Developer passos = new Developer();
        passos.setName("Daniel Passos");
        passos.setTwitter("passos");
        passos.setPhotoURL("http://www.gravatar.com/avatar/a796aaf10cd10acde35c4004d935ff0c?s=" + photoSize);
        team.add(passos);

        Developer qmx = new Developer();
        qmx.setName("Douglas Campos");
        qmx.setTwitter("qmx");
        qmx.setPhotoURL("http://www.gravatar.com/avatar/684b4bfe97a40454db104abcb601e375?s=" + photoSize);
        team.add(qmx);

        Developer sblanc = new Developer();
        sblanc.setName("Sébastien Blanc");
        sblanc.setTwitter("sebi2706");
        sblanc.setPhotoURL("http://www.gravatar.com/avatar/8d9d426881112f44887500d96852f725?s=" + photoSize);
        team.add(sblanc);

        Developer summersp = new Developer();
        summersp.setName("Summers Pittman");
        summersp.setTwitter("summerspittman");
        summersp.setPhotoURL("http://www.gravatar.com/avatar/c9f69f10d588aa96f31181e758db4d24?s=" + photoSize);
        team.add(summersp);

        return team;
    }

}
