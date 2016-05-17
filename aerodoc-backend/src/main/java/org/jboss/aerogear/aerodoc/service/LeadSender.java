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
package org.jboss.aerogear.aerodoc.service;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javax.inject.Inject;
import org.jboss.aerogear.aerodoc.model.Lead;
import org.jboss.aerogear.aerodoc.model.PushConfig;
import org.jboss.aerogear.aerodoc.rest.PushConfigEndpoint;
import org.jboss.aerogear.unifiedpush.DefaultPushSender;
import org.jboss.aerogear.unifiedpush.PushSender;
import org.jboss.aerogear.unifiedpush.message.MessageResponseCallback;
import org.jboss.aerogear.unifiedpush.message.UnifiedMessage;

public class LeadSender {

    private static final Logger logger = Logger.getLogger(LeadSender.class.getName());

    @Inject
    PushConfigEndpoint pushConfigEndpoint;

    private PushSender javaSender;

    public LeadSender() {

    }

    public void sendLeads(List<String> users, Lead lead) {
        System.setProperty("jsse.enableSNIExtension", "false");
        javaSender = DefaultPushSender.withRootServerURL(getActivePushConfig().getServerURL()).pushApplicationId(getActivePushConfig().getPushApplicationId())
                        .masterSecret(getActivePushConfig()
                        .getMasterSecret()).build();
        if (getActivePushConfig() != null) {
            UnifiedMessage unifiedMessage = UnifiedMessage.withMessage()
                    .criteria()
                        .categories("lead")
                        .aliases(users)
                    .message()
                        .apns().actionCategory("acceptLead")
                    .build()
                        .simplePush("version=" + new Date().getTime())
                    .userData("id", lead.getId().toString())
                    .userData("messageType", "pushed_lead")
                    .userData("name", lead.getName())
                    .userData("location", lead.getLocation())
                    .userData("phone", lead.getPhoneNumber()).sound("default")
                    .alert("A new lead has been created").build();

            javaSender.send(unifiedMessage, new LeadSenderMessageResponseCallback());

        } else {
            logger.severe("not PushConfig configured, can not send message");
        }
    }

    public void sendBroadCast(Lead lead) {
        javaSender = DefaultPushSender.withRootServerURL(getActivePushConfig().getServerURL())
                        .pushApplicationId(getActivePushConfig().getPushApplicationId())
                        .masterSecret(getActivePushConfig().getMasterSecret()).build();
        if (getActivePushConfig() != null) {
            UnifiedMessage unifiedMessage = UnifiedMessage.withMessage()
                    
                    .userData("id", lead.getId().toString())
                    .userData("messageType", "pushed_lead")
                    .simplePush("version=" + new Date().getTime())
                    .userData("name", lead.getName())
                    .userData("location", lead.getLocation())
                    .userData("phone", lead.getPhoneNumber())
                    .userData("messageType", "accepted_lead").sound("default")
                    .alert("A new lead has been accepted").build();

            javaSender.send(unifiedMessage, new LeadSenderMessageResponseCallback());

        } else {
            logger.severe("not PushConfig configured, can not send message");
        }
    }

    public PushSender getJavaSender() {
        return javaSender;
    }

    public void setJavaSender(PushSender javaSender) {
        this.javaSender = javaSender;
    }

    private PushConfig getActivePushConfig() {
        PushConfig pushConfig = pushConfigEndpoint.findActiveConfig();
        return pushConfig;

    }

    /**
     * Simple, stateless innerclass, that implements logger for the callbacks of
     * the MessageResponseCallback class.
     */
    private static class LeadSenderMessageResponseCallback implements MessageResponseCallback {

        @Override
        public void onComplete() {
            logger.info("Message submitted");
        }

    }

}
