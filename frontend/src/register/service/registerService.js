import {API_URL, REGISTER_PATH} from "../../constants/constants";
import jwtService from "../../security/service/jwtService";
import delegateFetch from "../../utils/delegateFetch";
export const registerUser = async (username, password) => {
    try {
        const response = await delegateFetch(API_URL + REGISTER_PATH, {
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