import React, { useState } from 'react';
import {Link, useNavigate} from "react-router-dom";
import {loginUser} from "../../security/service/loginService";
import {Alert, Button, Checkbox, FormControlLabel, Grid, Paper, Snackbar, TextField, Typography} from "@mui/material";

const Login = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [alertMessage, setAlertMessage] = useState('');
    const [rememberMe, setRememberMe] = useState(false);

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!username.trim() || !password.trim()) {
            setOpenSnackbar(true);
            setAlertMessage('Username and password cannot be empty.');
            return;
        }

        const didSucceed = await loginUser(username, password);

        if (didSucceed) {
            localStorage.setItem('loginMethod', 'login');
            localStorage.setItem("rememberMe", rememberMe.toString());
            navigate('/shortener');
        } else {
            setOpenSnackbar(true);
            setAlertMessage('Login failed. Please try again.');
        }
    };

    const handleSkipLogin = () => {
        localStorage.setItem('loginMethod', 'skip');
        navigate('/shortener');
    };

    return (
        <Grid container justifyContent="center" alignItems="center" style={{ height: '100vh' }}>
            <Snackbar
                open={openSnackbar}
                autoHideDuration={4000}
                onClose={() => setOpenSnackbar(false)}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={() => setOpenSnackbar(false)} severity="error">
                    {alertMessage}
                </Alert>
            </Snackbar>
            <Grid item>
                <Paper elevation={3} style={{ padding: '20px', width: '300px' }}>
                    <Typography variant="h5" align="center">Login</Typography>
                    <form onSubmit={handleSubmit}>
                        <TextField
                            label="Username"
                            variant="outlined"
                            fullWidth
                            margin="normal"
                            value={username}
                            onChange={(e) => setUsername(e.target.value)}
                            required
                        />
                        <TextField
                            label="Password"
                            type="password"
                            variant="outlined"
                            fullWidth
                            margin="normal"
                            value={password}
                            onChange={(e) => setPassword(e.target.value)}
                            required
                        />
                        <FormControlLabel
                            control={
                                <Checkbox
                                    checked={rememberMe}
                                    onChange={(e) => setRememberMe(e.target.checked)}
                                    color="primary"
                                />
                            }
                            label="Remember Me"
                        />
                        <Button
                            type="submit"
                            variant="contained"
                            color="primary"
                            fullWidth
                            style={{ marginTop: '20px' }}
                        >
                            Login
                        </Button>
                    </form>
                    <div style={{ textAlign: 'center', marginTop: '15px' }}>
                        <Link to="/register" style={{ textDecoration: 'none' }}>
                            <Button variant="outlined" color="primary" style={{ marginRight: '10px' }}>
                                Register Now
                            </Button>
                        </Link>
                        <Button onClick={handleSkipLogin} variant="outlined" color="primary">
                            Skip Login
                        </Button>
                    </div>
                </Paper>
            </Grid>
        </Grid>
    );
};

export default Login;