import {API_URL, MINIMIZE_PATH} from "../../constants/constants";
import delegateFetch from "../../utils/delegateFetch";

export const minimizeUrl = async (url, strategy) => {
    try {
        const response = await delegateFetch(
            API_URL + MINIMIZE_PATH + '?url=' + encodeURIComponent(url) + '&strategy=' + encodeURIComponent(strategy),
            {method: 'POST'},
            true
        );
        return await response.json();
    } catch (error) {
        console.error(error);
    }
}