### Modèlisez vos bases de données : MCD, MLD, MPD
- MCD : Modèle Conceptuel des données    
  - Concepts ( film,réalisateurs....)  
  - Associations (film PRODUIT par une société, réalisteur REALISE un film....)  
  - Multiplicité ( 0,1,*...)
    
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
le Spring Data JPA et le driver Mysql.  

Sans le Spring Data JPA:  
  
<img width="286" height="229" alt="image" src="https://github.com/user-attachments/assets/68e4b95f-0e83-4fa2-a0ae-89d2dc74d5d0" />  

Avec le Spring Data JPA :  
<img width="393" height="158" alt="image" src="https://github.com/user-attachments/assets/d9ebff66-6750-4d36-afc8-095a4e475650" />  

Possibilité d'externaliser le fichier configuration application.properties ou d'utiliser la variable système ( ex:mot de passe )   


