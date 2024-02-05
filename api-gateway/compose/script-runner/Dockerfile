# Use Alpine Linux as the base image
FROM alpine:3.19.1

# Install bash, curl, and jq
RUN apk add --no-cache bash curl jq

# Copy the Keycloak setup script into the container
COPY setup-keycloak.sh /setup-keycloak.sh

# Ensure the script is executable
RUN chmod +x /setup-keycloak.sh

# Optional: Specify a default environment variable if your script uses them
# ENV KEYCLOAK_HOST=keycloak
# ENV KEYCLOAK_PORT=8080
# ENV KEYCLOAK_USER=admin
# ENV KEYCLOAK_PASSWORD=admin
# ENV REALM_NAME=myrealm
# ENV CLIENT_ID=myclient
# ENV CLIENT_REDIRECT_URI="http://localhost:8080/myapp/*"

# Set the entrypoint to execute your script
ENTRYPOINT ["/setup-keycloak.sh"]

# Optionally, provide a CMD as default parameters to your script
# CMD ["param1", "param2"]
