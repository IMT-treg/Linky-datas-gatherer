************************************
************************************
**Mode d'emploi - Test du logiciel**
************************************
************************************

Ce document a pour objectif de donner les différentes étapes pour tester la solution de récupération des données de téléinformation depuis un compteur Linky.

La première partie décrit les étapes à suivre pour tester la solution depuis un ordinateur en émulant les données reçues.

La deuxième partie décrit les étapes à suivre pour tester la solution avec un raspberry pi connecté à un compteur Linky


**************************************
*Partie 1 : Test depuis un ordinateur*
**************************************

Prérequis :
1- Créez un compte mongoDB atlas en suivant le tutoriel depuis le lien suivant : 
https://openclassrooms.com/fr/courses/6390246-passez-au-full-stack-avec-node-js-express-et-mongodb/6466348-configurez-votre-base-de-donnees
2- Téléchargez et installez le logiciel de simulation de port Virtual Serial Port Driver depuis le lien suivant :
https://www.virtual-serial-port.org/fr/downloads.html
3- Téléchargez et installez le logiciel de simulation d'envoi de données sur le port serial COM Port Data Emulator depuis le lien suivant : 
https://www.aggsoft.com/com-port-emulator/download.htm
4- Téléchargez et installez Node.JS depuis le lien suivant : 
https://nodejs.org/en/

Etapes à suivre :

1- Ouvrez le logiciel Virtual Serial Port Driver. Depuis l'onglet "Manage ports" paramétrez un lien entre deux ports 
qui ne sont pas utilisés par votre ordinateur (exemple COM 33 et COM 34) puis cliquez sur "add pair".
2- Ouvrez le logiciel COM Port Data Emulator. 
- Depuis l'onglet Device cliquez sur l'option Serial Port et paramétrez la de la façon suivante : 
	- Dans la première case indiquez le numéro du deuxième des deux ports paramétrés dans l'étapes 1 (par exemple en suivant l'exemple de l'étape 1 indiquez 34)
	- Dans la deuxième case rentrez "1200,7,E,2" qui correspond aux paramètres d'envoi d'un vrai compteur Linky
	- Dans la troisième case laissez "None"
- Depuis l'onglet Data Source :
	- Dans la case Data Source changez la valeur pour "Text file"
	- Dans le chemin du fichier allez chercher le fichier "log" qui est dans le dossier "ressources" à la racine du projet
	- Cliquez sur "Start" en bas à droite de la fenêtre
3- Ouvrez le projet "log&parsing" du dossier "brique_1" depuis un environnement de développement comme eclipse
- Dans le dossier "resources" du projet ouvrez le fichier "Raspberry_linky.xml" qui correspond aux options du logiciel.
- A la ligne 19 changez la value par le nom du premier port paramétré à l'étape 1 de ce tutoriel (par exemple en suivant l'exemple de l'étape 1 indiquer COM33)
- Executez le fichier "src/org.imt.atlantique.sss.upas.device.linky/RunLinky.java" depuis votre environnement de développement
- Dans la console vous devriez voir défiler des signes correspondants aux informations retournés par un compteur Linky
4- Ouvrez le projet "Database" depuis du dossier "brique_2" depuis un environnement de développement comme eclipse
- Si un problème apparaît avec les dépendances il faut savoir que dans mon projet j'ai une dépendance avec la bibliotheque mongoDB ainsi qu'avec la bibliotheque "upasClientAPI.jar" du projet "demoloIoTConnector" disponible dans la brique 2.
Ajoutez ces dépendances si ça ne fonctionne pas pour vous.
- Depuis le dossier "src/main/java/flo.teleinfo" ouvrir le fichier "MongoManager.java"
- A la ligne 35 remplacez le String existant par le chemin de votre cluster MongoDB sur atlas. Pour accéder à ce lien suivez la fin du tutoriel du prérequis n°1 en remplaçant nodeJS par Java
5- Ouvrez le projet "demoloIoTConnector" du dossier "brique_2" depuis un environnement de développement comme eclipse
- Depuis le dossier "src/org.imt.atlantique" du projet demoloIotConnector, executez le fichier IoTClient.java. Si l'execution ne fonctionne pas, essayez plusieurs fois à nouveau 
car parfois l'utilisation d'un port bloque l'execution de l'application.
- Dans la console vous devriez voir défiler des signes correspondants à l'enregitrement des données reçues dans votre base de données
- Vous pouvez voir vos données insérées dans votre base de données depuis votre tableau de bord atlas ou depuis le logiciel Compass si vous l'avez.
6- Ouvrir un terminal dans le dossier "Brique_3/Dashboard/backend"
- Y executer la commande "nodemon server"
- Vous devriez voir apparaître "Connexion à MongoDB réussie"
7- Dans le dossier "Brique_3/Dashboard/frontend" double-cliquez sur le fichier "index.html"
- Attendez quelques instants : vous devriez voir apparaître un dashboard avec à gauche la consommation en temps réel et à droite un historique de la consommation.
Les données de ce dashboard correpondent à celles remontées dans votre base de données mongoDB.


******************************************
*Partie 2 : Test depuis un compteur Linky*
******************************************

Prérequis :
1- Créez un compte mongoDB atlas en suivant le tutoriel depuis le lien suivant : 
https://openclassrooms.com/fr/courses/6390246-passez-au-full-stack-avec-node-js-express-et-mongodb/6466348-configurez-votre-base-de-donnees
2- Disposez d'un Raspberry Pi et d'un ordinateur connectés au même réseau
3- Réalisez le montage électronique correspondant à votre type de compteur Linky (Historique ou Standard) décrit dans le lien ci dessous
et connectez le d'une part à votre Raspberry et d'autre part à la prise de télé-information de votre compteur Linky
https://blog.bigd.fr/suivre-sa-consommation-electrique-edf/
4- Téléchargez et installez Node.JS depuis le lien suivant : 
https://nodejs.org/en/

Etapes à suivre
1- Installez la version graphique de l'OS Raspbian sur votre Raspberry
2- Ouvrez les ports GPIO et la connexion SSH de votre Raspberry depuis les options de configuration puis redémarrez votre Raspberry
3- Tester la bonne connexion à votre compteur linky en executant les commandes suivantes depuis un terminal : 
"stty -F /dev/ttyS0 1200 sane cs7 -crtscts" qui permet de paramétrer la vitesse de réception
"cat /dev/ttyS0" En executant cette commande vous devriez voir défiler les données de télé information de votre compteur Linky sur votre terminal
4- Ouvrez le projet "log&parsing" du dossier "brique_1" depuis un environnement de développement comme eclipse et exportez le comme "Runnable JAR file"
5- Déposez le fichier jar exporté sur votre Raspberry Pi à l'aide d'un outil comme MobaXterm
6- Depuis Eclipse Dans le dossier "resources" du projet "log&parsing" ouvrez le fichier "Raspberry_linky.xml" qui correspond aux options du logiciel.
- A la ligne 19 changez la value par le nom de votre port en série qui reçoit les informations du compteur Linky (normalement "/dev/ttyS0")
- Copiez le dossier "resources" dans le même répertoire que celui où vous avez déposé le fichier jar executable
- executez le fichier jar depuis votre Raspberry à l'aide de la commande "java -jar nomDuFichier.jar"
7- Ouvrez le projet "Database" depuis du dossier "brique_2" depuis un environnement de développement comme eclipse
- Si un problème apparaît avec les dépendances il faut savoir que dans mon projet j'ai une dépendance avec la bibliotheque mongoDB ainsi qu'avec la bibliotheque "upasClientAPI.jar" du projet "demoloIoTConnector" disponible dans la brique 2.
Ajoutez ces dépendances si ça ne fonctionne pas pour vous.
- Depuis le dossier "src/main/java/flo.teleinfo" ouvrir le fichier "MongoManager.java"
- A la ligne 35 remplacez le String existant par le chemin de votre cluster MongoDB sur atlas. Pour accéder à ce lien suivez la fin du tutoriel du prérequis n°1 en remplaçant nodeJS par Java
8- Ouvrez le projet "demoloIoTConnector" du dossier "brique_2" depuis un environnement de développement comme eclipse
- Depuis le dossier "src/org.imt.atlantique" du projet demoloIotConnector, executez le fichier IoTClient.java. Si l'execution ne fonctionne pas, essayez plusieurs fois à nouveau 
car parfois l'utilisation d'un port bloque l'execution de l'application.
- Dans la console vous devriez voir défiler des signes correspondants à l'enregitrement des données reçues dans votre base de données
- Vous pouvez voir vos données insérées dans votre base de données depuis votre tableau de bord atlas ou depuis le logiciel Compass si vous l'avez.
9- Ouvrir un terminal dans le dossier "Brique_3/Dashboard/backend"
- Y executer la commande "nodemon server"
- Vous devriez voir apparaître "Connexion à MongoDB réussie"
10- Dans le dossier "Brique_3/Dashboard/frontend" double-cliquez sur le fichier "index.html"
- Attendez quelques instants : vous devriez voir apparaître un dashboard avec à gauche la consommation en temps réel et à droite un historique de la consommation.
Les données de ce dashboard correpondent à celles remontées dans votre base de données mongoDB.
