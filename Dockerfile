# USAMOS TOMCAT 10.1 con JAVA 21
# Esto es obligatorio porque usas Jakarta EE 10 en tu pom.xml
FROM tomcat:10.1-jdk21-openjdk-slim

# Limpiamos las apps por defecto
RUN rm -rf /usr/local/tomcat/webapps/*

# Copiamos el archivo.
# Asegúrate de haber hecho el cambio de <finalName>dogami</finalName> en el pom.xml
# Si no lo cambiaste, esta línea debe decir COPY target/project.war ...
COPY target/dogami.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]