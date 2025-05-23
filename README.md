#  UBGarden – Sauve le hérisson !

Un pauvre hérisson s’est perdu dans une série de **jardins enchantés**... Incarnez un **jardinier courageux** et partez à sa recherche à travers un monde plein de surprises et de dangers.

## Présentation

**UBGarden** est un jeu d’aventure en Java basé sur la programmation orientée objet. Le joueur doit retrouver un hérisson caché dans des cartes rectangulaires en surmontant divers obstacles : insectes dangereux, terrain difficile, et gestion d'énergie.

Ramassez des carottes pour ouvrir les portes, évitez ou neutralisez les guêpes et frelons, et gérez votre énergie pour survivre.

## Objectifs du jeu

- Explorer les jardins
- Ramasser tous les **bonus** (pommes, bombes...)
- Éviter ou neutraliser les **insectes** (guêpes et frelons)
- Réussir à atteindre le **hérisson** pour gagner

## Commandes principales

- **Déplacement** : Le jardinier se déplace dans les 4 directions (haut, bas, gauche, droite)
- **Bonus automatiques** : Les bonus sont ramassés automatiquement en marchant dessus
- **Énergie** : Chaque déplacement coûte de l’énergie ; rester immobile permet de la restaurer

## Composants du jeu

### Décor
| Élément | Description |
|--------|-------------|
| Herbe | Coût faible en énergie |
| Terre | Coût plus élevé |
| Bosquet de fleurs | Accessible uniquement aux insectes |
| Arbres | Infranchissables |
| Portes | S’ouvrent après ramassage de toutes les carottes |
| Nids | Produisent des guêpes ou frelons à intervalle régulier |

### Bonus
| Élément | Effet |
|--------|-------|
| Carotte | Nécessaire pour ouvrir les portes |
| Pomme | +50 énergie, soigne les maladies |
| Trognon pourri | +1 niveau de maladie pendant un certain temps |
| Bombe insecticide | Tue une guêpe, blesse un frelon |

### Ennemis
| Insecte | Détails |
|---------|--------|
| Guêpe | Meurt après une piqûre ou sur une bombe |
| Frelon | Nécessite deux piqûres ou deux bombes pour mourir |

## Architecture

Le projet repose sur des principes d'encapsulation, d’héritage et de **double dispatch dynamique** pour gérer les interactions entre le jardinier et les bonus.
Pour une explication complète de l’architecture du projet, des classes et de leur interaction, consultez le document PDF dédié disponible dans le dépôt.


