install:
	mvn clean install -DskipTests -DskipDocker
deploy:
	mvn clean deploy -DskipTests
release:
	rm -f release.properties pom.xml.releaseBackup
	mvn release:prepare -B -Dresume=false -DskipTests
	rm -f release.properties pom.xml.releaseBackup
