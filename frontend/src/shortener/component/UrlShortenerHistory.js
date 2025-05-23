import React, { useEffect, useState } from 'react';
import { fetchUrlShortenerHistory } from "../service/urlShortenerHistory";
import { Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Paper, Typography, TablePagination } from '@mui/material';
import {API_URL, REDIRECT_PATH} from "../../constants/constants";

const UrlShortenerHistory = ({ addHistory }) => {
    const [history, setHistory] = useState([]);
    const [page, setPage] = useState(0);
    const [rowsPerPage, setRowsPerPage] = useState(5);
    const [totalCount, setTotalCount] = useState(0);

    const fetchHistory = async (page, rowsPerPage) => {
        const history = await fetchUrlShortenerHistory(page, rowsPerPage);
        setHistory(history.content);
        setTotalCount(history.totalElements);
    };

    useEffect(() => {
        fetchHistory(page, rowsPerPage);
    }, [page, rowsPerPage]);

    const combinedHistory = [...history, ...addHistory];
    const handleChangePage = (event, newPage) => {
        setPage(newPage);
    };

    const handleChangeRowsPerPage = (event) => {
        setRowsPerPage(parseInt(event.target.value, 10));
        setPage(0);
    };

    return (
        <TableContainer
            component={Paper}
            style={{
                maxWidth: 450,
                maxHeight: 400,
                margin: '20px auto',
                background: '#add8e6',
                border: '3px solid #ccc',
                borderRadius: '8px',
            }}
        >
            <Typography variant="h6" align="center" style={{ padding: '20px' }}>Shortened URL History</Typography>
            <Table>
                <TableHead>
                    <TableRow>
                        <TableCell>Original URL</TableCell>
                        <TableCell>Shortened URL</TableCell>
                    </TableRow>
                </TableHead>
                <TableBody>
                    {combinedHistory.slice(0, rowsPerPage).length === 0 ? (
                        <TableRow>
                            <TableCell colSpan={2} align="center">No history available.</TableCell>
                        </TableRow>
                    ) : (
                        combinedHistory.slice(0, rowsPerPage).map((item, index) => (
                            <TableRow key={index} style={{ borderBottom: '2px solid #ccc' }}>
                                <TableCell>{item.url}</TableCell>
                                <TableCell>
                                    <a href={API_URL + REDIRECT_PATH + item.id} target="_blank" rel="noopener noreferrer">{item.id}</a>
                                </TableCell>
                            </TableRow>
                        ))
                    )}
                </TableBody>
            </Table>
            <TablePagination
                rowsPerPageOptions={[5, 10, 25]}
                component="div"
                count={totalCount}
                rowsPerPage={rowsPerPage}
                page={page}
                onPageChange={handleChangePage}
                onRowsPerPageChange={handleChangeRowsPerPage}
            />
        </TableContainer>
    );
};

export default UrlShortenerHistory;