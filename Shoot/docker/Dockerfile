FROM jboss/base-jdk:8

ENV MAVEN_OPTS -Xmx768m -XX:+UseConcMarkSweepGC -XX:MaxPermSize=128m -XX:+CMSClassUnloadingEnabled
ENV MAVEN_VERSION 3.3.9
ENV M2_HOME /opt/jboss/tools/maven

ENV PATH $JAVA_HOME/bin:$M2_HOME/bin:$PATH


ENV KEYCLOAK_VERSION 1.9.1.Final
ENV KEYCLOAK_HOME /opt/jboss/keycloak
RUN cd $HOME \ 
  && curl -O http://downloads.jboss.org/keycloak/$KEYCLOAK_VERSION/keycloak-demo-$KEYCLOAK_VERSION.tar.gz \
  && tar xf keycloak-demo-$KEYCLOAK_VERSION.tar.gz \
  && mv $HOME/keycloak-demo-$KEYCLOAK_VERSION $KEYCLOAK_HOME \
  && rm keycloak-demo-$KEYCLOAK_VERSION.tar.gz

EXPOSE 8080

USER root

RUN yum -y install git \
    &&     yum clean all -y

USER jboss

# Switch to the working dir /opt/jboss
WORKDIR /opt/jboss

# Create directory tools for Maven download
RUN mkdir tools && cd tools

# Create /opt/jboss/tools/maven, download and extract Maven
RUN mkdir -p $M2_HOME && cd $M2_HOME && curl http://mirror.nbtelecom.com.br/apache/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz | tar zx --strip 1 -C $M2_HOME

RUN cd $HOME \ 
    && git clone https://github.com/aerogear/aerogear-backend-cookbook \
    && cd $HOME/aerogear-backend-cookbook/Shoot \
    && mvn clean install

RUN rm -rf $HOME/.m2

RUN mv $HOME/aerogear-backend-cookbook/Shoot/target/shoot.war $HOME/keycloak/keycloak/standalone/deployments/

RUN /opt/jboss/keycloak/keycloak/bin/add-user-keycloak.sh --user admin --password admin

CMD ["/opt/jboss/keycloak/keycloak/bin/standalone.sh", "-Dkeycloak.migration.provider=singlefile", "-Dkeycloak.migration.file=/opt/jboss/aerogear-backend-cookbook/Shoot/configuration/shoot-realm.json", "-Dkeycloak.migration.strategy=OVERWRITE_EXISTING", "-b", "0.0.0.0"]
