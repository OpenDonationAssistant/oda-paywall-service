FROM fedora:44
WORKDIR /app
COPY target/oda-paywall-service /app

CMD ["./oda-paywall-service"]
