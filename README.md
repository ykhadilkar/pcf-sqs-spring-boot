# Sample Spring Boot and AWS SQS Integration for PCF

### Pre-requesite
- Download and Install PCF AWS Service Broker tile from [AWS Labs Github site](https://github.com/awslabs/aws-servicebroker/releases).
- Create new service instance of type SQS and name it **sqs**

### Properties
- If running locally populate values of SQS Url, Access Key and Secret access key in ``application.properties`` file.

### Build and Run
-  mvn clean install
- java -jar target/pcf-aws-queue-0.0.1-SNAPSHOT.jar

### Run on PCF
- cf push

### Usage
- Post message to the queue.
    - ```bash
      http -f post http://localhost:8090/messages body=Hello
      ```
- Observe logs to see message with ``Hello`` text show up.
- Delete all messages from Queue
    - ```bash
      http -f delete http://localhost:8090/messges  
      ```
- Observe logs to see listener return no messages.

