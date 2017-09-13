FROM fedora:26

RUN dnf install -y java wget
RUN wget https://downloads.jboss.org/keycloak/3.2.1.Final/keycloak-3.2.1.Final.tar.gz
RUN tar -zvxf keycloak-3.2.1.Final.tar.gz

ADD configuration/realm.json /keycloak-3.2.1.Final/bin

EXPOSE 8080
EXPOSE 9990
EXPOSE 8443

WORKDIR /keycloak-3.2.1.Final/bin

CMD ["./standalone.sh", "-b=0.0.0.0", "-Dkeycloak.migration.action=import", "-Dkeycloak.migration.provider=singleFile",  "-Dkeycloak.migration.file=realm.json", "-Dkeycloak.migration.strategy=IGNORE_EXISTING"]

