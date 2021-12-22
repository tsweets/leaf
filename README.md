# Leaf Fake Payment Card Processor

***
The Leaf fake payment card processor is standin for a real payment processor. This is used by an Enterprise Application
that I work on because of the level of test automation I use, I need a fake system to connect to. This is because the
test system that the vendor (Who shall remain nameless)
provides is pretty weak and I can't completely exercise the system under test.

Security is not an issue since all of the data is completely fake. There are no security concerns as this is not an
exact clone of the vendor's API. No harmful information can be gleamed by any bad actors that are looking at this code.

### Things a processor needs to do

1. Add Cardholder
2. Replace any Card with a Personalized Card
3. Replace any Card with a Instant Issue Card
4. Update Cardholder Information (Name, Address, Contact)
   1. Name
   2. Phone
   3. Address
   4. Email
   5. DOB
   6. SSN
5. Get Account Balance
6. Lookup Cardholder by External ID

### Things I need to replicate

- Create New Card Acct
- Account Inquiry (Given Card Number return Card Account)
- Prepaid Adjustment (CardNumber, Amount - From PM Acct - Used for Fee)
- Card to Card Transfer (Source CN, Dest CN, Amount)
- Tx History Detail
- Generate Card Number (need to know card type)
- Name/Address Change
- Negative File Maintenance (Close Card)
- Primary Account Change (Move DDA to a Card)
- Replace Card

### REST API Documentation

http://localhost:8080/swagger-ui.html

### Properties

Probably want to overwrite

``` 
-Dleaf.processor.prefix=123456  -Dleaf.processor.dda-prefix=1234567
```

### Docker Information

mvn clean compile jib:dockerBuild

#### Running

docker run -p 5000:5000 --env "SPRING_PROFILES_ACTIVE=prod" leaf-processor:latest

#### Push

docker image tag e447c2968705 tsweets/leaf-processor:latest docker push tsweets/leaf-processor:latest
