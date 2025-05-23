import delegateFetch from "../../utils/delegateFetch";
import {API_URL, SHORTENER_SEARCH_BY_ID_PATH} from "../../constants/constants";

const searchService = {
    searchById: async (id) => {
        try {
            const response = await delegateFetch(API_URL + SHORTENER_SEARCH_BY_ID_PATH + '?id=' + id, {method: 'GET'}, false);
            return await response.json();
        } catch (error) {
            return null;
        }
    }
}

export default searchService;