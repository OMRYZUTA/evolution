import DialogTitle from '@mui/material/DialogTitle';
import Dialog from '@mui/material/Dialog';
import {makeStyles} from "@mui/styles";
import React from "react";
import Typography from "@mui/material/Typography";

const useStyles = makeStyles((theme) => ({
    root: {
        padding: '50px 70px',
        spacing: 2,
        justifyContent: 'flex-start',
        alignItems: 'top-center',
        minHeight: "100vh",
    },
    settings: {
        spacing: 2,
        justifyContent: "space-between",
        width: '100%',
        height: 400,
        maxWidth: 300,
        backgroundColor: "#D3D3D3", //light gray
    },
    actions: {
        padding: 20,
        margin: 20,
        justifyContent: 'space-evenly',
    },
    tempGrid: {
        padding: 20,
        margin: 20,
        backgroundColor: "pink",
    },
}));

const SolutionDialog = ({open, onClose}) => {
    const classes = useStyles();

    return (
        <Dialog onClose={() => {
            console.log("handle Dialog Close")
        }} open={open}>
            <DialogTitle>Best Solution</DialogTitle>
            <Typography>just words for now</Typography>
        </Dialog>
    );
}

export default SolutionDialog;