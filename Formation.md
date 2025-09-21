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

### WCAG
Les WCAG (Web Content Accessibility Guidelines), ou en français Règles pour l'accessibilité des contenus Web,  
sont un ensemble de normes internationales conçues pour rendre le web accessible à tous,  
notamment aux personnes en situation de handicap.
✅ Les 4 grands principes WCAG (POUR) :
1. Perceptible
Les utilisateurs doivent pouvoir percevoir l'information (texte, images, vidéos, sons, etc.).
Exemples :
Fournir du texte alternatif (alt) pour les images.
Offrir des sous-titres pour les vidéos.
Utiliser un contraste suffisant entre texte et arrière-plan.

2. Opérable
Les utilisateurs doivent pouvoir naviguer et utiliser l'interface.
Exemples :
Le site doit pouvoir se naviguer au clavier (sans souris).
Donner suffisamment de temps pour lire ou interagir.
Éviter les éléments qui causent des crises (comme les animations clignotantes).

3. Compréhensible
Le contenu et le fonctionnement du site doivent être faciles à comprendre.
Exemples :
Utiliser un langage clair et simple.
Les liens et boutons doivent être intuitifs (ex. "Envoyer" plutôt que "Cliquez ici").
Fournir des messages d’erreur clairs dans les formulaires.

4. Robuste
Le contenu doit être compatible avec différents outils d’assistance (lecteurs d’écran, navigateurs, etc.).
Exemples :
Utiliser un code HTML/CSS propre et structuré.
S'assurer que le site fonctionne avec les technologies d’assistance actuelles et futures.

Exemple : Afficher un message d'erreur en rouge lors d’une authentification incorrecte peut contribuer  
à l’accessibilité, mais ce n’est pas suffisant à lui seul pour respecter les standards WCAG.

Ce que demandent les WCAG (niveau A et AA) :
1. Informer clairement l'utilisateur de l'erreur
Avec un texte explicite : "Mot de passe ou identifiant incorrect. Veuillez réessayer."

2. Utiliser autre chose que la couleur seule pour signaler l’erreur
Couleur + icône (ex : ⚠️), ou
Couleur + bordure/encadré, ou
Couleur + texte en gras ou souligné

3. Le message doit être lisible par les technologies d’assistance
Par exemple, le message d’erreur doit être dans une balise ARIA role="alert" ou rendu focalisable  
pour que les lecteurs d’écran le détectent.

<div role="alert" style="color: red;">
  ⚠️ Identifiant ou mot de passe incorrect. Veuillez réessayer.
</div>

Le message est :
  Clair
  Accessible au lecteur d’écran (avec role="alert")
  Pas uniquement rouge, il y a aussi une icône et un texte explicite