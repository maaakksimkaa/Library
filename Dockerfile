#образ взятый за основу
FROM openjdk:17
#Записываем в переменную путь до варника (необязательно)
ARG jarFile=target/library-0.0.2-SNAPSHOT.jar
#Куда переместить варник внутри контейнера
WORKDIR /opt/app
#копируем наш джарник в новый внутри контейнера
COPY ${jarFile} library.jar
#Открываем порт
EXPOSE 9090
#Команда для запуска
ENTRYPOINT ["java", "-jar", "library.jar"]
