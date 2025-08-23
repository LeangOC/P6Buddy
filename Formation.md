### Modèlisez vos bases de données : MCD, MLD, MPD
- MCD : Modèle Conceptuel des données
      * Concepts ( film,réalisateurs....)
      * Associations (film PRODUIT par une société, réalisteur REALISE un film....)
      * Multiplicité ( 0,1,*...)
  On utilise le diagramme de classe qui fait partie du langage de modélisaton UML.
  Un rectangle divisés en trois parties: Concept, Attibut,Méthode:
      * Concept : convention Upper Camel Case
      * Attribut : convention Lower Camel Case
                  typé (date,texte...)
                  domaine : l'ensemble de valeurs , extension (list finie )  ou intention ( date,composite ...)
      * Méthode : modifier les attributs, calculer un résultat ou les deux.
                  
                   
    
    
    - MLD : Modèle Logique  de données.
    - MPD : Modèle Physique de données.

### Spring Data JPA
Pour pouvoir faire communiquer l'application Java avec la base de données Mysql, il nous faut 
le Spring Data JPA et API Mysql.


