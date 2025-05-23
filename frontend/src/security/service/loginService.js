import {API_URL, LOGIN_PATH} from "../../constants/constants";
import jwtService from "./jwtService";
import delegateFetch from "../../utils/delegateFetch";

export const loginUser = async (username, password) => {
    try {
        console.log(API_URL + " HERE")
        const response = await delegateFetch(API_URL + LOGIN_PATH, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({username, password})
        }, false);

        if (response.ok) {
            jwtService.setToken(await response.json());
        }
        return response.ok;
    } catch (error) {
        console.error(error);
    }
}