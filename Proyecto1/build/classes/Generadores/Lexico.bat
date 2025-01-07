SET JAVA_HOME="C:\Program Files\Java\jdk-17\bin"
SET PATH=%JAVA_HOME%;%PATH%
SET CLASSPATH=%JAVA_HOME%;
SET JFLEX_HOME=C:\jflex-1.9.1\lib
cd C:\Users\Pc2\Desktop\Usac\SegundoSemestre2024\Compi1\OLC1_Proyecto1_201807086\Proyecto1\src\Analizadores
java -jar %JFLEX_HOME%\jflex-full-1.9.1.jar lex.flex
pause