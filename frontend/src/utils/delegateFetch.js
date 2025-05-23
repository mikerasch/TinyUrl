import jwtService from "../security/service/jwtService";
import {HTTP_STATUS_TO_NOT_RETRY} from "../constants/constants";

const delegateFetch = async (url, options, authRequired = true, maxRetries = 3) => {
    let attempts = 0;
    const token = jwtService.getToken()?.jwt ?? null;
    if (authRequired && token) {
        options.headers = {
            ...options.headers,
            Authorization: `Bearer ${token}`
        };
    }

    while (attempts < maxRetries) {
        try {
            const response = await fetch(url, options);
            if (!response.ok && !HTTP_STATUS_TO_NOT_RETRY.includes(response.status)) {
                attempts++;
            } else {
                return await response;
            }
        } catch (error) {
            attempts++;
            if (attempts === maxRetries) {
                throw new Error('Max retries reached. Request failed.');
            }
        }
    }
};

export default delegateFetch;