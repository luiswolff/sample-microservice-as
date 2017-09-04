Microservice-Applicationserver
===

Ein Beispiel-Projekt, was zeigt, wie nach meinem Verständis eine Microservice-Architektur mit hilfe von Application-Servicer umgesetzt werden kann. 

Motivation
---

Dieses Projekt ist im Rahmen meiner Master-Thesis entstanden.
Dabei sollte Untersucht werden, wie Microservices miteinander und evtl. mit einen existierenden Monolithen integrieren können.
Die hier vorgestellte Lösung stellt ein mögliches Vorgehen da.

Die Master-Thesis wurde von der [BioArtProducts GmbH](http://www.bioeng.de) betreut.
Entsprechend richtet sich die Fachlichkeit dieser Beispielanwendung an der Branche dieses Unternehmens aus.
Es wird eine Art Mini-Patienten-Register umgesetzt.
Es ist möglich anonyme Patienten zuerfassen und abzurufen.
Für jeden Patienten können zusätzlich Mediaktionen und Diagnosen gespeichert werden.
    
Umsetzung
---

Das Projekt definiert zunächst zwei (Micro-) Services.
Der [Backend-Service](backend) definiert eine Schnittstelle zu einer Datenbank.
Er ist für das Speichern und Auslesen der Patientendaten zuständig.
Außerdem gibt es noch den [Frontend-Service](frontend), welche eine Darstellung für einen Webbrowser generiert.
Für die Verarbeitung von Nutzereingaben wird auf die Schnittstellen des Backends zugegriffen.

Dieses Projekt generiert Web-Archive-Dateien (WAR).
Demzufolge wird ein Anwendungsserver benötigt.
Der Vorteil bei diesem Vorgehen ist, das nur wenige Kilobyte große Deployment-Artefakte erzeugt werden.
Auch werden weniger Ressourcen (Zeit, Speicher, etc) beim Bild benötigt.
Dieses Deployment-Model wird von [Adam Bien](http://about.adam-bien.com) auch als [Thin-WAR](http://adambien.blog/roller/abien/entry/think_wars_high_productivity_and) bezeichnet.

Ein alternatives Vorgehen wäre das Erstellen von soganannten Fat- oder Uber-JARs.
Dies wird beispielsweise von [Eberhard Wolff](http://ewolff.com) bevorzugt, welcher daher schon zweimal [das Aus der Anwenndungsserver prophezeit](https://jaxenter.de/zwei-jahre-nach-dem-tod-der-java-application-server-leben-totgesagte-laenger-38603) hat.
Allerdings benötigen Fat-JARs beim Build mehr Ressourcen und die Services müssen in separaten Prozessen betrieben werden.
Der große Vorteil ist aber, dass die Anwendung an sich leichter Übertragen werden kann, da die gesamte Infrastruktur in der ausgelieferten JAR enthalten ist.
So wäre es beispielsweise nicht mehr notwendig, dass ein Anwendungsserver vor der Inbetriebnahme konfiguriert werden muss.
Aus diesem Grund habe ich auch ein [alternatives Projekt](https://github.com/luiswolff/sample-microservice-sa) entwickelt, was den hier beschriebenen Anwendungfall unter Verwendung von Standalone-Deployments demonstriert.

Ich persönlich denke, dass die Entscheidung für oder gegen eine bestimmtes Vorgehensweise nicht so pauschal getroffen werden kann.
Sowohl Fat-JARs als auch Thin-WARs haben je nach Einsatzszenario ihre jeweiligen Vorteile.
Soll eine Anwendung durch ein DevOps-Team entwicket werden, denke ich, dass es tatsächlich besser ist, wenn die gesamte Infrastruktur in einen Deployment enthalten ist.
Klassische Vorgehensweisen, bei dem ein Team die Infrastruktur betreut und ein anderes die Anwendungentwickelt, machen dagegen WAR- oder EAR-Anwendungen sinvoller.
Allerdings glaube ich, dass wenn [Docker](https://www.docker.com) eingesetzt werden soll, gerade das Thin-WAR-Deployment seine Vorteile ausspielen kann, da so die letzte Schicht des Docker-Image nur sehr klein ist.

Build
---

Bei dem Projekt handelt es sich um eine [Apache Maven](https://maven.apache.org)-Anwendung.
Es werden zwei Module definiert, die als WAR gepackt werden.
Beide Services können erstellt werden, indem im Projektverzeichnes der Befehl `mvn package` ausgeführt wird.
    
Abhängigkeiten
---

Beide Services benötigen folgende Umgebung, um betrieben werden zu können.

* **Java 8**: Da das Projekte mit Java 8 kompiliert wird, muss auch die Laufzeitumgebung hierauf aufbauen. 
* **Java EE 7 (Web Profile)**: Um das Projekt zu starten muss ein Java EE 7 konformer Anwendungsserver verwendet werden.
Getestet und Entwickelte wurde es auf dem [Glassfish 4](https://javaee.github.io/glassfish/).

**Hinweis:** Für Testzwecke empfehle ich die Verwendung des [Glassfish 4.1](http://download.oracle.com/glassfish/4.1/release/index.html).
Nach meiner Erfahrung ist dies die letzte Version, die wirklich stabil läuft.
Bei den folgenden Versionen, 4.1.1 und 4.1.2, gibt es einen Fehler im EclipseLink-Moxy Modul, durch den Abhängigkeit nicht richtig aufgelösst werden.
Dadurch kommt es zu einer Exception, wenn eine Anwendung versucht ein Java-Objekt ins JSON-Format zu serialisieren.

Start und Deployment
---

Dadurch, dass durch den Build WAR-Dateien erzeugt werden, können diese auch auf jeden JavaEE7-Zertifizierten Anwendungsserver deployed werden.
Der Glassfish bietet hierfür ein Kommandozeilen-Interface oder das Autodeploy-Verzeichnis an.
Es ist auch kein Problem beide Services auf einen Anwendungsserver zu betreiben.
Zwar liegt so keine echte Microservice-Architektur mehr vor, aber es ist aus meiner Sicht ein leicht anzuwendender Modularisierungsansatzt, der Microservices in weiten Teilen ähnlich ist.
Allerdings muss gesagt werden, dass so kein flexibles skalieren möglich ist, weshalb diese Betriebsform nur in betracht kommen sollte, wenn die Skalierbarkeit kein wichtiges Kriterierum ist.

Das Backend-Projekt benötigt eine Anbindung zu einer Datenbank.
Dadurch, dass JPA als Schnittstellen verwendet wird und keine nativen Abfragen durchgeführt werden, ist dass verwendete RDBMS theoretisch egal.
JPA ist so konfiguriert, dass die von Anwendungsserver bereitgestellte DataSource verwendet wird.
Sie sollte unter den JNDI-Namen `jdbc/__default` erreichbar sein.
Auch wird JPA beim Start der Anwendung versuchen die für die Anwendung benötigten Tabellen zu erzeugen, sollten sie nicht bereits existieren.

Das Frontend-Projekt verwendet eine Kombination von JAX-RS und JSP, um die Oberfläche zu generieren.
Hierfür wird die Servlet-API mit Request-Forwarding benutzt, um die Rendering-Engine zu aktivieren.
Bestimmte Anwendungsserver, wie z.B. der [Wildfly](http://wildfly.org/), verbieten diese Aktion, da die JAX-RS-Implementierung (bei Wildfly ist es RestEasy) einen nicht-standartisierten Request-Wrapper zu Verfügung stellt.
Daher ist es notwendig solche Wrapper in der Konfiguration des Servers expliziert zu erlauben.
Für Wildfly kann folgende CLI-Anweisung verwendet werden:

``
    /subsystem=undertow/servlet-container=default/:write-attribute(name=allow-non-standard-wrappers, value=true)
``

**Wichtig:** Bitte auch beachten, dass das Attribute im Richtigen Servlet-Container geschrieben wird!!!
**Hinweis:** Ein Versuch hat gezeigt, dass der vorliegende Code mit den Wildfly-Servicer nicht kompatibell ist.
    
Autor und Datum
---

Luis-Manuel Wolff Heredia

04.09.2017