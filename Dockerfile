FROM openjdk:10-slim
MAINTAINER  Ankit Rana
EXPOSE  8001

RUN mkdir -p /opt/webapp/config
WORKDIR /opt/webapp

ADD docker/entrypoint.sh /
RUN chmod a+x /entrypoint.sh

ADD docker/application.properties /opt/webapp/config/

ONBUILD RUN ./gradlew clean build
ADD build/libs/marketplace.jar /opt/webapp/

ENTRYPOINT ["/bin/sh","/entrypoint.sh"]
