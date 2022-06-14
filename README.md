# Traffic Tax Calculation

#### This is a simple spring boot application which exposes a rest endpoint for calculating vehicle's traffic taxation based on the provided list of date-time timestamps!

### How To Run.?!

* Make sure you have docker engine installed.
* Make sure you have docker-compose installed.
* Run `docker compose --profile app up -d` in the root directory of the project.

### What Happens Next.!?

* It will start a container from an image that it builds for you.
* You can visit http://127.0.0.1:8081/swagger-ui/index.html in your browser.
* Now if you enter `/v3/api-docs` in the explore text box and press enter, you will see the swagger information about.
  the endpoint

If you provide the JSON below as the input to the POST method:
```
{
    "registrationNumber": "XXX123",
    "vehicleType": "CAR",
    "dates": [
        "2022-05-16 12:24:24",
        "2022-05-16 15:24:24"
    ]
}
```

You will get this response back:
```
{
  "error": null,
  "tax": {
    "registrationNumber": "XXX123",
    "vehicleType": "CAR",
    "eventTime": [
      "2022-05-16T12:24:24",
      "2022-05-16T15:24:24"
    ],
    "tax": 21
  },
  "message": "tax calculated successfully"
}
```

### Externalising Application Configuration
You can put different configuration as application.yml under external-properties folder in case you want to have different time cost than the ones which is defined in this application.