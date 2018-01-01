# Compilador de C (mini)
Es un compilador hecho en java con jflex para análisis lexico y cup para análisis sintatactico. Este compilador genera el código ensamblador para MIPS en un archivo `.asm`

# Pre-requisitos
Este proyecto fue hecho con netbeans, asi que es recomendado usar netbeans. En este repositorio se encuentran .jar necesarios para ejecutar el proyecto (jflex y cup)

# Antes de correr
Antes de poder correr este proyecto se debe ejecutar el siguiente comando en la terminal `sh build.sh` esto generará los archivos equivalentes en java. Convierte el archivo `c.flex` en dos clases de java, un `lexer` con el nombre definido en el archivo jflex y otra clase `symbols` que se genera apartir de los símbolos que ocupa el lexer. Tambien convierte el archivo `c.cup` en una clase de java, la cual está sería nuestra clase de análisis semántico. Tener en cuenta que siempre que se hacen cambios en cualquiera de estos dos archivos (flex y cup) se tiene que ejecutar el comando `sh build.sh`

# Al correr
Despues de ejecutar el comando `sh build.sh` ya se puede ejecutar normalmente el proyecto, este utilizará archivos predefinidos en el codigo para realizar los analisis y generar el codigo

# Estado del proyecto
Este proyecto fue realizado en la clase de compiladores I y II de UNITEC de Tegucigalpa, Honduras en el segundo semestre del año 2017.
Este proyecto realiza todas las fases de un compilador a excepcion de la optimizacion, se hizo lo posible para que por lo menos pudiera compilar un programa de C de hola mundo, el scanf y printf se usan de diferente forma que en el C normal.
Tambien se pueden realizar operaciones aritmeticas.
El manejo de saltos `IF WHILE FOR ELSE` solo se implementaron hasta su analisis sintáctico. El ambiente de las variables se maneja con arboles de tablas. Lo mas probable es que este proyecto se deje abandonado y no se termine. 

# Mini-C
`scanf` solo recibe una variable, la cual es la que se va a leer de la consola. Y `printf` solamente puede recibir texto y un entero. El siguiente codigo de nuestro C funciona perfectamente y genera el archivo ensamblador para MIPS correctamente.
```C
int main(){
    int x;
    printf("Ingrese un numero: ");
    scanf(x);
    x = x * 2;
    printf("El doble de su numero es: %d",x);
    return 0;
}
```
