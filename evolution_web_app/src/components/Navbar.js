import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import {makeStyles} from "@mui/styles";

const useStyles = makeStyles((theme) => ({
    toolbar: {
        backgroundColor: 'lightyellow',
        color: 'gray',
    },
}))

export default function Navbar({user}) {
    const classes = useStyles();

    return (
        <Box sx={{flexGrow: 1}}>
            <AppBar position="static" className={classes.toolbar}>
                <Toolbar className={classes.toolbar}>
                    Welcome {user}
                </Toolbar>
            </AppBar>
        </Box>
    );
}
