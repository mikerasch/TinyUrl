import {Navigate} from "react-router-dom";
import localStorageService from "../../storage/service/localStorageService";

const RememberMeRoute = ({element}) => {
    const rememberMe = localStorageService.getItem('rememberMe');
    const skipLogin = localStorageService.getItem('loginMethod');
    if (skipLogin && skipLogin === 'skip') {
        return element;
    }
    if ((rememberMe && rememberMe === 'true')) {
        return <Navigate to={"/shortener"} />
    }
    return element;
};

export default RememberMeRoute;