import {API_URL, SHORTENER_HISTORY_PATH} from "../../constants/constants";
import delegateFetch from "../../utils/delegateFetch";

export const fetchUrlShortenerHistory = async (page, size) => {
    try {
        const response = await delegateFetch(API_URL + SHORTENER_HISTORY_PATH + `?page=${page}&size=${size}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            }
        });
        return response.json();
    } catch (error) {
        console.error(error);
    }
}