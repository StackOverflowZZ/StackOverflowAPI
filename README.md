# StackOverflowAPI
Back-end of the application StackOverflow

# Features
- Provides a Rest API for operating with Question, Answer, Comment, User, Tag entities.
- Provides a health check: request to know if the service is up.
- Provides feature flipping: disable or enable features.
- Provides authentication through JWT.

# Circuit beaker
The circuit breaker is not implemented in the project yet because it would be useless in our case. The mecanism can be implemented if a distant service is necessary : if there is an error we displayed a default data, otherwise we will get the data of the service.

# Deployment
To install and run the application:

- Install Grails
- Run the application with the command ```grails run-app```