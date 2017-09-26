FROM jboss/base-jdk:8

ENV MAVEN_OPTS -Xmx768m -XX:+UseConcMarkSweepGC -XX:MaxPermSize=128m -XX:+CMSClassUnloadingEnabled
ENV MAVEN_VERSION 3.5.0
ENV M2_HOME /opt/jboss/tools/maven
ENV HOME /opt/jboss
ENV PATH $JAVA_HOME/bin:$M2_HOME/bin:$PATH

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

WORKDIR $HOME/aerogear-backend-cookbook/Shoot

EXPOSE 8080
EXPOSE 9990
EXPOSE 8443

CMD ["mvn", "wildfly-swarm:start"]

