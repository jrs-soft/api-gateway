#!/bin/bash
# Placeholder for using kc.sh if applicable
# As of the last known update, kc.sh does not handle realm or client configurations directly

# Function to configure Keycloak via REST API
configure_keycloak_via_api() {
    # Ensure required variables are set
    : "${KEYCLOAK_HOST:?Variable not set}"
    : "${KEYCLOAK_PORT:?Variable not set}"
    : "${KEYCLOAK_USER:?Variable not set}"
    : "${KEYCLOAK_PASSWORD:?Variable not set}"
    : "${REALM_NAME:?Variable not set}"
    : "${CLIENT_ID:?Variable not set}"
    : "${CLIENT_REDIRECT_URI:?Variable not set}"

    # Obtain access token
    TOKEN=$(curl -s -X POST "http://${KEYCLOAK_HOST}:${KEYCLOAK_PORT}/realms/master/protocol/openid-connect/token" \
        -H "Content-Type: application/x-www-form-urlencoded" \
        -d "username=${KEYCLOAK_USER}" \
        -d "password=${KEYCLOAK_PASSWORD}" \
        -d 'grant_type=password' \
        -d 'client_id=admin-cli' | jq -r '.access_token')

    if [ "$TOKEN" == "null" ] || [ -z "$TOKEN" ]; then
        echo "Failed to obtain access token. Exiting."
        exit 1
    fi

    # Create realm
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://${KEYCLOAK_HOST}:${KEYCLOAK_PORT}/admin/realms" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{"id": "'$REALM_NAME'", "realm": "'$REALM_NAME'", "enabled": true}')

    if [ "$RESPONSE_CODE" != "201" ]; then
        echo "Failed to create realm. Server responded with status code $RESPONSE_CODE"
        exit 1
    fi

    # Create client
    RESPONSE_CODE=$(curl -s -o /dev/null -w "%{http_code}" -X POST "http://${KEYCLOAK_HOST}:${KEYCLOAK_PORT}/admin/realms/${REALM_NAME}/clients" \
        -H "Content-Type: application/json" \
        -H "Authorization: Bearer $TOKEN" \
        -d '{"clientId": "'$CLIENT_ID'", "directAccessGrantsEnabled": true, "publicClient": true, "redirectUris": ["'$CLIENT_REDIRECT_URI'"]}')

    if [ "$RESPONSE_CODE" != "201" ]; then
        echo "Failed to create client. Server responded with status code $RESPONSE_CODE"
        exit 1
    fi
}

# Main script execution
configure_keycloak_via_api

echo "Keycloak initialization completed."
