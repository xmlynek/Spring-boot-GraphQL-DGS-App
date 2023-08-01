# Spring boot Netflix DGS GraphQL Federation project

This is a modified project from the Udemy course [Code GraphQL Application : Java Spring Boot 3 & Netflix DGS - Timotius Pamungkas](https://www.udemy.com/course/code-graphql-application-with-java-spring-boot-netflix-dgs/).

This project was done just for demonstration purposes.



The repository contains three services:

1.  `products`: Java GraphQL service running on port 8081
2.  `sales`: Java GraphQL service running on port 8080
3.  `apollo-gw`: Node.js service of Apollo Server acting as the Federated Gateway running on port 4000

## Quick start
1.  Have running postgres database
2.  Set [products-application.yml](https://github.com/xmlynek/Spring-boot-GraphQL-DGS-App/blob/master/products/src/main/resources/application.yml) and
   [sales-application.yml](https://github.com/xmlynek/Spring-boot-GraphQL-DGS-App/blob/master/sales/src/main/resources/application.yml) datasource url
3.  Start both `products` and `sales` apps
4.  Run `npm install` in the `apollo-gw` project
5.  Run `npm start` or `node index.js` in the `apollo-gw` project
6.  Open http://localhost:4000 for the query editor


## GraphQL services schema
   ![Arrows - GraphQL Schema](https://github.com/xmlynek/Spring-boot-GraphQL-DGS-App/assets/70724986/041ca221-0dc6-47ca-8ae9-e79f83975edb)


