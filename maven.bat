@ECHO OFF

set M2_HOME="C:\personal\contact-tracing-be\apache-maven-3.8.1"
set M2_REPO="C:\personal\contact-tracing-be\repo"
set JAVA_HOME=C:\personal\contact-tracing-be\jdk

set LOCAL_REPO=%M2_REPO%
set MAVEN_OPTS=%MAVEN_OPTS%

set MAVEN=%M2_HOME%\bin\mvn -Dmaven.test.skip=true -T4

set argv=%1
set clean=false
set install=false

if not x%argv:c=%==x%argv% (set clean=true)
if not x%argv:i=%==x%argv% (set install=true)

if "%clean%"=="true" (

	echo *****************************
	echo [] Command CLEAN
	echo *****************************

	if "%2"=="" (
		call %MAVEN% clean
	) else (
		call %MAVEN% clean -pl %2 --also-make
	)
)
if "%install%"=="true" (

	echo *****************************
	echo [] Command INSTALL
	echo *****************************

	if "%2"=="" (
		call %MAVEN% install
	) else (
		call %MAVEN% install -pl %2 --also-make
	)
)