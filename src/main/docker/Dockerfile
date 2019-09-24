FROM tomcat:9.0.26-jdk8-openjdk
LABEL maintainer="<Rodrigo Leme> rmleme@gmail.com"
LABEL vendor="LuizaLabs"

ADD customer-api.war /usr/local/tomcat/webapps/

ENV JAVA_OPTS ""

CMD ["catalina.sh", "run"]