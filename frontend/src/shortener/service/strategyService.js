import {API_URL, STRATEGY_TYPES_PATH} from "../../constants/constants";
import delegateFetch from "../../utils/delegateFetch";

export const fetchStrategies = async () => {
    try {
        const response = await delegateFetch(
            API_URL + STRATEGY_TYPES_PATH,
            {},
            false
        );

        return response.json();
    } catch (error) {
        console.error(error);
        return [];
    }
}