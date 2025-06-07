## Problemas detectados:
- En ocasiones el programa puede tardar en responder al logearse, tener paciencia con eso.
- Gran parte de los posters de peliculas proveidos por la data de donde fue extraida puede no existir o ser erronea.
---
## Acceso a la DB
- En este caso, la DB usada es una DB alojada externamente en el servicio gratuito para Postgre, Render. Si desea usar la misma DB, debajo están los accesos:

    - Host: dpg-d0vsb8emcj7s73fuf6c0-a.oregon-postgres.render.com
    - Port: 5432
    - User: dialcadev
    - Contraseña: Zc5uZf1SdFjWLUC8q4DxzIVLfIRcwtAR
    - Nombre DB: dialcaflix_g3qt
    - URL: jdbc:postgresql://dpg-d0vsb8emcj7s73fuf6c0-a.oregon-postgres.render.com:5432/dialcaflix_g3qt
- Si desea crear la DB desde 0, el código SQL se encuentra en la raíz del proyecto/sql/dialcaflix.sql
---
## Ejecución del proyecto
- El proyecto puede ser corrido en Netbeans perfectamente al seleccionarlo como proyecto para abrir, si se desea se puede ejecutar desde ahí.
- La opción más rapida y recomendable para ejecutar el proyecto es: 
  - Correr el proyecto ejecutando el .jar del proyecto en la carpeta /dist
  - Si no funciona lo anterior pruebe ejecutando el .bat en la misma ubicación.