import React from 'react';
import { Navigate } from 'react-router-dom';
import localStorageService from "../../storage/service/localStorageService";
import jwtService from "../service/jwtService";

const ProtectedRoute = ({ element }) => {
    const skipLogin = localStorageService.getItem('loginMethod');
    if (skipLogin && skipLogin === 'skip') {
        return element;
    }
    const isAuthenticated = localStorageService.exists('loginMethod');
    if (!isAuthenticated) {
        return <Navigate to={"/"} />
    }
    const token = jwtService.getToken()
    if (!token?.jwt || !token?.expirationAt) {
        return <Navigate to={"/"} />
    }
    const expiredAt = new Date(token.expirationAt);
    if (expiredAt < new Date()) {
        return <Navigate to={"/"} />
    }
    return element;
};

export default ProtectedRoute;