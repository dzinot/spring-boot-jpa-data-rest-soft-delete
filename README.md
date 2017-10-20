# Spring Boot JPA Soft Deletes with Spring Data Rest

Simple example of how to use custom JPA repository implementation for soft deletes functionality. 

## List of Things

* Service layer
* [Project Lombok](https://projectlombok.org/)
* Controller layer
* Repository layer
* Entity Validation
* JSON Configuration
* usage of JPA Specifications
* generic soft delete functionality
* custom JPA Repository implementation
* generic schedule soft delete functionality
* association table entities are with ```@IdClass```
* Spring Data Rest endpoints merged with custom controller endpoints
* generic methods to use in repositories (see the methods from the interface below)
* on creation of new entity check whether a soft deleted version already exists in the database
* BackendIdConverter for every association entity so you can use Spring Data REST to manipulate entity 
  * ```http GET localhost:8080/roleUsers/1_1``` 
  * ```http PATCH localhost:8080/roleUsers/1_1 otherField="field"```
  * ```http DELETE localhost:8080/roleUsers/1_1``` // __TODO__ 
* use ```@UniqueConstraint``` inside ```@Table``` to define unique constraints __(MUST MATCH DATABASE SCHEMA)__
  * ```@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "single" }) })```
  * ```@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "single1" }), @UniqueConstraint(columnNames = { "single2" }) })```
  * ```@Table(uniqueConstraints = { @UniqueConstraint(columnNames = { "double1", "double2" }) })```
* CustomRepositoryRestConfigurerAdapter to register the BackendIdConverters and expose the ID fields for all entities
* integration of the entity check functionality on create with Spring Data REST, the check is done on Spring Data REST repository endpoints POST methods

## TODO

* write tests
* write examples and usage
* write documentation about everything
* make generic repository methods accessable trough Spring Data Rest endpoints

## Notes

* Because of using Lombok, mapped entities should be excluded from the ```toString()``` method
  * check the [User](src/main/java/com/kristijangeorgiev/softdelete/model/entity/User.java) entity for example  
  ```java
  @ToString(exclude = { "roles" })
  ```

## Generic Repository Methods
```java
long countActive();
```
```java
T findOneActive(ID id);
```
```java
boolean existsActive(ID id);
```
```java
Iterable<T> findAllActive();
```
```java
Iterable<T> findAllActive(Sort sort);
```
```java
Page<T> findAllActive(Pageable pageable);
```
```java
Iterable<T> findAllActive(Iterable<ID> ids);
```
```java
void softDeleteAll();
```
```java
void softDelete(ID id);
```
```java
void softDelete(T entity);
```
```java
void softDelete(Iterable<? extends T> entities);
```
```java
void scheduleSoftDelete(ID id, LocalDateTime localDateTime);
```
```java
void scheduleSoftDelete(T entity, LocalDateTime localDateTime);
```

## Entity mapping examples

// 

## Example HTTP calls

//

## In Short

In order to use this, we first must create our [CustomJpaRepositoryFactoryBean](src/main/java/com/kristijangeorgiev/softdelete/util/CustomJpaRepositoryFactoryBean.java) and enable that in our main class using:
```java
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
```

This returns our custom [SoftDeletesRepositoryImpl](src/main/java/com/kristijangeorgiev/softdelete/repository/SoftDeletesRepositoryImpl.java) instead of the default SimpleJpaRepository.

There's a [BaseEntity](src/main/java/com/kristijangeorgiev/softdelete/model/entity/BaseEntity.java) which all of our entities must extend, that contains the necessary field to enable us to use this functionality.

Now for every repository interface, instead of extending the JpaRepository we extend our [SoftDeletesRepository](src/main/java/com/kristijangeorgiev/softdelete/repository/SoftDeletesRepository.java) ([UserRepository](src/main/java/com/kristijangeorgiev/softdelete/repository/UserRepository.java) example).

## Installing

Just clone or download the repo and import it as an existing maven project.

You'll also need to set up [Project Lombok](https://projectlombok.org/) or if you don't want to use this library you can remove the associated annotations from the code and write the getters, setters, constructors, etc. by yourself.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
