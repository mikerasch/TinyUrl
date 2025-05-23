import React, { useState } from 'react';
import {registerUser} from "../service/registerService";
import {useNavigate} from "react-router-dom";
import {Alert, Button, Grid, Paper, Snackbar, TextField, Typography} from "@mui/material";

const Register = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const navigate = useNavigate();
    const [openSnackbar, setOpenSnackbar] = useState(false);
    const [alertMessage, setAlertMessage] = useState('');
    const passwordPattern = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{8,}$/;
    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!passwordPattern.test(password)) {
            setOpenSnackbar(true);
            setAlertMessage('Password must be at least 8 characters long and include uppercase, lowercase, number, and special character.');
            return;
        }

        const didSucceed = await registerUser(username, password);
        if (didSucceed) {
            localStorage.setItem('loginMethod', 'login');
            navigate("/shortener")
        } else {
            setOpenSnackbar(true);
            setAlertMessage('Registration failed. Please try again.');
        }
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
                    <Typography variant="h5" align="center">Register</Typography>
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
                        <div style={{ display: 'flex', justifyContent: 'center', marginTop: '20px' }}>
                            <Button
                                variant="outlined"
                                color="primary"
                                onClick={() => navigate('/')}
                                style={{ marginRight: '10px' }}
                            >
                                Back
                            </Button>
                            <Button
                                type="submit"
                                variant="contained"
                                color="primary"
                            >
                                Register
                            </Button>
                        </div>
                    </form>
                </Paper>
            </Grid>
        </Grid>
    );
};

export default Register;