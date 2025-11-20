# Online Form Management System (School Project)

This project is a school assignment inspired by Google Forms / Microsoft Forms.  
It is an online form management system where users can create forms, add questions, share them with other users, and collect and analyse responses.

The application is built on a fixed database schema provided by the teachers and focuses heavily on **business rules, data validation and access control** rather than on changing the model.

---

## Main Features

- **User management & authentication**
  - Login with email/password or predefined test accounts
  - Three roles: `guest`, `user`, `admin`
  - Profile view & edit, password change with strong password rules

- **Form management**
  - Create, edit and delete forms
  - Title, description and “public / private” flag
  - A form becomes read-only once it has responses (no more editing questions)

- **Question management**
  - Multiple question types:
    - `short` – single-line text
    - `long` – multi-line text
    - `date`
    - `email`
    - `check` – multiple choice (checkboxes)
    - `combo` – single choice (combobox)
    - `radio` – single choice (radio buttons)
  - Required flag, validation rules and unique constraints per form (index, title)
  - Option lists reusable across questions (system lists + user-owned lists)
  - Reordering of questions with automatic renumbering

- **Option lists**
  - Create, edit, duplicate and delete option lists
  - Each list contains ordered options (with an index starting at 1)
  - Alphabetical reorder, manual reorder and consistency checks
  - System option lists only editable by admins

- **Form instances & answers**
  - When a user opens a form, an **instance** is created
  - Answers are saved progressively while navigating through questions
  - Required questions must be answered before submission
  - Submitted instances become read-only
  - For `check` questions, multiple answers are stored with indexed options

- **Access rights & sharing**
  - Public forms: anyone (including guest) can answer them
  - Private forms: only owner, admins, and explicitly shared users/lists
  - Two access levels:
    - `user` – can only answer and view their latest instance
    - `editor` – can edit the form (except managing shares)
  - Sharing with:
    - Individual users
    - Distribution lists (custom groups of users)

- **Distribution lists**
  - Create named distribution lists owned by a user
  - Add/remove users to/from a list
  - Share forms with a full distribution list in one action

- **Statistics & analysis**
  - Per-form statistics on submitted instances
  - For each question:
    - list of distinct answers
    - count of occurrences
    - ratio per answer
  - Supports both single-choice and multiple-choice questions

- **Instances management**
  - List all submitted instances for a form
  - Open an instance in read-only mode
  - Delete single instances or all instances (with confirmation)

- **Cascading delete**
  - Deleting a form also deletes:
    - its questions
    - its instances
    - all related answers and shares
  - All destructive actions are confirmed with explicit messages

---

## Technical context

- Fixed relational schema (tables for users, forms, questions, instances, answers, option lists, distributions lists, shares, etc.)
- Strong business validations implemented in the application layer
- Project developed as part of the **TGPR** course (2024–2025, EPFC).

> This project is mainly focused on **data modeling, validation rules and access rights**, not on changing the database structure.
