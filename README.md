# shoppingcart

# Welcome to ShoppingCart RestApi!

This is for shoppingcart backend RestApi exercise

#Tech Stack
- SpringBoot
- H2 Memory Database
- Maven
- initial data
If you need more products, you can update resource/sql/data.sql

#How to Run
- `mvn spring-boot:run`
- download postman_script and import, you can test url directly.

#Swagger UI
http://localhost:8081/swagger-ui/

#Endpoints
- GET /v1/api/products  Get all products
- GET /v1/api/products/{prodcutId} Get particular product detail by productId
- POST v1/api/orders Place Order 

#TODO
- Update Runtime using Docker