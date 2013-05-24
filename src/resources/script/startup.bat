@ECHO OFF

    REM 设置批量CLASS路径
    SET PRJ_HOME=D:\ccbprize

    SET classpath=%JAVA_HOME%\jre\lib\alt-rt.jar;%JAVA_HOME%\jre\lib\charsets.jar;%JAVA_HOME%\jre\lib\rt.jar

    SET classpath=%CLASSPATH%;%PRJ_HOME%\apps\ccbprize

    FOR %%i IN (%PRJ_HOME%\lib\*.*) do call %PRJ_HOME%\bin\cpappend.bat %%i

    SET CLASSPATH

    JAVA prize.PosServer
