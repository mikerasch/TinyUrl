import React, {useEffect, useState} from 'react';
import jwtService from "../../security/service/jwtService";
import localStorageService from "../../storage/service/localStorageService";


const Navbar = () => {
    const [isLoggedIn, setIsLoggedIn] = useState(false);

    useEffect(() => {
        setIsLoggedIn(jwtService.isLoggedIn());
    }, []);

    function onLogin() {
        window.location.href = '/login';
    }

    function onLogout() {
        jwtService.remove();
        localStorageService.removeItem('rememberMe');
        window.location.href = '/';
    }

    return (
        <nav style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '10px 20px',
            backgroundColor: '#333',
            color: 'white',
            position: 'fixed',
            top: 0,
            right: 0,
            left: 0,
            zIndex: 1000
        }}>
            <h1 style={{ margin: 0 }}></h1>
            <div>
                {isLoggedIn ? (
                    <button onClick={onLogout} style={{
                        padding: '10px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#f44336',
                        color: 'white',
                        cursor: 'pointer'
                    }}>
                        Logout
                    </button>
                ) : (
                    <button onClick={onLogin} style={{
                        padding: '10px',
                        border: 'none',
                        borderRadius: '5px',
                        backgroundColor: '#4CAF50',
                        color: 'white',
                        cursor: 'pointer'
                    }}>
                        Login
                    </button>
                )}
            </div>
        </nav>
    );
};

export default Navbar;