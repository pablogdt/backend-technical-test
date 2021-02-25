## Summary

TUI DX Backend technical Test

The base project uses lombok, so you have to install it. You can use the following guide https://www.baeldung.com/lombok-ide


### Remarks

1. I would have validated controllers inputs with Spring Validation, but I couldn't add the maven library with hibernate implementation to do that.
2. I also though about using Spring security to secure the API, but I couldn't add this dependency and I did I very simple authentication mechanism through two headers (ApiKey and Secret) which the values to compare to are in the properties file. This isn't by far the best way to do it, but supposing some parts are mocked I thought this data could be there. I also could have created a DB to store securely the credentials, but I left it out of the scope.
3. All booking operations are asynchronous. Every operation returns a response with a 201 code and a location to check the task information. The retrieve booking operation is a bit different because it retrieves a whole booking, and it has an endpoint to check specifically the result (getting a booking response instead of a task info)
4. I couldn't create a cbranch in your remote so that is the reason I had to commit all together in my forked project.
