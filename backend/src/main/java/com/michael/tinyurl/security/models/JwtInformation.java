package com.michael.tinyurl.security.models;

import java.util.Date;

public record JwtInformation(String jwt, Date issuedAt, Date expirationAt) {}
