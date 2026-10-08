# Service de tele-expertise

Structure Maven avec T3 implemente : entite Specialiste, enum Specialite,
repository JPA et script SQL de demonstration. Les endpoints restent a implementer.

## Stack

Java 17, Jakarta EE, Jersey/Jackson, Hibernate/JPA, bcrypt, packaging WAR pour Tomcat 10.1.
Modules Jersey : https://eclipse-ee4j.github.io/jersey.github.io/documentation/latest31x/modules-and-dependencies.html

## Organisation

- `config` : configuration JAX-RS, prefixe `/api`.
- `resource` : futures resources REST.
- `service` : future logique metier.
- `repository` : futur acces aux donnees.
- `entity` : futures entites JPA.
- `enums` : futurs roles, specialites, priorites et statuts.
- `dto` : futurs corps de requetes et reponses.
- `security` : futur filtre HTTP Basic et SecurityContext.
- `exception` : future gestion des erreurs HTTP.
- `src/main/resources/META-INF/persistence.xml` : emplacement JPA a configurer.
- `src/main/resources/sql/seed.sql` : emplacement du script des comptes.
- `src/test/java` : emplacement des futurs tests.
- `postman` : emplacement de la collection et de son environnement.

## Compilation

```shell
mvn clean package
```

Le fichier genere est `target/tele-expertise.war`.
Avant de developper la persistance, choisir le pilote JDBC et configurer la base partagee du brief 1.

## T3 : specialistes et comptes

`Specialiste` reference un `Utilisateur` par une relation un-a-un. Les comptes
contiennent le nom, l'email unique, le hash bcrypt et le role (`MEDECIN` ou
`SPECIALISTE`). La specialite est stockee comme texte (`CARDIOLOGIE`,
`DERMATOLOGIE`, `NEUROLOGIE`, `PEDIATRIE`, `RADIOLOGIE`).

`SpecialisteRepository` recoit un `EntityManager` dans son constructeur et propose
`findById`, `findAll`, `findBySpecialite` et `save`. Le service appelant gere la
transaction pour `save`; le compte utilisateur doit deja etre persiste.

### Base de donnees

Il n'est pas necessaire de changer de base. Le script
`src/main/resources/sql/seed.sql` cible MySQL 8 et ajoute les tables
`utilisateurs` et `specialistes` dans la base `clinique`. Si votre base possede
deja une table de comptes, adaptez les noms/colonnes et les annotations JPA avant
d'executer le script : `CREATE TABLE IF NOT EXISTS` ne modifie pas une table existante.

Dans MySQL Workbench, executez le script : il selectionne la base existante avec
`USE clinique;`. Il conserve les comptes deja presents lors d'une nouvelle
execution. Il fournit trois comptes de demonstration, tous avec le mot de passe
`Demo123!` : `medecin@demo.test`, `cardiologue@demo.test` et `dermatologue@demo.test`.
Les mots de passe sont stockes sous forme de hashes bcrypt avec des sels distincts.

La connexion de l'application reste a configurer : `persistence.xml` est encore
vide et aucun pilote JDBC n'est choisi dans `pom.xml`. Pour MySQL, ajouter le
pilote JDBC puis configurer l'unite de persistance, l'URL de la base existante et
ses identifiants. Ce script n'est pas execute automatiquement au demarrage.
