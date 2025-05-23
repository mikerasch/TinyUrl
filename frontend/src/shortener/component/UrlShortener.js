import React, {useEffect, useState} from 'react';
import {fetchStrategies} from "../service/strategyService";
import {minimizeUrl} from "../service/minimizeService";
import {API_URL, REDIRECT_PATH} from "../../constants/constants";
import jwtService from "../../security/service/jwtService";
import UrlShortenerHistory from "./UrlShortenerHistory";
import Navbar from "../../navbar/components/Navbar";
import {
    Alert,
    Button,
    FormControlLabel,
    Grid,
    Paper,
    Radio,
    RadioGroup,
    Snackbar,
    TextField, Tooltip,
    Typography
} from "@mui/material";
import searchService from "../service/searchService";

const UrlShortener = () => {
    const [longUrl, setLongUrl] = useState('');
    const [lookupUrl, setLookupUrl] = useState('');
    const [lookupResult, setLookupResult] = useState('');
    const [strategy, setStrategy] = useState('default');
    const [strategies, setStrategies] = useState([]);
    const [loading, setLoading] = useState(true);
    const [shortenedUrl, setShortenedUrl] = useState('');
    const [isLoggedIn, setIsLoggedIn] = useState(false);
    const [history, setHistory] = useState([]);
    const [openSnackbar, setOpenSnackbar] = React.useState(false);
    const [alertMessage, setAlertMessage] = React.useState('');
    const [view, setView] = useState('shortener');

    useEffect(() => {
        const loadStrategies = async () => {
            try {
                const data = await fetchStrategies();
                setStrategies(data);
                const defaultStrategy = data.find((s) => s.isDefault);
                if (defaultStrategy) {
                    setStrategy(defaultStrategy.strategy);
                }
            } finally {
                setLoading(false)
            }
        };

        setIsLoggedIn(jwtService.isLoggedIn())
        loadStrategies();
    }, []);

    async function uiSearchById() {
        const response = await searchService.searchById(lookupUrl);
        setLookupResult(response);
    }

    const handleSubmit = async (e) => {
        e.preventDefault();

        try {
            new URL(longUrl);
        } catch (error) {
            setAlertMessage('Please enter a valid URL.');
            setOpenSnackbar(true);
            return;
        }

        const result = await minimizeUrl(longUrl, strategy);
        setShortenedUrl(API_URL + REDIRECT_PATH + result.id);
        if (isLoggedIn) {
            setHistory(prevHistory => [...prevHistory, result]);
        }
    };


    return (
        <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', paddingTop: '60px' }}>
            <Navbar/>
            <Snackbar
                open={openSnackbar}
                autoHideDuration={4000}
                onClose={() => setOpenSnackbar(false)}
                anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
            >
                <Alert onClose={() => setOpenSnackbar(false)} severity="error" sx={{ width: '400px' }}>
                    {alertMessage}
                </Alert>
            </Snackbar>

            <Grid container justifyContent="center" alignItems="center" style={{ }}>
                <Grid item>
                    <Paper elevation={3} sx={{
                        padding: '10px',
                        width: '300px',
                        display: 'flex',
                        flexDirection: 'column',
                        alignItems: 'center',
                        marginTop: { xs: '10%', md: '60%' }
                    }}>
                        <Typography variant="h5" align="center">URL Shortener</Typography>
                        <Typography variant="body2" align="center">
                            {isLoggedIn ? "Hey, you are logged in as a user!" : "Hey, you are logged in as anonymous!"}
                        </Typography>
                        <RadioGroup row value={view} onChange={(e) => setView(e.target.value)}>
                            <FormControlLabel value="shortener" control={<Radio />} label="Shorten URL" />
                            <FormControlLabel value="lookup" control={<Radio />} label="Lookup URL" />
                        </RadioGroup>
                        {view === 'shortener' ? (
                            <form onSubmit={handleSubmit} style={{ width: '100%' }}>
                                <TextField
                                    label="Enter long URL"
                                    variant="outlined"
                                    fullWidth
                                    margin="normal"
                                    value={longUrl}
                                    onChange={(e) => setLongUrl(e.target.value)}
                                    required
                                />
                                <TextField
                                    select
                                    label="Select Strategy"
                                    variant="outlined"
                                    fullWidth
                                    margin="normal"
                                    value={strategy}
                                    onChange={(e) => setStrategy(e.target.value)}
                                    SelectProps={{ native: true }}
                                >
                                    {loading ? (
                                        <option>Loading strategies...</option>
                                    ) : (
                                        strategies.map((strat, index) => (
                                            <option key={index} value={strat.strategy}>
                                                {strat.strategy}
                                            </option>
                                        ))
                                    )}
                                </TextField>
                                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: '20px' }}>
                                    Shorten URL
                                </Button>
                                {shortenedUrl && (
                                    <Typography variant="body2" align="center" style={{ marginTop: '10px' }}>
                                        Shortened URL: {shortenedUrl}
                                    </Typography>
                                )}
                            </form>) : (
                            <div style={{ width: '100%' }}>
                                <TextField
                                    label="Enter short URL's Identification"
                                    variant="outlined"
                                    fullWidth
                                    margin="normal"
                                    value={lookupUrl}
                                    onChange={(e) => setLookupUrl(e.target.value)}
                                    required
                                />
                                <Button type="submit" variant="contained" color="primary" fullWidth style={{ marginTop: '20px' }} onClick={uiSearchById}>
                                    Lookup URL
                                </Button>
                                {lookupResult && (
                                    <Tooltip title={lookupResult?.id} arrow>
                                        <Typography variant="body2" align="center" style={{ marginTop: '10px', overflow: 'hidden', textOverflow: 'ellipsis', whiteSpace: 'nowrap' }}>
                                            Full URL: {lookupResult?.id}
                                        </Typography>
                                    </Tooltip>
                                )}
                            </div>
                            )}
                    </Paper>
                </Grid>
            </Grid>

            {isLoggedIn && (
                <div style={{ marginTop: '20px', padding: '10px', display: 'flex', justifyContent: 'center' }}>
                    <UrlShortenerHistory addHistory={history} />
                </div>
            )}
        </div>
    );
};

export default UrlShortener;