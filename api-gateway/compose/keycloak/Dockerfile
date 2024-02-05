# Builder stage for setting up the server and running the optimization
FROM quay.io/keycloak/keycloak:23.0.6 AS builder

# Configure a database vendor
ENV KC_DB=postgres

WORKDIR /opt/keycloak

# Note: Use proper certificates for production and ensure that they are securely handled
RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 \
    -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" \
    -keystore conf/server.keystore

# Run the build command to optimize Keycloak
RUN /opt/keycloak/bin/kc.sh build

# Final stage for the runtime image
FROM quay.io/keycloak/keycloak:23.0.6

# Copy the optimized Keycloak from the builder stage
COPY --from=builder /opt/keycloak/ /opt/keycloak/

# Set the runtime environment variables
ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://postgres/keycloak
ENV KC_DB_USERNAME=keycloak
ENV KC_DB_PASSWORD=password
ENV KC_HOSTNAME=localhost
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true

# Run Keycloak
ENTRYPOINT ["/opt/keycloak/bin/kc.sh", "start-dev"]