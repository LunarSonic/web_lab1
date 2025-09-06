FROM openjdk
WORKDIR /usr/myapp
COPY lib/fastcgi-lib.jar /usr/myapp/lib/fastcgi-lib.jar
COPY backend/src /usr/myapp/src
RUN javac -cp lib/fastcgi-lib.jar -d bin src/main/java/web/*.java
EXPOSE 1009
CMD ["java", "-DFCGI_PORT=1009", "-cp", "lib/fastcgi-lib.jar:bin", "web.App"]
