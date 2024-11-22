# Projet tgpr-2425-a01 - MyForms

## Notes de version

### Liste des utilisateurs et mots de passe

  * login, password, role
  * A compléter... 

###  IMPORTANT ###
 ***  Pour que la fonctionnalite du Guest fonctionne sur View_Edit_Instance 
      il faut supprimer l'instance de la base de donnees, 
      l'instance y'est deja dans la base de donnees
      ( c'est pour ca qu'elle saffiche this instance exists for the guest)
       mais les forms sont en not started *****

#### FIN IMPORTANT ###


### Liste des bugs connus

view forms : PgUp et PgDn


view_edit_instance : instance pas créée à l'ouverture, ni quand on répond aux
questions
submit again : pas d'instance créée en BD, mais réponses
mises sur la 7
ne réouvrent pas l'instance en cours mais créent une
nouvelle à chaque fois
r/o : check : valeurs en dessous des autres
stocke pas les idx des option list et un seul record pour une
seule option
guest : on l'empèche de répondre s'il y a déjà des instances
=> pas d'accord avec remarque dans le README : il faut tjrs
créer une nouvelle instance pour guest
si erreur, bloque le next au lieu d'afficher l'erreur : c'est un
bug et pas une fcté complémentaire


view_instances : enter ne marche pas

### Liste des fonctionnalités supplémentaires


### Divers
UC developpées par chacun :

Stitou Hamza : login,add_edit_question,manage_option_lists

Mohamed El Hajioui Khattouti : view_forms,signup,analyse

Cihan Aslan : Add_edit_form, add_edit_optionlists

Aly Samir Mahmoud : view_form, manage_shares

Amine Ihdene : view_edit_instance, view_instances

