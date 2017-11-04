FROM arana198/baseimage:latest
MAINTAINER  Ankit Rana
EXPOSE  8001

RUN mkdir -p /opt/webapp/config
WORKDIR /opt/webapp

ADD entrypoint.sh /
RUN chmod a+x /entrypoint.sh

ADD application.properties /opt/webapp/config/
ADD marketplace.jar /opt/webapp/

ENTRYPOINT ["/bin/sh","/entrypoint.sh"]
