package com.company.Server;

/**
 * These are the commands which will be sent across the network connection.
 */
public enum Command {
    ADD_USER,
    GET_ID_USER,
    GET_USERNAME_USER,
    CHECK_AVAILABILITY,
    DELETE_USER,
    GET_USER_SET,
    ADD_PERSON,
    GET_PERSON,
    GET_PERSON_SET,
    ADD_ORGANISATION_UNIT,
    GET_ORGANISATION_UNIT,
    GET_ORGANISATION_UNIT_SET,
    ADD_ASSET,
    GET_ASSET,
    UPDATE_ASSET,
    DELETE_ASSET,
    GET_ASSET_SET,
    CHECK_NAME,
    ADD_ORG_ASSET,
    GET_ORG_ASSET,
    GET_ORG_ASSET_SET,
    GET_MY_ORG_ASSET_SET,
    GET_ORG_ASSET_COUNT,
    GET_ASSET_LIST,
    UPDATE_ORG_ASSET_QUANTITY,
    LOGIN
}