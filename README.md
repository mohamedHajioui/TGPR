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

  * La touche Enter fonctionne pas sur le UC View_Instances
  * le guest peut se login,mais le questionnaire est toujours en not started 
  * Attention dans le View_forms,page up page down fonctionne bien SAUF lorsque on se place sur open ou manage, auquel cas l'app crash...
  * 
  * 

### Liste des fonctionnalités supplémentaires

- Sur le Uc View_edit_instance : - on ne peut pas fermer une instance sans repondre sur une question required.
                                 - on ne peut pas passer a la suivante si on n'a pas repondu.


### Divers
UC developpées par chacun :

Stitou Hamza : login,add_edit_question,manage_option_lists

Mohamed El Hajioui Khattouti : view_forms,signup,analyse

Cihan Aslan : Add_edit_form, add_edit_optionlists

Aly Samir Mahmoud : view_form, manage_shares

Amine Ihdene : view_edit_instance, view_instances

