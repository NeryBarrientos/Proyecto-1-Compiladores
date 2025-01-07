SET JAVA_HOME="C:\Program Files\Java\jdk-17\bin"
SET PATH=%JAVA_HOME%;%PATH%
SET CLASSPATH=%JAVA_HOME%;
SET CUP_HOME=C:\Fuentes
cd C:\Users\Pc2\Desktop\Usac\SegundoSemestre2024\Compi1\OLC1_Proyecto1_201807086\Proyecto1\src\Analizadores
java -jar %CUP_HOME%\java-cup-11b.jar -parser Parser -symbols Simbolos parser.cup
pause
