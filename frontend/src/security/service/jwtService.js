import localStorageService from "../../storage/service/localStorageService";

const jwtService = {
    getToken() {
        const token = localStorageService.getJsonItem("token");
        return token || null;
    },
    setToken(token) {
        localStorageService.setItem("token", token);
    },
    isLoggedIn() {
        return localStorageService.getItem('loginMethod') === 'login';
    },
    remove() {
        localStorageService.removeItem('token');
        localStorageService.removeItem('loginMethod');
    }
}

export default jwtService;