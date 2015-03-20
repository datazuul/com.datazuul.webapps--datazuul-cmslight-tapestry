pixoCMS light 1.1
=================

release
-------
1.2, 01.11.2011	migration to maven
1.1, 27.01.2008
1.0, 17.08.2005

description
-----------
pixoCMS light is a easy to use and easy to install online editor and file manager for
managing and editing the files of one or more website(s).
provides template-directory, editing, (recursive) deleting, creating, uploading and downloading of files.

it is not using WYSIWYG, so all text-files can be edited.
provides live preview on webserver. all text-files (html, shtml, php, js, css) can be edited.

perhaps in the future a full blown database-WYSIWYG-multi-role CMS called "pixoCMS" rises...
but this is not the target of pixoCMSlight.

technics
--------
- Java 1.4 SDK
- Tapestry 3.0
- Jakarta Tomcat
- no database (readonly XML-file for userdata)

installation
------------
- download pixoCMSlight-x.x.war
- rename it to pixoCMSlight.war
- drop pixoCMSlight.war into tomcat
- add your websites/users to WEB-INF/database.xml
- edit context/index.html to use correct redirect-url
- edit src/java/log4j.properties for logging preferences
- restart tomcat, call http://{localhost:8080}/pixoCMSlight/app

new in this release
-------------------
+ sortable table in "DateiManager"-page (using contrib-table)

TODO
----
- file watchdog for reloading manual edited "database.xml"
  (so no more tomcat restart after editing is needed)
- moving files/directories
- more languages
- make pixocms.js international

development
-----------
developed with eclipse 3.1.0, sysdeo tomcat-plugin and tapestry spindle-plugin
- source folders on build path: src/java
- default output folder: pixoCMSlight/context/WEB-INF/classes
- libraries (most under WEB-INF/lib)
  check the library-path to servlet.jar coressponding ".../common/lib" path of your tomcat installation   
- tomcat under port 18080 (user level tomcat...), you may use 8080...

feedback and developers are welcome,
Ralf Eichinger, ralf.eichinger@pixotec.de, http://www.datazuul.com