### Springyne-timetable

Timetable scheduling application. 
Access published version via https://tomcat.akademijait.vtmc.lt/springyne/

### Run application

- Open back end directory springyne-timetable-api/ as maven project on IDE
- mvn clean install
- Run SpringyneTimetableApiApplication

- Open front end directory springyne-timetable-front/ on VSCode
- Change value of apiUrl to "http://localhost:8080" (comment like 42, uncomment line 43) in App.js file
- npm install
- npm start

Access app via http://localhost:3000/springyne

### Run application on tomcat locally

- Open front end directory springyne-timetable-front/
- npm install
- npm run build
- Copy contents of springyne-timetable-front/build to springyne-timetable-api/src/main/resources/public

- Open back end directory springyne-timetable-api/ as maven project on IDE
- mvn clean install
- mvn org.codehaus.cargo:cargo-maven2-plugin:1.7.7:run -Dcargo.maven.containerId=tomcat9x -Dcargo.servlet.port=8081 -Dcargo.maven.containerUrl=https://repo1.maven.org/maven2/org/apache/tomcat/tomcat/9.0.40/tomcat-9.0.40.zip
    
Access app via http://localhost:8081/springyne
