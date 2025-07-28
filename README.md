# 📚 Library Management System

A **Java + MySQL** based console application to manage library operations with role-based access for Admin, Librarian, Clerk, and Borrowers. This project simulates a real-world library environment and demonstrates database integration, user authentication, and transaction handling using core Java.

---

## 💡 Features

- 🔐 **Login/Register Functionality**
  - Admin (hardcoded)
  - Librarian
  - Clerk
  - Borrower (can register and login)

- 📚 **Book Management**
  - Add, View, Delete books
  - Change book status (available/unavailable)

- 👤 **User Management**
  - Add/Delete/View Borrowers (Admin, Clerk, Librarian)
  - Add/Delete Clerk and Librarian (Admin only)
  - View all registered users (Admin)

- 📖 **Borrowing System**
  - Borrowers can reserve available books
  - Track borrower transaction history

- ✅ **Input Validation**
  - User inputs are validated for range and format

---

## 🛠️ Tech Stack

| Tech       | Description                                |
|------------|--------------------------------------------|
| Java       | Core Java (OOP, Collections, Scanner, etc.)|
| MySQL      | Database to store users, books, and transactions |
| JDBC       | Java Database Connectivity for SQL queries |
| CLI        | Console-based interface                    |

---

## 🔐 Roles & Permissions

| Role       | Features |
|------------|----------|
| **Admin**      | Manage all users, books, and view all transactions |
| **Librarian**  | Manage borrowers and books |
| **Clerk**      | Manage borrowers and change book status |
| **Borrower**   | View/reserve books and see transaction history |

---

## 🧪 How to Run

### 🧰 Prerequisites
- Java JDK (8 or higher)
- MySQL Server
- MySQL Connector JAR (for JDBC)

### 🖥️ Setup

1. **Clone the Repository**
   ```bash
   git clone https://github.com/[your-username]/Library-Management-System.git
   cd Library-Management-System
