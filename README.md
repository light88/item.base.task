# item.base.task

**Currency Conversion Service**  project

Currency Conversion Service -> CCS.

It is implemented using 'java 11', spring boot [2.2.6.RELEASE] and spring reactive web stack,
maven build tool.

To start up CCS run a command 'mvn spring-boot:run' 
which runs on port 8080 and has one endpoint under '/currency/convert'.

example: 

POST http://localhost:8080/currency/convert
Accept: */*
Content-Type: application/json

{
  "from": "USD",
  "to": "EUR",
  "amount": 10.0
}

For the moment it has two exchange rate providers 
ExchangeRateProviderOne (base endpoint https://api.exchangerate-api.com/v4/latest/EUR) 
ExchangeRateProviderTwo (base endpoint https://api.exchangeratesapi.io/latest?base=EUR)

There is a manager that return next provider to get rates or empty if all providers checked.
Provider is responsible to return rates result in unified form.
Application uses reactive router function to handle requests with help of handlers to process request.

!!! For the moment it doesn't have any request validations, will be implemented in the upcoming release ;)

[ Bonus Tasks ]
 
Authentication: 

If provider requires authentication then it has to provide a way to pass authentication and in response returns 
some token to use in upcoming requests (pass that token into 'Authorization' request header)
otherwise pass authentication data in every service call.

Improvements:

- As currency rate providers provide caching (response header Cache-Control: max-age ...) than the app can
use it in way of internal caching and do not trigger service calls.
- Good practice applying 'Content-Encoding' to reduce the amount of data to transfer over the network.
- Add validation to request data that come.