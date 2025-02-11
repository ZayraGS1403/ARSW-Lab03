# Laboratorio Sincronización de Hilos y DeadLocks 03

**Integrantes:** Sebastián Cardona, Laura Gil, Zayra Gutiérrez  
**Profesor:** Javier Toquica Barrera  
**Universidad:** Escuela Colombiana de Ingeniería Julio Garavito  


## Contenido
1. [Introducción](#introducción)
2. [Parte I – Control de hilos con wait/notify. Productor/consumidor.](#parte-i--control-de-hilos-con-waitnotify-productorconsumidor)
3. [Parte II – Sincronización y Dead-Locks.](#parte-ii--sincronización-y-dead-locks)
4. [Conclusiones](#conclusiones)

## Introducción
Este laboratorio aborda la sincronización de hilos en aplicaciones concurrentes, evitando problemas como condiciones de carrera y deadlocks. Se explorará el patrón Productor-Consumidor con `wait()` y `notifyAll()`, y la simulación de batallas en "Highlander Simulator" para analizar bloqueos e inconsistencias. 

## Desarrollo del Laboratorio
### Parte I – Control de hilos con wait/notify. Productor/consumidor.
1. **¿Cómo afecta el consumo de CPU la falta de sincronización en el Productor-Consumidor?**  
   - El `Consumer` entra en un bucle infinito sin pausas cuando la cola está vacía, lo que incrementa el uso de CPU. Además, el `Producer` agrega elementos sin control, causando un uso excesivo de memoria.
     ![image](https://github.com/user-attachments/assets/d8c33411-d900-4661-9928-5fa352114a3b)


2. **¿Cómo optimizar el uso de CPU implementando `wait()` y `notifyAll()`?**  
   - Implementando `wait()` para que el consumidor espere cuando la cola esté vacía y `notifyAll()` para avisar cuando haya cambios. Esto reduce el uso de CPU y mejora la eficiencia del sistema.
     ![image](https://github.com/user-attachments/assets/0ddeb87d-d3ed-48ca-907e-7ec2bf299789)


3. **¿Cómo garantizar un límite de stock en la cola sin alto consumo de CPU?**  
   - Se estableció un `stockLimit` para que el productor espere cuando la cola esté llena y el consumidor la vacíe. Se utilizó una colección que respete el límite, manteniendo el consumo de CPU bajo.
     ![image](https://github.com/user-attachments/assets/7ba003f2-a4e6-4e09-8b15-14abef1fad25)


### Parte II – Sincronización y Dead-Locks.
1. **¿Cómo se implementa la pelea entre inmortales en `highlander-simulator`?**  
   - Cada jugador ataca a otro, reduciendo su salud y aumentando la propia. El juego puede volverse infinito si quedan solo dos inmortales.

2. **¿Se cumple el invariante de salud de los jugadores?**  
   - No, la suma de salud de todos los jugadores varía debido a condiciones de carrera.

3. **Ejecute la aplicación y verifique cómo funcionan las opciones ‘pause and check’. ¿Se cumple el invariante??**  
   - No, no se cumple la invariante, en varios casos se ve que la invariante es menor o es mayor a la que debería ser.
     
     ![image](https://github.com/user-attachments/assets/e4c69581-6a90-4350-b4a6-b4dba52a40ff)

5. **Verifique nuevamente el funcionamiento (haga clic muchas veces en el botón). ¿Se cumple o no el invariante?**
   
   La invariante se cumple
   ![image](https://github.com/user-attachments/assets/58c47735-ba54-411b-9fcc-7d5195edd644)

7. **¿Cómo implementar una estrategia de bloqueo para evitar inconsistencias en `fight()`?**  
   - Se usó `synchronized` anidado con un orden fijo en la adquisición de bloqueos para evitar interbloqueos.

8. **¿Cómo identificar y corregir un interbloqueo usando `jps` y `jstack`?**  
   - Se identificó que varios hilos estaban en `waiting for monitor entry`, indicando un deadlock. Se corrigió asegurando un orden de adquisición de bloqueos.
     ![image](https://github.com/user-attachments/assets/237fcf9b-dcea-4857-9699-b8ce2660836b)
     ![image](https://github.com/user-attachments/assets/f954345c-8d17-45a3-8743-d267630e963a)

     
9. **Plantee una estrategia para corregir el problema**
   
   Se estableció un orden consistente de adquisición de bloqueos, asegurando que siempre se adquieran en el mismo orden basado en el nombre del hilo, tambien
se añadió un mecanismo para pausar y reanudar los hilos de forma segura mediante wait() y notifyAll(). Esto evita interrumpir los hilos abruptamente y permite un control más estable de la ejecución. Por último, se implementó pauseLock.wait() para suspender los hilos de forma segura y notifyAll() para reanudarlos.
Para evitar condiciones de carrera, se usó una lista sincronizada con Collections.synchronizedList(), y se protegió el acceso a la lista con synchronized.

10. **rectifique que el programa siga funcionando de manera consistente cuando se ejecutan 100, 1000 o 10000 inmortales. Si en estos casos grandes se empieza a incumplir de nuevo el invariante**  

N=100
	![image](https://github.com/user-attachments/assets/a6042a72-332a-4fe1-be25-846e6df5ef1e)

Efectivamente la invariante se cumplió.

N=1000
	![image](https://github.com/user-attachments/assets/eaca6463-b03f-4791-a90b-ab1e54f68d7a)

De igual forma la invariante se cumple.

N=10000
	![image](https://github.com/user-attachments/assets/aa42ea48-dfb1-447b-9f86-9bb7f1ca78d1)

También se cumple la invariante para este caso.


11. **¿Cómo implementar la opción `STOP`?**  
   - Se agregó un mecanismo para detener y reiniciar la simulación sin afectar la estabilidad del sistema.
     ![image](https://github.com/user-attachments/assets/39b32f1b-07aa-44b6-9446-b179115cd353)
     ![image](https://github.com/user-attachments/assets/34fcedd3-d049-4405-9dad-6054914004c3)

	

## Conclusiones
- La sincronización adecuada mejora el rendimiento y evita el uso innecesario de CPU y memoria.
- La falta de control en la concurrencia genera inconsistencias en los datos compartidos.
- Un orden seguro en la adquisición de bloqueos es clave para evitar deadlocks.
