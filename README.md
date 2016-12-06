# Spring Boot JPA Soft Delete

Simple example of how to use custom JPA repository implementation for soft deletes functionality. 

## In Short

In order to use this, we first must create our [CustomJpaRepositoryFactoryBean](src/main/java/com/kristijangeorgiev/softdelete/util/CustomJpaRepositoryFactoryBean.java) and enable that in our main class using:
```
@EnableJpaRepositories(repositoryFactoryBeanClass = CustomJpaRepositoryFactoryBean.class)
```

This returns our custom [SoftDeletesRepositoryImpl](src/main/java/com/kristijangeorgiev/softdelete/repository/SoftDeletesRepositoryImpl.java) instead of the default SimpleJpaRepository.

There's a [BaseEntity](src/main/java/com/kristijangeorgiev/softdelete/model/entity/BaseEntity.java) which all of our entities must extend, that contains the necessary field to enable us to use this functionality.

Now for every repository interface, instead of extending the PagingAndSortingRepository we extend our [SoftDeletesRepository](src/main/java/com/kristijangeorgiev/softdelete/repository/SoftDeletesRepository.java) ([UserRepository](src/main/java/com/kristijangeorgiev/softdelete/repository/UserRepository.java) example).

## Installing

Just clone or download the repo and import it as an existing maven project.

You'll also need to set up [Project Lombok](https://projectlombok.org/) or if you don't want to use this library you can remove the associated annotations from the code and write the getters, setters, constructors, etc. by yourself.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details
