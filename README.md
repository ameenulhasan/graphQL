# 🚀 GraphQL API – Spring Boot Integration

This project demonstrates the implementation of **GraphQL** with Spring Boot, enabling powerful, flexible, and efficient API interactions.

---

## 🔧 Technologies Used

- `Spring Boot`
- `graphql-java`
- `graphql-kickstart`
- `GraphQLQueryResolver`, `GraphQLMutationResolver`
- `Lombok`, `JPA`, `MySQL`

---

## 🎯 Features

- Fetch data using `query` resolvers
- Modify data using `mutation` resolvers
- Supports deep/nested fetching
- JSON-based flexible API calls
- Works with tools like **GraphQL Playground**, **Altair**, or **Insomnia**

---

## ⚙️ Setup Instructions

1. **Run the Spring Boot App**
2. Access the GraphQL Playground at:


> If using graphiql-spring-boot-starter:

http://localhost:8080/graphql

---

## 🔍 Example Queries

graphQl.txt 

### ✅ Get all users

```graphql
query {
  allUsers {
    id
    name
    email
  }
}
