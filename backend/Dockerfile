FROM openjdk
WORKDIR /usr/myapp
COPY backend/src/main/resources/logging.properties /usr/myapp/logging.properties
COPY lib/gson-2.13.2.jar /usr/myapp/lib/gson-2.13.2.jar
COPY lib/fastcgi-lib.jar /usr/myapp/lib/fastcgi-lib.jar
COPY backend/src /usr/myapp/src
RUN javac -cp lib/fastcgi-lib.jar:lib/gson-2.13.2.jar -d bin src/main/java/web/*.java src/main/java/web/*/*.java
EXPOSE 1009
CMD ["java", "-DFCGI_PORT=1009", "-Djava.util.logging.config.file=/usr/myapp/logging.properties", "-cp", "lib/fastcgi-lib.jar:lib/gson-2.13.2.jar:bin", "web.App"]
