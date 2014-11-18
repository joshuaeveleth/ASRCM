@echo off

SETLOCAL ENABLEEXTENSIONS

IF DEFINED JAVA_HOME (
    ECHO "JAVA_HOME must be defined."
    goto End
)

START /WAIT mysql-installer-web-community-5.6.21.1.msi /quiet

"C:\Program Files (x86)\MySQL\MySQL Installer for Windows\MySQLInstallerConsole.exe" install -silent server;5.6.21;X64:*:serverid=0:type=user;username=asrcadmin;password=AsrcAdminDb;role=DBManager

START /WAIT glassfish-3.1.2.2-web-windows.exe -j "%JAVA_HOME%" -a glassfish3_asrc.cfg -s

REM Change directory to the Glassfish install directory.
C:
cd \asrc\glassfish3\glassfish

REM Create a Windows Service to auto-start Glassfish.
bin\asadmin create-service

REM Start the Windows service.
sc start domain1

:End
