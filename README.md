#  TheOffice

## Description

**TheOffice** est une API REST développée en Java avec Spring Boot. Elle permet la gestion complète d’une entreprise, incluant :

- La gestion des entreprises (`Company`)
- Le suivi de la popularité
- La gestion des machines, du personnel et des matières premières
- Un système sécurisé d’authentification avec JWT

---

## Stack technique

- **Langage principal** : Java
- **Framework** : Spring Boot, Spring Security
- **Base de données** : MySQL
- **Outils complémentaires** : JWT (JSON Web Token)

---

## Architecture

Le projet suit une architecture modulaire organisée en packages :

- `controller` – Gère les routes et les requêtes HTTP
- `service` – Contient la logique métier
- `dao` – Accès aux données (Data Access Object)
- `entities` – Modèles représentant les entités de la base de données
- `exceptions` – Gestion des erreurs personnalisées
- `security` – Configuration de la sécurité (JWT, filtres, etc.)

---

##  Fonctionnalités principales

- Authentification sécurisée avec **JWT**
- API RESTful complète

### Endpoints disponibles :

#### CRUD complet :
- `Company`
- `Machine`
- `User`

#### CRU :
- `Cycle`
- `Event`
- `Loan`
- `StockFinalMaterial`
- `StockMaterial`
- `Supplier`

#### R uniquement :
- `Employee`

#### CRD :
- `Statistic`

---

##  API REST

Toutes les routes sont accessibles via les endpoints REST. L’authentification par JWT est requise pour la majorité des routes sécurisées.

---

##  Installation et lancement

### Prérequis :
- Java Development Kit (JDK) **version 23.0.1**
- MySQL en local ou à distance
- Maven ou Gradle

### Étapes :

1. Cloner le projet :
   ```bash
   git clone {clé SSH}
