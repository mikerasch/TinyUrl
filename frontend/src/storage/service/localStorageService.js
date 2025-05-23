const localStorageService = {
    setItem(key, value) {
        localStorage.setItem(key, JSON.stringify(value));
    },
    getJsonItem(key) {
        return JSON.parse(localStorage.getItem(key));
    },
    getItem(key) {
        return localStorage.getItem(key);
    },
    exists(key) {
        return localStorage.getItem(key) !== null;
    },
    removeItem(token) {
        localStorage.removeItem(token);
    }
}

export default localStorageService;